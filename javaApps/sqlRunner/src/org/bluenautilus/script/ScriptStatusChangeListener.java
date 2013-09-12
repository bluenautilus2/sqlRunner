package org.bluenautilus.script;

import org.bluenautilus.data.SqlScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/11/13
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ScriptStatusChangeListener {

    public void updateTableRowStatus(SqlScriptFile file, ScriptStatus newStatus);

}
