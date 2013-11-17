package org.bluenautilus.db;

import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.ScriptStatus;

import java.io.File;
import java.util.ArrayList;

/**
 * User: bluenautilus2
 * Date: 7/27/13
 * Time: 7:26 PM
 */
public class SqlScriptMgr {

    //File is directory that contains the sql scripts
    private File folder = null;
    private ArrayList<File> sqlFiles = null;


    public SqlScriptMgr(File parentFolder) throws Exception {
        this.folder = parentFolder;
        checkFile(folder);
        this.sqlFiles = getListofFiles(folder);
    }

    private static void checkFile(final File folder) throws Exception {
        if (!folder.exists()) {
            throw new Exception("File not found: " + folder.getAbsolutePath());
        }
        if (!folder.isDirectory()) {
            throw new Exception("This is not a directory!: " + folder.getAbsolutePath());
        }

        File[] listOfFiles = folder.listFiles();
        if (listOfFiles.length <= 0) {
            throw new Exception("This folder is empty: " + folder.getAbsolutePath());
        }
    }

    private static ArrayList<File> getListofFiles(final File folder) {
        File[] listOfFiles = folder.listFiles();

        ArrayList<File> sqlFiles = new ArrayList<File>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String files = listOfFiles[i].getName();
                if (files.endsWith(".sql")) {
                    sqlFiles.add(listOfFiles[i]);
                }
            }
        }
        return sqlFiles;
    }

    public ArrayList<SqlScriptFile> getSqlList() {
        ArrayList<SqlScriptFile> answer = new ArrayList<SqlScriptFile>();

        for (File file : this.sqlFiles) {
            SqlScriptFile newFile = new SqlScriptFile(file);

            if (!file.exists()) {
                newFile.setStatus(ScriptStatus.FILE_ERROR);
            }
            if (!file.canRead()) {
                newFile.setStatus(ScriptStatus.FILE_ERROR);
            }

            answer.add(newFile);
        }
        return answer;
    }


}

