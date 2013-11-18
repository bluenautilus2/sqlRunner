package org.bluenautilus.data;

import org.bluenautilus.script.ScriptStatus;

import java.io.File;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 11:58 AM
 */
public class SqlScriptFile implements Comparable<SqlScriptFile> {
    private File theFile;
    private ScriptStatus status = ScriptStatus.DEFAULT;
    private String compareString;
    private int tableRowIndex = -1;
    private String resultsString;
    private long crcValue = 0;
    private SqlRowHolder rowHolder = new SqlRowHolder();

    public SqlScriptFile(File theFile) {
        this.theFile = theFile;
        this.compareString = this.extractCompareString(theFile);
    }

    private static String extractCompareString(File f) {
        String filename = f.getName();
        int lastdot = filename.lastIndexOf(".");
        filename = filename.substring(0, lastdot);

        //do more stuff to filename?
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

    public void setTheFile(File theFile) {
        this.theFile = theFile;
    }

    @Override
    public String toString() {
        return this.theFile.getName();
    }

    public long getCrcValue() {
        return crcValue;
    }

    public void setCrcValue(long crcValue) {
        this.crcValue = crcValue;
    }
}
