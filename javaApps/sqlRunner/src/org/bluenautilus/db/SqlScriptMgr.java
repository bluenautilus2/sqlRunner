package org.bluenautilus.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.ScriptContentFilter;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.ScriptStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * User: bluenautilus2
 * Date: 7/27/13
 * Time: 7:26 PM
 */
public class SqlScriptMgr {

    private ArrayList<File> sqlFiles = null;
    private static Log log = LogFactory.getLog(SqlScriptMgr.class);
    private DataStoreType dataStoreType = DataStoreType.SQL;
    ScriptContentFilter filter;

    public SqlScriptMgr(File parentFolder, DataStoreType dataStoreType, ScriptContentFilter filter) throws Exception {
        checkFile(parentFolder);
        this.sqlFiles = getListOfFiles(parentFolder, filter);
        this.dataStoreType = dataStoreType;
        this.filter = filter;
    }

    private static void checkFile(final File folder) throws Exception {

        if (!folder.exists()) {
            throw new Exception("File not found: " + folder.getAbsolutePath());
        }
        if (!folder.isDirectory()) {
            throw new Exception("This is not a directory!: " + folder.getAbsolutePath());
        }

        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null && listOfFiles.length <= 0) {
            throw new Exception("This folder is empty: " + folder.getAbsolutePath());
        }
    }

    private ArrayList<File> getListOfFiles(final File folder, ScriptContentFilter filter) throws IOException {
        File[] listOfFiles = folder.listFiles();

        ArrayList<File> sqlFiles = new ArrayList<>();

        if (listOfFiles != null) {
            for (File singleFile : listOfFiles) {
                if (singleFile.isFile()) {
                    String fileName = singleFile.getName();
                    if (fileName.endsWith(this.dataStoreType.extension)) {
                        if (filter == null || filter.filterScript(singleFile)) {
                            sqlFiles.add(singleFile);
                        }
                    }
                }
            }
        }
        return sqlFiles;
    }

    public ArrayList<SqlScriptFile> getSqlList() {
        ArrayList<SqlScriptFile> answer = new ArrayList<>();

        for (File file : this.sqlFiles) {
            SqlScriptFile newFile = new SqlScriptFile(file);

            if (!file.exists()) {
                newFile.setStatus(ScriptStatus.FILE_ERROR);
            }
            if (!file.canRead()) {
                newFile.setStatus(ScriptStatus.FILE_ERROR);
            }

            //do filtering here.

            answer.add(newFile);
        }
        return answer;
    }

    public enum DataStoreType {
        CASS(".cql"), SQL(".sql");

        public String extension;

        DataStoreType(String extension) {
            this.extension = extension;
        }
    }


}

