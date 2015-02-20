package org.bluenautilus.data;

import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by bstevens on 1/25/15.
 */
public class DataStoreGroup extends UuidConfigItem {
    private List<UUID> dataStores = new ArrayList<>();

    private String nickname = null;

    private boolean ignoreErrors = false;
    private boolean ignoreNoRun = false;

    public List<UUID> getDataStores() {
        return dataStores;
    }

    public void setDataStores(List<UUID> dataStores) {
        this.dataStores = dataStores;
    }

    @JsonIgnore
    public void setDataStoreItems(List<UuidConfigItem> dataStores) {
        List<UUID> plainIds = new ArrayList<>();
        for (UuidConfigItem item : dataStores) {
            plainIds.add(item.getUniqueId());
        }
        this.setDataStores(plainIds);

    }


    public void addDataStoreUUID(UUID dataStore) {
        this.dataStores.add(dataStore);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isIgnoreErrors() {
        return ignoreErrors;
    }

    public void setIgnoreErrors(boolean ignoreErrors) {
        this.ignoreErrors = ignoreErrors;
    }

    public boolean isIgnoreNoRun() {
        return ignoreNoRun;
    }

    public void setIgnoreNoRun(boolean ignoreNoRun) {
        this.ignoreNoRun = ignoreNoRun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataStoreGroup)) return false;

        DataStoreGroup that = (DataStoreGroup) o;

        if (this.getUniqueId().equals(that.getUniqueId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = uniqueId != null ? uniqueId.hashCode() : 0;
        result = 31 * result + (dataStores != null ? dataStores.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (ignoreErrors ? 1 : 0);
        result = 31 * result + (ignoreNoRun ? 1 : 0);
        return result;
    }

    public DataStoreGroup() {
       //remember this is json pojo
    }

    @Override
    public String toString() {
        return this.nickname;
    }

    @Override
    @JsonIgnore
    public String getTableDisplayString() {
        return nickname;
    }

    @Override
    @JsonIgnore
    public DataStoreGroup clone() {
        DataStoreGroup cloned = new DataStoreGroup();
        cloned.setNickname(this.getNickname());
        cloned.generateUniqueId();
        List<UUID> newList = new ArrayList<>();
        for (UUID id : this.getDataStores()) {
            newList.add(id);
        }
        cloned.setDataStores(newList);
        return cloned;
    }

    @JsonIgnore
    public List<UuidConfigItem> retrieveFullObjectsFromFiles(){
        SqlConfigItemsList sqlList = SqlConfigUtil.getSqlConfigItemsList();
        CassConfigItemsList cassConfigItemsList = CassConfigUtil.getCassConfigItemsList();
        HashMap<UUID,SqlConfigItems> sqlMap = sqlList.getUuidHash();
        HashMap<UUID,CassConfigItems> cassMap = cassConfigItemsList.getUuidHash();

        List<UuidConfigItem> objectList = new ArrayList<>();
        for(UUID id:this.getDataStores()){
            SqlConfigItems s = sqlMap.get(id);
            CassConfigItems c = cassMap.get(id);
            if(s!=null){
                objectList.add(s);
            }
            if(c !=null){
                objectList.add(c);
            }
        }
        return objectList;
    }

}
