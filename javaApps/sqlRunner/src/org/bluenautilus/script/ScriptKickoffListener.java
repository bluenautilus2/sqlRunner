package org.bluenautilus.script;

import org.bluenautilus.data.SqlScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/11/13
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ScriptKickoffListener {

    public void kickoffSelectedScripts(ScriptType type);

    public void singleScriptStarting(SqlScriptFile file, ScriptType type);

    public void kickoffAllToRunScripts();
}
