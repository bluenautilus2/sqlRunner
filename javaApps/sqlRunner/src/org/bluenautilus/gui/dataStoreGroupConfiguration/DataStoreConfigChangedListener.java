package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;

/**
 * Created by bstevens on 1/28/15.
 */
public interface DataStoreConfigChangedListener {

    public void newSqlConfig(SqlConfigItems newSql);

    public void newCassConfig(CassConfigItems newCass);

    public void updatedSqlConfig(SqlConfigItems updatedSql);

    public void updatedCassConfig(CassConfigItems updatedCass);

    public void deletedDataStore(UuidConfigItem deletedItem);
}
