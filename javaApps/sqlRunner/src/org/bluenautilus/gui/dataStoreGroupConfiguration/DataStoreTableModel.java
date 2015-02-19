package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.CassConfigItemsList;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlConfigItemsList;
import org.bluenautilus.data.UuidConfigItem;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 11:53 AM
 */
public class DataStoreTableModel extends AbstractTableModel {
    private final String columnNames[] = {"Type", "Data Stores"};
    private List<UuidConfigItem> dataStoreList = new ArrayList<UuidConfigItem>();

    public static final int IMAGE_HEIGHT_IN_PIXELS = 20;


    public DataStoreTableModel(CassConfigItemsList cassList, SqlConfigItemsList sqlList) {
        this(cassList.getCassConfigItems(), sqlList.getSqlConfigItems());
    }

    public DataStoreTableModel(List<CassConfigItems> cassList, List<SqlConfigItems> sqlList) {
        int i = 0;
        for (CassConfigItems items : cassList) {
            items.setTableRowIndex(i);
            dataStoreList.add(items);
            i++;
        }
        //notice i is not reset
        for (SqlConfigItems items : sqlList) {
            items.setTableRowIndex(i);
            dataStoreList.add(items);
            i++;
        }
    }

    public DataStoreTableModel() {

    }

    @Override
    public String getColumnName(int col) {
        return this.columnNames[col];
    }

    @Override
    public int getRowCount() {
        if (dataStoreList == null) {
            return 0;
        }
        return dataStoreList.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    public boolean isEmpty() {
        if (dataStoreList == null) {
            return true;
        }
        return dataStoreList.isEmpty();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (this.dataStoreList == null || this.isEmpty() || rowIndex >= this.dataStoreList.size()) {
            return null;
        }

        return dataStoreList.get(rowIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public Class getColumnClass(int c) {
         return UuidConfigItem.class;
    }


    public UuidConfigItem getRowObject(int index) {
        if (index < 0 || index >= this.dataStoreList.size()) {
            return null;
        }
        return dataStoreList.get(index);
    }

    public List<UuidConfigItem> getAllRows() {
        return this.dataStoreList;
    }

    public void addUuidItem(UuidConfigItem newItem) {
        if (!this.dataStoreList.contains(newItem)) {
            this.dataStoreList.add(newItem);
            this.fireTableRowsInserted(dataStoreList.size() - 1, dataStoreList.size() - 1);
        }

    }

    public void removeUuidItem(UuidConfigItem oldItem) {
        int i = this.dataStoreList.indexOf(oldItem);
        //may not be in table
        if (i >= 0) {
            this.dataStoreList.remove(oldItem);
            this.fireTableRowsDeleted(i, i);
        }
    }

    public void replaceUuidItem(UuidConfigItem updated) {
        UuidConfigItem foundObj = null;
        for (UuidConfigItem item : this.dataStoreList) {
            if (item.getUniqueId().equals(updated.getUniqueId())) {
                foundObj = item;
            }
        }
        if (foundObj != null) {
            int i = this.dataStoreList.indexOf(foundObj);
            this.dataStoreList.set(i, updated);
            this.fireTableRowsUpdated(i, i);
        }
    }

}
