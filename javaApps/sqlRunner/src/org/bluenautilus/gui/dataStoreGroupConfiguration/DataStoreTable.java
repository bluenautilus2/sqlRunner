package org.bluenautilus.gui.dataStoreGroupConfiguration;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import java.util.UUID;

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

        this.setTableHeader(null);
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        return this.render;
    }

    public DataStoreTableModel getDataStoreTableModel(){
        return mymodel;
    }

}



