package org.bluenautilus.db;

import java.util.regex.Pattern;

/**
 * Created by bstevens on 8/6/15.
 */
public enum SqlTarget {
    COMMON(Pattern.compile("common")),
    POD_MAIN(Pattern.compile("pod_main")),
    POD_LOGGING(Pattern.compile("pod_logging")),
    NONE(Pattern.compile(".*"));

    private Pattern headerTag;
    protected static Pattern targetPattern = Pattern.compile("target");

    SqlTarget(Pattern p) {
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
