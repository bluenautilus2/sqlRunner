package org.bluenautilus.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private ArrayList<File> sqlFiles = null;
    private static Log log = LogFactory.getLog(SqlScriptMgr.class);

    public SqlScriptMgr(File parentFolder) throws Exception {
        checkFile(parentFolder);
        this.sqlFiles = getListofFiles(parentFolder);
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

    private static ArrayList<File> getListofFiles(final File folder) {
        File[] listOfFiles = folder.listFiles();

        ArrayList<File> sqlFiles = new ArrayList<>();

		if (listOfFiles != null) {
			for (File listOfFile : listOfFiles) {
				if (listOfFile.isFile()) {
					String files = listOfFile.getName();
					if (files.endsWith(".sql")||(files.endsWith("cql"))) {
						sqlFiles.add(listOfFile);
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

            answer.add(newFile);
        }
        return answer;
    }


}

