package org.bluenautilus.db;

import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.NoRunException;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jim on 3/28/14.
 */
public interface SqlScriptRunner {
	/**
	 * Do the call to the DB in whatever fashion is in style at the current moment.
	 * @param completionListeners
	 * @param items
	 * @param scriptFile
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoRunException
	 */
	ScriptResultsEvent runSqlCmdScript(ArrayList<ScriptCompletionListener> completionListeners,
									   SqlConfigItems items,
									   SqlScriptFile scriptFile,
									   ScriptType type) throws IOException, NoRunException;


}
