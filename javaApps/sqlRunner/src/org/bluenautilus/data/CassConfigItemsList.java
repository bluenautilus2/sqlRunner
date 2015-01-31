package org.bluenautilus.data;

import java.util.ArrayList;
import java.util.List;

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

    public void addCassConfigItems(CassConfigItems items){
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
}
