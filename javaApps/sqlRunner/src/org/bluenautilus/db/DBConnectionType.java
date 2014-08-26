package org.bluenautilus.db;

import org.bluenautilus.util.MiscUtil;

/**
 * Created by jim on 3/28/14.
 * Enumeration of methods to connect to the database.
 */
public enum DBConnectionType {
	JDBC("JDBC", true, true),
	SQL_CMD("sqlCmd", true, false),
	TSQL("tSQL", false, false);


	private final String displayString;
    private final boolean supportsWindows;
    private final boolean supportsLinux;

    public static final DBConnectionType WINDOWS_DEFAULT = DBConnectionType.SQL_CMD;
    public static final DBConnectionType LINUX_DEFAULT = DBConnectionType.JDBC;

	DBConnectionType(String displayString, boolean windows, boolean linux) {
		this.displayString = displayString;
        this.supportsLinux = linux;
        this.supportsWindows = windows;
	}


	public String toString() {
		return this.displayString;
	}

    /*
    * Returns null if enum not found!
    * NULL I SAY
    *
     */
    public static DBConnectionType getEnum(String input){
         for(DBConnectionType type: DBConnectionType.values()){
             if(type.toString().equals(input)){
                 return type;
             }
         }

        return null;
    }

    public boolean supportsWindows() {
        return supportsWindows;
    }

    public boolean supportsLinux() {
        return supportsLinux;
    }

    public static DBConnectionType getDefaultForThisOS(){
        if(MiscUtil.isThisWindows()){
            return DBConnectionType.WINDOWS_DEFAULT;
        }else{
            return DBConnectionType.LINUX_DEFAULT;
        }
    }
}
