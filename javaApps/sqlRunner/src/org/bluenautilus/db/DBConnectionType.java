package org.bluenautilus.db;

import org.bluenautilus.util.MiscUtil;

/**
 * Created by jim on 3/28/14.
 * Enumeration of methods to connect to the database.
 */
public enum DBConnectionType {
    JDBC("JDBC", true, true,true),
    SQL_CMD("sqlCmd", true, false,false),
    TSQL("tSQL", false, false,false);


    private final String displayString;
    private final boolean supportsWindows;
    private final boolean supportsLinux;
    private final boolean supportsMacOS;

    public static final DBConnectionType WINDOWS_DEFAULT = DBConnectionType.SQL_CMD;
    public static final DBConnectionType LINUX_DEFAULT = DBConnectionType.JDBC;

    DBConnectionType(String displayString, boolean windows, boolean linux, boolean macOS) {
        this.displayString = displayString;
        this.supportsLinux = linux;
        this.supportsWindows = windows;
        this.supportsMacOS = macOS;
    }


    public String toString() {
        return this.displayString;
    }

    /*
    * Returns null if enum not found!
    * NULL I SAY
    *
     */
    public static DBConnectionType getEnum(String input) {
        for (DBConnectionType type : DBConnectionType.values()) {
            if (type.toString().equals(input)) {
                return type;
            }
        }

        return null;
    }

    public boolean supportsMacOS() {
        return supportsMacOS;
    }

    public boolean supportsWindows() {
        return supportsWindows;
    }

    public boolean supportsLinux() {
        return supportsLinux;
    }

    public static DBConnectionType getDefaultForThisOS() {
        if (MiscUtil.isThisWindows()) {
            return DBConnectionType.WINDOWS_DEFAULT;
        } else {
            return DBConnectionType.LINUX_DEFAULT;
        }
    }

    public boolean worksInThisOS() {
        if (MiscUtil.isThisWindows()) {
            return supportsWindows();
        }
        if (MiscUtil.isThisLinux()) {
            return supportsLinux();
        }

        if (MiscUtil.isThisMacOS()) {
            return supportsMacOS();
        }
        return false;
    }
}
