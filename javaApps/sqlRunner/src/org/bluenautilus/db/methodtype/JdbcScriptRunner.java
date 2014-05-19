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
	// (/\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*\*+/)|(--.*[\n\r])|(^commit ?.*$)
	private static final String REGEX_PARSING = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(--.*[\\n\\r])";
	private static final String REGEX_COMMIT_REMOVALS = "^commit( .*)?$";
	private static final String QUERY_SANDWICH = "begin transaction sqlRunnerTemp\n%s\ncommit transaction sqlRunnerTemp\n";


	@Override
	public ScriptResultsEvent runSqlCmdScript(ArrayList<ScriptCompletionListener> completionListeners, FieldItems items, SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
		String query = readInFile(scriptFile, type);
		ScriptResultsEvent results = getScriptResultsEvent(items, scriptFile, type, query);

		// if we fail, just try it again.
		if (results.isDbProblem()) {
			results = getScriptResultsEvent(items, scriptFile, type, query);
		}
		return results;
	}

	/**
	 * Perform the call to the DB.
	 * @param items
	 * @param scriptFile
	 * @param type
	 * @param query
	 * @return
	 */
	private ScriptResultsEvent getScriptResultsEvent(FieldItems items, SqlScriptFile scriptFile, ScriptType type, String query) {
		boolean dbProblem = true;
		Connection conn = null;
		CallableStatement cs = null;

		try {
			conn = DriverManager.getConnection(String.format(DB_STRING, items.getIpAddressField(), items.getPort(), items.getDbNameField()), items.getLoginField(), items.getPasswordField());
			conn.setAutoCommit(false);
			String[] queries = query.split("GO\n");

			// Go is a transaction barrier, so split there and run each "batch" separately.
			for (String q : queries) {
				// We also need to remove queries that the whole of their content is commit, we do that already.
				if (q.trim().toLowerCase().matches(REGEX_COMMIT_REMOVALS)) {
					continue;
				}
				// DDL goes in a query all of its own and all others are sandwiched in a transaction.
				if (q.trim().toLowerCase().startsWith("create ")) {
					cs = conn.prepareCall(q);
				} else {
					cs = conn.prepareCall(String.format(QUERY_SANDWICH, q));

				}
				cs.execute();
				conn.commit();
				cs.close();
			}
			dbProblem = false;

		} catch (SQLException e) {
			e.printStackTrace();
			// First roll back the transaction
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// Close the callable statement
			if (cs != null) {
				try {
					if (!cs.isClosed()) {
						cs.close();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

			// Close the connection
			if (conn != null) {
				try {
						conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

		return new ScriptResultsEvent("JDBC mode doesn't support output", scriptFile, type, dbProblem);
	}

	/**
	 * Read in the script and parse it for un-needed stuff.
	 * @param scriptFile
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoRunException
	 */
	private String readInFile(SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
		File file = null;
		StringBuilder sb = new StringBuilder();

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
	 * @param sb
	 * @return
	 */
	private String additionalParsing(StringBuilder sb) {
		return sb.toString().replaceAll(REGEX_PARSING, " ");
	}
}
