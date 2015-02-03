package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidItem;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/2/13
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class UuidRenderer extends DefaultTableCellRenderer {

    public UuidRenderer() {
    }

    public Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        Component c = super.getTableCellRendererComponent(table, color, isSelected, hasFocus, row, column);

        DataStoreTableModel model = (DataStoreTableModel) table.getModel();

        UuidItem item = model.getRowObject(row);

        c.setFont(new Font("Sans Serif", Font.PLAIN, 12));

        if(item instanceof CassConfigItems){
            if (isSelected) {
                c.setForeground(Color.BLACK);
                c.setBackground(new Color(250,100,80));
            } else {
                c.setForeground(Color.BLACK);
                c.setBackground(new Color(230,70,50));

            }
        }

        if(item instanceof SqlConfigItems){
            if (isSelected) {

                c.setForeground(Color.BLACK);
                c.setBackground(new Color(250,250,100));
            } else {
                c.setForeground(Color.BLACK);
                c.setBackground(new Color(200,200,50));

            }
        }


        // setToolTipText(...);
        return c;
    }

}
