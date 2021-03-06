package org.bluenautilus;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.CassConfigItemsList;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.DataStoreGroupList;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlConfigItemsList;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import java.util.UUID;

/**
 * Created by bstevens on 1/27/15.
 */
public class TestJsonMarshalling {

    public static void main(String args[]) {

        DataStoreGroupList list = new DataStoreGroupList();

        for (int i = 0; i < 14; i++) {
            DataStoreGroup group = new DataStoreGroup();
            group.setIgnoreErrors(true);
            group.setIgnoreNoRun(false);
            group.setNickname("configuration "+ i);
            group.generateUniqueId();
            for (int j = 0; j < 12; j++) {
                group.addDataStoreUUID(UUID.randomUUID());
            }
            list.addGroup(group);
        }


        DataStoreGroupConfigUtil.saveOffCurrent(list);
        DataStoreGroupConfigUtil.setDataStoreGroupList(null);
        DataStoreGroupList newList = DataStoreGroupConfigUtil.readInConfiguration();

        System.out.println("are they equal? " + newList.equals(list));


        SqlConfigItemsList sqlList = new SqlConfigItemsList();

        for (int i = 0; i < 10; i++) {
            SqlConfigItems items = new SqlConfigItems();
            items.setDbConnectionType(DBConnectionType.SQL_CMD.toString());
            items.setDbNameField("panswersTest" + i);
            items.setIpAddressField("10.34.23." + i);
            items.setLoginField("common");
            items.setPasswordField("password!");
            items.setPort("1433");
            items.setScriptFolderField("C:\\home\\scripts" + i + "\\folders");
            items.generateUniqueId();
            sqlList.addSqlConfigItem(items);
        }

        SqlConfigUtil.saveOffCurrent(sqlList);
        SqlConfigUtil.setSqlConfigItemsList(null);
        SqlConfigItemsList sqlListNew = SqlConfigUtil.readInConfiguration();

        System.out.println("are they equal? " + sqlListNew.equals(sqlList));

        CassConfigItemsList cassList = new CassConfigItemsList();

        for (int i = 0; i < 11; i++) {
            CassConfigItems cassItems = new CassConfigItems();
            cassItems.setScriptFolderField("/home/linux/stuff/QSLEWdsdf" + i);
            cassItems.setHostField("nucleus" + i);
            cassItems.setKeyspace("keyspace");
            cassItems.setPort("1234");
            cassItems.generateUniqueId();
            cassList.addCassConfigItem(cassItems);
        }

        CassConfigUtil.saveOffCurrent(cassList);
        CassConfigUtil.setCassConfigItemsList(null);
        CassConfigItemsList newCassList = CassConfigUtil.readInConfiguration();

        System.out.println("are they equal? " + newCassList.equals(cassList));

    }

}
