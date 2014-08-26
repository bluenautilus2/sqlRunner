package org.bluenautilus.cass;

import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.NoRunException;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;

import java.io.IOException;
import java.util.ArrayList;


public interface CassandraScriptRunner {

	ScriptResultsEvent runCassandraScript(ArrayList<ScriptCompletionListener> completionListeners,
                                       CassFieldItems items,
                                       SqlScriptFile scriptFile,
                                       ScriptType type) throws IOException, NoRunException;


}
