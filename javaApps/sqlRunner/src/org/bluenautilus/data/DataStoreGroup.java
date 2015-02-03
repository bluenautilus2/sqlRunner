package org.bluenautilus.data;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bstevens on 1/25/15.
 */
public class DataStoreGroup extends UuidItem {



    private ArrayList<UUID> dataStores = new ArrayList<>();

    private String nickname = null;

    private boolean ignoreErrors = false;
    private boolean ignoreNoRun = false;

    public ArrayList<UUID> getDataStores() {
        return dataStores;
    }

    public void setDataStores(ArrayList<UUID> dataStores) {
        this.dataStores = dataStores;
    }

    public void addDataStoreUUID(UUID dataStore){
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

        if (ignoreErrors != that.ignoreErrors) return false;
        if (ignoreNoRun != that.ignoreNoRun) return false;
        if (dataStores != null ? !dataStores.equals(that.dataStores) : that.dataStores != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (uniqueId != null ? !uniqueId.equals(that.uniqueId) : that.uniqueId != null) return false;

        return true;
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

    }

    @Override
    public String toString(){
        return this.nickname;
    }

    @Override
    @JsonIgnore
    public String getTableDisplayString() {
        return nickname;
    }
}
