package org.bluenautilus.cass;

import java.util.regex.Pattern;

/**
 * Created by bstevens on 12/4/15.
 */
public enum CassTarget {

    REGIONAL(Pattern.compile("regional")),
    GLOBAL(Pattern.compile("global")),
    NONE(Pattern.compile(".*"));

    private Pattern headerTag;
    protected static Pattern targetPattern = Pattern.compile("target");

    CassTarget(Pattern p) {
        headerTag = p;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public Pattern getHeaderTag() {
        return headerTag;
    }



}
