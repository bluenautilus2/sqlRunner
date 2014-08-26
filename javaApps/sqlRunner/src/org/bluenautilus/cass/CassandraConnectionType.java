package org.bluenautilus.cass;

import org.bluenautilus.util.MiscUtil;


public enum CassandraConnectionType {
	SSH("SSH", true, true),
	SECURE_SSH("Secure SSH", true, false),
	WINDOWS("Placeholder - TBD", false, false);


	private final String displayString;
    private final boolean supportsWindows;
    private final boolean supportsLinux;

    public static final CassandraConnectionType WINDOWS_DEFAULT = CassandraConnectionType.WINDOWS;
    public static final CassandraConnectionType LINUX_DEFAULT = CassandraConnectionType.SSH;

	CassandraConnectionType(String displayString, boolean windows, boolean linux) {
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

    public static CassandraConnectionType getDefaultForThisOS(){
        if(MiscUtil.isThisWindows()){
            return CassandraConnectionType.WINDOWS_DEFAULT;
        }else{
            return CassandraConnectionType.LINUX_DEFAULT;
        }
    }
}
