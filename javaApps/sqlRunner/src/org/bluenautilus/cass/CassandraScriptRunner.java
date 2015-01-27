package org.bluenautilus.cass;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.NoRunException;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;

import java.io.IOException;
import java.util.ArrayList;


public interface CassandraScriptRunner {

	ScriptResultsEvent runCassandraScript(ArrayList<ScriptCompletionListener> completionListeners,
                                       CassConfigItems items,
                                       SqlScriptFile scriptFile,
                                       ScriptType type) throws IOException, NoRunException;


}
