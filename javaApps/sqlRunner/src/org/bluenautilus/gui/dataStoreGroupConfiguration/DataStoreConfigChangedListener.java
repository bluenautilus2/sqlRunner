package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.DataStoreGroupList;

import java.util.List;

/**
 * Created by bstevens on 1/28/15.
 */
public interface DataStoreConfigChangedListener {

    public void dataStoreConfigChanged(List<DataStoreGroupList> newOrChangedItems);
}
