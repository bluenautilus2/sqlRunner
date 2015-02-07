package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidItem;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

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
            if(column==0){
                return new JLabel(CassConfigUtil.cassandraSmall);
            }else {
                if (isSelected) {
                    c.setForeground(new Color(20, 20, 100));
                    c.setBackground(new Color(230,230,230));
                } else {
                    c.setForeground(new Color(20, 20, 100));
                    c.setBackground(Color.white);

                }
            }
        }

        if(item instanceof SqlConfigItems){
            if(column==0){
                JLabel label = new JLabel(SqlConfigUtil.sqlserverSmall);
                label.setPreferredSize(new Dimension(30,30));
                return label;
            }

            if (isSelected) {
                c.setForeground(new Color(100,20,20));
                c.setBackground(new Color(230,230,230));
            } else {
                c.setForeground(new Color(100,20,20));
                c.setBackground(Color.WHITE);

            }
        }


        // setToolTipText(...);
        return c;
    }

}
