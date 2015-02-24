package org.bluenautilus.data;

import org.joda.time.DateTime;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 5:26 PM
 */

//represents the string retrieved from the db - is a script that
//has already run

public class SqlScriptRow implements Comparable<SqlScriptRow> {

    //Column length chops the string down.
    private static final String ROLLBACK_TAG = "_rollba";
    private String compareString;
    private DateTime rowCreated;



    private String dbUpdateDate;

    public SqlScriptRow(String s, DateTime created) {
        dbUpdateDate = s;
        rowCreated = created;
        compareString = this.removeRollback(s);
    }

    public int compareTo(SqlScriptFile file) {
        if (this.compareString == null || "".equals(this.compareString)) {
            return -1;
        }

        if (file.getCompareString() == null || "".equals(file.getCompareString())) {
            return 1;
        }

        return compareString.compareTo(file.getCompareString());
    }

    @Override
    public int compareTo(SqlScriptRow o) {
        if (this.compareString == null || "".equals(this.compareString)) {
            return -1;
        }

        if (o.compareString == null || "".equals(o.compareString)) {
            return 1;
        }

        return compareString.compareTo(o.compareString);
    }



    public boolean isRollback() {
        if (this.dbUpdateDate != null) {
            return (dbUpdateDate.indexOf(ROLLBACK_TAG) != -1);
        }
        return false;
    }

    private static String removeRollback(String compareString) {
        String lowerCase = compareString.toLowerCase();
        int index = lowerCase.indexOf(ROLLBACK_TAG);

        //if it's not at the end there is a problem
        if (index > 4) {
            return lowerCase.substring(0, index);
        }
        return compareString;
    }

    public DateTime getRowCreated() {
        return rowCreated;
    }

    public String getCompareString() {
        return compareString;
    }

    public String getDbUpdateDate() {
        return dbUpdateDate;
    }

}
