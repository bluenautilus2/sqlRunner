package org.bluenautilus.script;

/**
 * Created by bstevens on 12/29/13.
 */

public enum CommandFlag {

	//ALL LOWER CASE
	//The line compared to this flag will be automatically set to lower case.

	NORUN("no_run"),
	NOCMD("no_command"),;
	private String flag;

	public static final String SQLRUNNER_CMD = "sqlrunner";

	private CommandFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}

