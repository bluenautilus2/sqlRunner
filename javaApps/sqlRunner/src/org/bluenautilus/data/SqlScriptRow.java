package org.bluenautilus.data;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 5:26 PM
 */

//represents the string retrieved from the db - is a script that
//has already run

public class SqlScriptRow implements Comparable<SqlScriptRow> {

    private String compareString;


    public SqlScriptRow(String s) {
        compareString = s;
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

    public boolean equals(SqlScriptFile scriptFile) {
        return false;
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

    public String getCompareString() {
        return compareString;
    }
}
