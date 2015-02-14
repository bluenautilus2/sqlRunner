package org.bluenautilus.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by bstevens on 1/27/15.
 */
public class SqlConfigItemsList {

    private List<SqlConfigItems> sqlConfigItems = new ArrayList<>();

    public List<SqlConfigItems> getSqlConfigItems() {
        return sqlConfigItems;
    }

    public void setSqlConfigItems(List<SqlConfigItems> sqlConfigItems) {
        this.sqlConfigItems = sqlConfigItems;
    }

    public void addSqlConfigItem(SqlConfigItems items){
        this.sqlConfigItems.add(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlConfigItemsList)) return false;

        SqlConfigItemsList that = (SqlConfigItemsList) o;

        if (sqlConfigItems != null ? !sqlConfigItems.equals(that.sqlConfigItems) : that.sqlConfigItems != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sqlConfigItems != null ? sqlConfigItems.hashCode() : 0;
    }



    public void removeSqlConfigItem(UUID sqlItemsId){
        SqlConfigItems fromList = null;
        for(SqlConfigItems items: sqlConfigItems){
            if(items.getUniqueId().equals(sqlItemsId)){
                fromList = items;
            }
        }
        //if not found, will return null pointer
        sqlConfigItems.remove(fromList);
    }

    public void replace(SqlConfigItems updated){
        if(updated==null){
            return;
        }
        SqlConfigItems toRemove = null;
        for(SqlConfigItems items:sqlConfigItems){
            if(updated.getUniqueId().equals(items.getUniqueId())){
                toRemove = items;
            }
        }
        if(toRemove==null){
            toRemove = updated;
        }
        sqlConfigItems.remove(toRemove);
        sqlConfigItems.add(updated);
    }
}
