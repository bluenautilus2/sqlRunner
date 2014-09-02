package org.bluenautilus.util;

import org.apache.commons.lang.SystemUtils;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.script.ScriptStatus;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;

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

    public static ArrayList<SqlScriptFile> combine(ArrayList<SqlScriptRow> rows, ArrayList<SqlScriptFile> files) {
        ArrayList<SqlScriptFile> answer = new ArrayList<SqlScriptFile>();

        Collections.sort(rows);
        Collections.sort(files);

        if(null==rows || null==files || rows.isEmpty() || files.isEmpty()) {
            return answer;
        }
        SqlScriptRow firstRow = rows.get(0);

        boolean done = false;
        boolean beginningWasFound = false;

        int i = 0;
        while (!done) {
            SqlScriptFile currentFile = files.get(i);

            if (firstRow.compareTo(currentFile) > 0) {
                //this file is older, keep going
            }
            if (firstRow.compareTo(currentFile) <= 0) {
                //we found a file that is at least as new as the oldest db script
                beginningWasFound = true;
                done = true;
            }
            if (files.size() == i + 1) {
                done = true;
            } else if (!done) {
                i++;
            }
        }

        if (!beginningWasFound) {
            return files;
            //@todo throw a warning
        }

        for (int k = i; k < files.size(); k++) {
            SqlScriptFile currentFile = files.get(k);


            for (SqlScriptRow row : rows) {
                if (currentFile.compareTo(row) == 0) {
                    currentFile.addRowObjectToCollection(row);
                }
            }

SqlScriptRow lastRow = currentFile.getLastRunRow();
if(lastRow == null){
currentFile.setStatus(ScriptStatus.NEED_TO_RUN);
}else if(lastRow.isRollback()){
currentFile.setStatus(ScriptStatus.ROLLED_BACK);
}else{
currentFile.setStatus(ScriptStatus.ALREADY_RUN);
}

        }
        Collections.reverse(files);
        return files;
    }
}
