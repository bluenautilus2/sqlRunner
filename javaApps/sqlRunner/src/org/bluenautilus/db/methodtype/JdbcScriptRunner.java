package org.bluenautilus.db.methodtype;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.ScriptRunner;
import org.bluenautilus.script.*;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by jim on 3/28/14.
 * Script runner for JDBC connection types.
 */
public class JdbcScriptRunner implements ScriptRunner {
    private static final String DB_STRING = "jdbc:sqlserver://%s:%s;DatabaseName=%s";
	private static final String REGEX_PARSING = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(--.*[\\n\\r])";
    private static final String REGEX_PARSING_LONG_RUNTIME = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|((?<=([^']*'[^'\\r\\n]*'[^-\\r\\n']*)|([^-']+))--.*)|(\\n\\r?--.*)|(^--.*)";
    private static final String REGEX_COMMIT_REMOVALS = "^commit( .*)?$";
	private static final String QUERY_SANDWICH = "begin transaction sqlRunnerTemp\n%s\ncommit transaction sqlRunnerTemp\n";
    private static final String REGEX_PARSING_TEST_FOR_LONG_REQUIREMENT = "'[^-\\r\\n']*-(-+[^-\\r\\n']*)*'";


    @Override
	public ScriptResultsEvent runSqlCmdScript(final ArrayList<ScriptCompletionListener> completionListeners, final FieldItems items, final SqlScriptFile scriptFile, final ScriptType type) throws IOException, NoRunException {
		return getScriptResultsEvent(items, scriptFile, type, readInFile(scriptFile, type));
	}

	/**
	 * Perform the call to the DB.
	 * @param items Field values
	 * @param scriptFile script file
	 * @param type script type (rollback or regular)
	 * @param query String query
	 * @return script results
	 */
	private ScriptResultsEvent getScriptResultsEvent(final FieldItems items, final SqlScriptFile scriptFile, final ScriptType type, final String query) {
		boolean dbProblem = true;
		Connection conn = null;
		CallableStatement cs = null;
        final StringBuilder output = new StringBuilder();

		try {
			conn = DriverManager.getConnection(String.format(DB_STRING, items.getIpAddressField(), items.getPort(), items.getDbNameField()), items.getLoginField(), items.getPasswordField());
			conn.setAutoCommit(false);
			final String[] queries = query.split("GO\n");

			// Go is a transaction barrier, so split there and run each "batch" separately.
			for (final String q : queries) {
				// We also need to remove queries that the whole of their content is commit, we do that already.
				if (q.trim().toLowerCase().matches(REGEX_COMMIT_REMOVALS)) {
					continue;
				}
				// DDL goes in a query all of its own and all others are sandwiched in a transaction.
				if (q.trim().toLowerCase().startsWith("create ") || q.trim().toLowerCase().startsWith("alter procedure")) {
					cs = conn.prepareCall(q);
				} else {
					cs = conn.prepareCall(String.format(QUERY_SANDWICH, q));

				}
				cs.execute();
                conn.commit();
                // This is a dumb warning in this case.
                @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                SQLWarning warning = cs.getWarnings();
                while (warning != null) {
                    output.append(warning.getMessage());
                    warning = warning.getNextWarning();
                    output.append("\n");
                }
				cs.close();
			}
			dbProblem = false;

		} catch (final SQLException e) {
			e.printStackTrace();
            output.append(e.getMessage());
			// First roll back the transaction
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (final SQLException e1) {
				e1.printStackTrace();
			}

			// Close the callable statement
			if (cs != null) {
				try {
					if (!cs.isClosed()) {
						cs.close();
					}
				} catch (final SQLException e1) {
					e1.printStackTrace();
				}
			}

			// Close the connection
			if (conn != null) {
				try {
						conn.close();
				} catch (final SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

		return new ScriptResultsEvent(output.toString(), scriptFile, type, dbProblem);
	}

	/**
	 * Read in the script and parse it for un-needed stuff.
	 * @param scriptFile script file
	 * @param type type of script (rollback or regular)
	 * @return String value of the file contents
	 * @throws IOException
	 */
	private String readInFile(final SqlScriptFile scriptFile, final ScriptType type) throws IOException {
		File file = null;
		final StringBuilder sb = new StringBuilder();

		if(ScriptType.REGULAR == type){
			file = scriptFile.getTheFile();
		}else if(ScriptType.ROLLBACK == type){
			file = scriptFile.getRollbackFile();
		}
		if (file != null) {
			BufferedReader reader = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

				String line;

				while ((line = reader.readLine()) != null) {
					sb.append(String.format("%s\n", line.trim()));
				}
			} finally {
				if (fis != null) {
					fis.close();
				}
				if (reader != null) {
					reader.close();
				}
			}
		}

		return additionalParsing(sb);
	}

	/**
	 * Do the script parsing.
	 * @param sb unparsed script
	 * @return parsed script
	 */
	private String additionalParsing(final StringBuilder sb) {
        if (sb.toString().matches(REGEX_PARSING_TEST_FOR_LONG_REQUIREMENT)) {
            return sb.toString().replaceAll(REGEX_PARSING_LONG_RUNTIME, " ");
        } else {
            return sb.toString().replaceAll(REGEX_PARSING, " ");
        }
	}
}
