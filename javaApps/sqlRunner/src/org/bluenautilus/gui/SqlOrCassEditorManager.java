package org.bluenautilus.gui;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidItem;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStoreConfigChangedListener;

/**
 * Created by bstevens on 2/6/15.
 */
public class SqlOrCassEditorManager implements DataStoreConfigChangedListener, PrettyButtonListener {



    public SqlOrCassEditorManager(DataStoreConfigChangedListener parentListener) {

    }


    @Override
    public void newSqlConfig(SqlConfigItems newSql) {


    }

    @Override
    public void newCassConfig(CassConfigItems newCass) {


    }

    @Override
    public void updatedSqlConfig(SqlConfigItems updatedSql) {

    }

    @Override
    public void updatedCassConfig(CassConfigItems updatedCass) {

    }

    @Override
    public void deletedDataStore(UuidItem deletedItem) {

    }

    @Override
    public void prettyButtonClicked(ButtonType type) {

    }
}
