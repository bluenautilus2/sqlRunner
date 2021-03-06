package org.bluenautilus.data;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by bstevens on 1/25/15.
 */
public abstract class UuidConfigItem implements Serializable{

    private static final long serialVersionUID = -6023926035731356418L;

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

    public abstract UuidConfigItem clone();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UuidConfigItem)) return false;

        UuidConfigItem uuidItem = (UuidConfigItem) o;

        if (uniqueId != null ? !uniqueId.equals(uuidItem.uniqueId) : uuidItem.uniqueId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uniqueId != null ? uniqueId.hashCode() : 0;
    }

    @Override
    public String toString(){
        return getTableDisplayString();
    }
}
