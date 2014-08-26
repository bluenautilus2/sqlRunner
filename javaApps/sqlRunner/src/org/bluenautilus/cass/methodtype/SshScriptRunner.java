package org.bluenautilus.cass.methodtype;

import org.bluenautilus.cass.CassandraScriptRunner;
import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.SqlScriptRunner;
import org.bluenautilus.script.NoRunException;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bstevens on 8/24/14.
 */
public class SshScriptRunner implements CassandraScriptRunner {

    @Override
    public ScriptResultsEvent runCassandraScript(ArrayList<ScriptCompletionListener> completionListeners, CassFieldItems items, SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
        return null;
    }
}
