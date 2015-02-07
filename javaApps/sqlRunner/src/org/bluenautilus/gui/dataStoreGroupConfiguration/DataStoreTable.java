package org.bluenautilus.gui.dataStoreGroupConfiguration;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/2/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataStoreTable extends JTable {

    DataStoreTableModel mymodel;
    UuidRenderer render;

    public DataStoreTable(DataStoreTableModel mymodel) {
        super(mymodel);
        this.mymodel = mymodel;
        this.render = new UuidRenderer();
        this.setDefaultRenderer(String.class, this.render);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        return this.render;
    }

    public void fixColumns() {
        this.getColumnModel().getColumn(0).setPreferredWidth(15);
    }

    public DataStoreTableModel getDataStoreTableModel(){
        return mymodel;
    }

}



