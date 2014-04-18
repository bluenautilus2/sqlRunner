package org.bluenautilus.db;

import org.bluenautilus.db.methodtype.JdbcScriptRunner;
import org.bluenautilus.db.methodtype.OsqlScriptRunner;
import org.bluenautilus.db.methodtype.SqlCmdScriptRunner;

/**
 * Created by jim on 3/28/14.
 * Enumeration of methods to connect to the database.
 */
public enum DBConnectionType {
	NONE("Select One", null),
	JDBC("JDBC", new JdbcScriptRunner()),
	SQL_CMD("sqlCmd", new SqlCmdScriptRunner()),
	OSQL("OSQL - OOL", new OsqlScriptRunner());

	private final String displayString;
	private final ScriptRunner runner;

	DBConnectionType(String displayString, ScriptRunner runner) {
		this.displayString = displayString;
		this.runner = runner;
	}

	public ScriptRunner getScriptRunner() {
		return runner;
	}

	public String toString() {
		return this.displayString;
	}

    /*
    * Returns null if enum not found!
    * NULL I SAY
     */
    public static DBConnectionType getEnum(String input){
         for(DBConnectionType type: DBConnectionType.values()){
             if(type.toString().equals(input)){
                 return type;
             }
         }

        return null;
    }
}
