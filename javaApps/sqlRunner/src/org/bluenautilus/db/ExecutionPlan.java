package org.bluenautilus.db;

/**
 * Created by bstevens on 5/15/16.
 */
public enum ExecutionPlan {
    PRE_DEPLOYMENT("PRE-DEPLOYMENT"),
    POST_DEPLOYMENT("POST-DEPLOYMENT"),
    OUTAGE("OUTAGE");

    private String displayName;

    ExecutionPlan(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
