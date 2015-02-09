package org.bluenautilus.data;

import java.util.UUID;

/**
 * Created by bstevens on 1/25/15.
 */
public abstract class UuidItem {

    UUID uniqueId = null;
    Integer tableRowIndex = 0;

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void generateUniqueId() {
        uniqueId = UUID.randomUUID();
    }

    public abstract String getTableDisplayString();

    public Integer getTableRowIndex() {
        return tableRowIndex;
    }

    public void setTableRowIndex(Integer tableRowIndex) {
        this.tableRowIndex = tableRowIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UuidItem)) return false;

        UuidItem uuidItem = (UuidItem) o;

        if (uniqueId != null ? !uniqueId.equals(uuidItem.uniqueId) : uuidItem.uniqueId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uniqueId != null ? uniqueId.hashCode() : 0;
    }
}
