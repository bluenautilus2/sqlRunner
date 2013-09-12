package org.bluenautilus.script;

import org.bluenautilus.data.SqlScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/10/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptResultsEvent {

    private String output;
    private Exception theException;
    private SqlScriptFile scriptFile;

    public ScriptResultsEvent(String output, SqlScriptFile scriptFile) {
        this.output = output;

        this.scriptFile = scriptFile;

    }

    public ScriptResultsEvent(String output, SqlScriptFile scriptFile, Exception e) {
        this.output = output;
        this.theException = e;

        this.scriptFile = scriptFile;

    }

    public SqlScriptFile getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(SqlScriptFile scriptFile) {
        this.scriptFile = scriptFile;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Exception getTheException() {
        return theException;
    }

    public void setTheException(Exception theException) {
        this.theException = theException;
    }


}
