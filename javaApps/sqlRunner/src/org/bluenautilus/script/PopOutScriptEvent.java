package org.bluenautilus.script;

/**
 * Created by bstevens on 3/8/14.
 */
public class PopOutScriptEvent {

	private ScriptType type;

	public PopOutScriptEvent(ScriptType type) {
		this.type = type;
	}

	public ScriptType getType() {
		return type;
	}

	public void setType(ScriptType type) {
		this.type = type;
	}




}
