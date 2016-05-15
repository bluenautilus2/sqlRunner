package org.bluenautilus.data;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by bstevens on 1/26/15.
 */
public class DataStoreGroupList {

    private List<DataStoreGroup> dataStoreGroupList = new ArrayList<>();
    private UUID lastUsedDataStoreGroupId;
    private String lastUsedFileFolderSql;
    private String lastUsedFileFolderCass;

    @Deprecated
    private boolean poddedMode;

    private String authorName;
    private String authorEmail;

    public DataStoreGroupList() {
        //nothin'
    }

    public void removeDataStoreGroup(UUID goneGroup) {
        DataStoreGroup fromList = null;
        for (DataStoreGroup group : dataStoreGroupList) {
            if (group.getUniqueId().equals(goneGroup)) {
                fromList = group;
            }
        }
        //if not found, will return null pointer
        dataStoreGroupList.remove(fromList);
    }

    public List<DataStoreGroup> getDataStoreGroupList() {
        return dataStoreGroupList;
    }

    public void setDataStoreGroupList(List<DataStoreGroup> dataStoreGroupList) {
        this.dataStoreGroupList = dataStoreGroupList;
    }

    @JsonIgnore
    public void addGroup(DataStoreGroup group) {
        this.dataStoreGroupList.add(group);
    }

    @JsonIgnore
    public String[] getComboBoxList() {
        String[] stringArray = new String[dataStoreGroupList.size()];
        for (int i = 0; i < dataStoreGroupList.size(); i++) {
            stringArray[i] = dataStoreGroupList.get(i).getNickname();
        }
        return stringArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataStoreGroupList)) return false;

        DataStoreGroupList that = (DataStoreGroupList) o;

        if (dataStoreGroupList != null ? !dataStoreGroupList.equals(that.dataStoreGroupList) : that.dataStoreGroupList != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return dataStoreGroupList != null ? dataStoreGroupList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DataStoreGroupList{" +
                "dataStoreGroupList=" + dataStoreGroupList +
                '}';
    }

    @JsonIgnore
    public void replace(DataStoreGroup updated) {
        DataStoreGroup groupToRemove = null;
        for (DataStoreGroup group : dataStoreGroupList) {
            if (updated.getUniqueId().equals(group.getUniqueId())) {
                groupToRemove = group;
            }
        }
        if (groupToRemove == null) {
            groupToRemove = updated;
        }
        dataStoreGroupList.remove(groupToRemove);
        this.dataStoreGroupList.add(updated);
    }

    public UUID getLastUsedDataStoreGroupId() {
        return lastUsedDataStoreGroupId;
    }

    public void setLastUsedDataStoreGroupId(UUID lastUsedDataStoreGroupId) {
        this.lastUsedDataStoreGroupId = lastUsedDataStoreGroupId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    @Deprecated
    public boolean isPoddedMode() {
        return poddedMode;
    }

    @Deprecated
    public void setPoddedMode(boolean poddedMode) {
        this.poddedMode = poddedMode;
    }

    public String getLastUsedFileFolderSql() {
        return lastUsedFileFolderSql;
    }

    public void setLastUsedFileFolderSql(String lastUsedFileFolderSql) {
        this.lastUsedFileFolderSql = lastUsedFileFolderSql;
    }

    public String getLastUsedFileFolderCass() {
        return lastUsedFileFolderCass;
    }

    public void setLastUsedFileFolderCass(String lastUsedFileFolderCass) {
        this.lastUsedFileFolderCass = lastUsedFileFolderCass;
    }
}
