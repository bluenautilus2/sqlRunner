package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.CassConfigItemsList;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.DataStoreGroupList;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlConfigItemsList;
import org.bluenautilus.data.UuidItem;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 11:53 AM
 */
public class DataStoreTableModel extends AbstractTableModel {
    private final String columnNames[] = {"Data Stores"};
    private List<UuidItem> dataStoreList = new ArrayList<UuidItem>();

    public DataStoreTableModel(DataStoreGroupList newList) {
        int i=0;
        for(DataStoreGroup group: newList.getDataStoreGroupList()){
            group.setTableRowIndex(i);
            dataStoreList.add(group);
            i++;
        }
    }

    public DataStoreTableModel(CassConfigItemsList cassList,SqlConfigItemsList sqlList) {
      this(cassList.getCassConfigItems(),sqlList.getSqlConfigItems());
    }

    public DataStoreTableModel(List<CassConfigItems> cassList,List<SqlConfigItems> sqlList) {
        int i=0;
        for(CassConfigItems items: cassList){
            items.setTableRowIndex(i);
            dataStoreList.add(items);
            i++;
        }
        //notice i is not reset
        for(SqlConfigItems items: sqlList){
            items.setTableRowIndex(i);
            dataStoreList.add(items);
            i++;
        }
    }

    public DataStoreTableModel() {

    }

    @Override
    public String getColumnName(int col) {
        return this.columnNames[0];
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

       return this.dataStoreList.get(rowIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public Class getColumnClass(int c) {
       return UuidItem.class;
    }

    public UuidItem getRowObject(int index) {
        if (index < 0 || index >= this.dataStoreList.size()) {
            return null;
        }
        return this.dataStoreList.get(index);
    }

    public List<UuidItem> getAllRows(){
        return this.dataStoreList;
    }

}
