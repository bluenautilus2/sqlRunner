package org.bluenautilus.data;

import org.bluenautilus.script.ScriptStatus;

import java.io.File;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 11:58 AM
 *
 * This class handles sql and cql scripts.
 */
public class SqlScriptFile implements Comparable<SqlScriptFile> {
    private static final String ROLLBACK_FOLDER = "rollback";
    private static final String ROLLBACK_POSTFIX = "_rollback";
    private File theFile;
    private ScriptStatus status = ScriptStatus.DEFAULT;
    private String compareString;
    private int tableRowIndex = -1;
    private String resultsString;

    private SqlRowHolder rowHolder = new SqlRowHolder();

    public SqlScriptFile(File theFile) {
        this.theFile = theFile;
        this.compareString = this.extractCompareString(theFile);
    }

    private static String extractCompareString(File f) {
        String filename = f.getName();
        int lastdot = filename.lastIndexOf(".");
        filename = filename.substring(0, lastdot);

        return filename;
    }

    public String getResultsString() {
        return resultsString;
    }

    public void setResultsString(String resultsString) {
        this.resultsString = resultsString;
    }

    public int getTableRowIndex() {
        return tableRowIndex;
    }

    public void setTableRowIndex(int tableRowIndex) {
        this.tableRowIndex = tableRowIndex;
    }

    public String getCompareString() {
        return compareString;
    }

    public void addRowObjectToCollection(SqlScriptRow rowObj){
        //System.out.println("Adding: "+ rowObj.getDbUpdateDate());
        this.rowHolder.addScript(rowObj);
    }

    @Override
    public int compareTo(SqlScriptFile o) {
        if (this.compareString == null || "".equals(this.compareString)) {
            return -1;
        }

        if (o.compareString == null || "".equals(o.compareString)) {
            return 1;
        }

        return compareString.compareTo(o.compareString);
    }

    public int compareTo(SqlScriptRow row) {
        if (this.compareString == null || "".equals(this.compareString)) {
            return -1;
        }

        if (row.getCompareString() == null || "".equals(row.getCompareString())) {
            return 1;
        }

        return compareString.compareTo(row.getCompareString());
    }


	public File getRollbackFile() {

		File parentDir = this.theFile.getParentFile();
		String parentStr = parentDir.getAbsolutePath();
        String extension = this.getFileExtension(this.theFile.getName());
		String rollbackStr = parentStr +File.separator+ ROLLBACK_FOLDER + File.separator + this.compareString + ROLLBACK_POSTFIX + extension;
		File rollFile = new File(rollbackStr);
        return rollFile;

	}


    public SqlScriptRow getLastRunRow(){
        return this.rowHolder.getLastCreatedRow();
    }

    public ScriptStatus getStatus() {
        return status;
    }

    public void setStatus(ScriptStatus status) {
        this.status = status;
    }

    public File getTheFile() {
        return theFile;
    }



    @Override
    public String toString() {
        return this.theFile.getName();
    }


    private String getFileExtension(String input){
        int index = input.indexOf('.');
        return input.substring(index);
    }
}
