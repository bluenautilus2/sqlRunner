package org.bluenautilus.db;

/**
 * Created by bstevens on 5/15/16.
 */
public enum DataLanguage {
    DML("Data Manipulation Language", "Change the data we keep in the database", "DML"),
    DDL("Data Definition Language", "Change the structures that hold the data", "DDL");

    private String description;
    private String explanation;
    private String displayName;

    DataLanguage(final String description, final String explanation, final String displayName) {
        this.description = description;
        this.explanation = explanation;
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getDisplayName() {
        return displayName;
    }
}
