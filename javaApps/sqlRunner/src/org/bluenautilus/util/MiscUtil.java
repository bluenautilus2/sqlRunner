package org.bluenautilus.util;

import org.apache.commons.lang.SystemUtils;
import org.bluenautilus.db.DBConnectionType;

/**
 * Created by bstevens on 5/18/14.
 */
public class MiscUtil {

    public static boolean isThisWindows(){
        return (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_WINDOWS_7);
    }

    public static boolean isThisLinux(){
        return !isThisWindows();
    }

    public static DBConnectionType getDefaultConnectionForThisOS(){
        if (isThisWindows()){
            return DBConnectionType.WINDOWS_DEFAULT;
        }
        return DBConnectionType.LINUX_DEFAULT;
    }
}
