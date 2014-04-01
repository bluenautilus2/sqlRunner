package org.bluenautilus.db.methodtype;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.ScriptRunner;
import org.bluenautilus.script.NoRunException;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jim on 3/28/14.
 * SQL CMD script runner.
 */
public class SqlCmdScriptRunner implements ScriptRunner {
	@Override
	public ScriptResultsEvent runSqlCmdScript(ArrayList<ScriptCompletionListener> completionListeners, FieldItems items, SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
		return new ScriptResultsEvent("SQL CMD not supported at this time.", scriptFile, type, true);
	}
}
