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
    private int row = -1;
    private String resultsString;
    private long crcValue = 0;

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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getCompareString() {
        return compareString;
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
