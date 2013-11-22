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
	private ScriptType type;
	private boolean dbProblem = false;


	public ScriptResultsEvent(String output, SqlScriptFile scriptFile, ScriptType type, boolean dbProblem) {
		this.output = output;
		this.type = type;
		this.scriptFile = scriptFile;
		this.dbProblem = dbProblem;
	}


	public ScriptResultsEvent(String output, SqlScriptFile scriptFile, ScriptType type, boolean dbProblem, Exception e) {
		this.output = output;
		this.theException = e;
		this.type = type;
		this.scriptFile = scriptFile;
		this.dbProblem = dbProblem;

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


	public ScriptType getType() {
		return type;
	}


	public void setType(ScriptType type) {
		this.type = type;
	}


	public boolean isDbProblem() {
		return dbProblem;
	}


	public void setDbProblem(boolean dbProblem) {
		this.dbProblem = dbProblem;
	}
}
