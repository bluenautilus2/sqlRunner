package org.bluenautilus.db.methodtype;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.SqlScriptRunner;
import org.bluenautilus.script.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by jim on 3/28/14.
 * SQL CMD script runner.
 */
public class SqlCmdScriptRunner implements SqlScriptRunner {
	private static final String CMD = "sqlcmd";
	private static final String DB_ERROR_FLAG = "Level 16, State";

	@Override
	public ScriptResultsEvent runSqlCmdScript(ArrayList<ScriptCompletionListener> completionListeners, FieldItems items, SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
		File oldfile = null;  //if this stays null something crazy is going on.

		if (ScriptType.REGULAR == type) {
			oldfile = scriptFile.getTheFile();
		} else if (ScriptType.ROLLBACK == type) {
			//returns a file obj even if the file may not exist
			oldfile = scriptFile.getRollbackFile();
		}

		ScriptModifier modifier = new ScriptModifier(oldfile);
		completionListeners.add(modifier);

		File newFile = modifier.createModifiedCopy();

		ProcessBuilder builder = new ProcessBuilder(this.CMD,
				"-U", items.getLoginField(),
				"-P", items.getPasswordField(),
				"-S", items.getIpAddressField() + "," + items.getPort(),
				"-d", items.getDbNameField(),
				"-i", newFile.getAbsolutePath());


		Process process = builder.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		StringBuilder strbuilder = new StringBuilder();

		boolean dbProblem = false;

		while ((line = br.readLine()) != null) {
			if (null != line) {
				int i = line.indexOf(DB_ERROR_FLAG);
				if (i != -1) {
					dbProblem = true;
				}
			}
			strbuilder.append(line);
			strbuilder.append("\n");
		}

		ScriptResultsEvent event = new ScriptResultsEvent(strbuilder.toString(), scriptFile, type, dbProblem);
		return event;
	}
}
