package org.bluenautilus.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by bstevens on 1/27/15.
 */
public class CassConfigItemsList {

    List<CassConfigItems> cassConfigItems = new ArrayList<>();

    public List<CassConfigItems> getCassConfigItems() {
        return cassConfigItems;
    }

    public void setCassConfigItems(List<CassConfigItems> cassConfigItems) {
        this.cassConfigItems = cassConfigItems;
    }

    public void addCassConfigItem(CassConfigItems items){
        this.cassConfigItems.add(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CassConfigItemsList)) return false;

        CassConfigItemsList that = (CassConfigItemsList) o;

        if (cassConfigItems != null ? !cassConfigItems.equals(that.cassConfigItems) : that.cassConfigItems != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cassConfigItems != null ? cassConfigItems.hashCode() : 0;
    }

    public void removeCassConfigItem(UUID cassItemsId){
        CassConfigItems fromList = null;
        for(CassConfigItems items: cassConfigItems){
            if(items.getUniqueId().equals(cassItemsId)){
                fromList = items;
            }
        }
        //if not found, will return null pointer
        cassConfigItems.remove(fromList);
    }

    public void replace(CassConfigItems updated){
        if(updated==null){
            return;
        }
        CassConfigItems toRemove = null;
        for(CassConfigItems items:cassConfigItems){
            if(updated.getUniqueId().equals(items.getUniqueId())){
                toRemove = items;
            }
        }
        if(toRemove==null){
            toRemove = updated;
        }
        cassConfigItems.remove(toRemove);
        cassConfigItems.add(updated);
    }
}
