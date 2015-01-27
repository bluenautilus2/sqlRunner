package org.bluenautilus.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bstevens on 1/26/15.
 */
public class DataStoreGroupList {

    private List<DataStoreGroup> dataStoreGroupList = new ArrayList<>();

    public DataStoreGroupList(){
        //nothin'
    }

    public List<DataStoreGroup> getDataStoreGroupList() {
        return dataStoreGroupList;
    }

    public void setDataStoreGroupList(List<DataStoreGroup> dataStoreGroupList) {
        this.dataStoreGroupList = dataStoreGroupList;
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
}
