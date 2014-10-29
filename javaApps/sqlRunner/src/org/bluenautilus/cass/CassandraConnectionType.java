package org.bluenautilus.cass;

import org.bluenautilus.util.MiscUtil;


public enum CassandraConnectionType {
	SSH("SSH", true, true),
	PLINK("PLINK", false, false),
        SPLUNK("Splunk",true,true);

	private final String displayString;
    private final boolean supportsWindows;
    private final boolean supportsLinux;
    private final boolean supportsOSX;

    public static final CassandraConnectionType WINDOWS_DEFAULT = CassandraConnectionType.PLINK;
    public static final CassandraConnectionType LINUX_DEFAULT = CassandraConnectionType.SSH;
    public static final CassandraConnectionType OSX_DEFAULT = CassandraConnectionType.SSH;

	CassandraConnectionType(String displayString, boolean windows, boolean linux, boolean osx) {
		this.displayString = displayString;
        this.supportsLinux = linux;
        this.supportsWindows = windows;
        this.supportsOSX = osx;
	}


	public String toString() {
		return this.displayString;
	}

    /*
    * Returns null if enum not found!
    * NULL I SAY
    *
     */
    public static CassandraConnectionType getEnum(String input){
         for(CassandraConnectionType type: CassandraConnectionType.values()){
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

    public boolean supportsOSX() {
        return supportsOSX;
    }

    public static CassandraConnectionType getDefaultForThisOS(){
        if(MiscUtil.isThisWindows()){
            return CassandraConnectionType.WINDOWS_DEFAULT;
        }else{
            return CassandraConnectionType.LINUX_DEFAULT;
        }
    }
}
