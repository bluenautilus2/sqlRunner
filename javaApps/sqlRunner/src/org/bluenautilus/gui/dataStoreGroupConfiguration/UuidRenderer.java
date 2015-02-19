package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;
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

        UuidConfigItem item = model.getRowObject(row);

        c.setFont(new Font("Sans Serif", Font.PLAIN, 12));

        if (item instanceof CassConfigItems) {
            JLabel label = new JLabel(CassConfigUtil.cassandraSmall);

            if (isSelected) {
                c.setForeground(new Color(20, 20, 100));
                c.setBackground(new Color(230, 230, 230));
            } else {
                c.setForeground(new Color(20, 20, 100));
                c.setBackground(Color.white);

            }
            JPanel holdBoth = new JPanel(new BorderLayout());
            holdBoth.add(label, BorderLayout.WEST);
            holdBoth.add(c, BorderLayout.CENTER);
            return holdBoth;

        }

        if (item instanceof SqlConfigItems) {
            JLabel label = new JLabel(SqlConfigUtil.sqlserverSmall);

            if (isSelected) {
                c.setForeground(new Color(100, 20, 20));
                c.setBackground(new Color(230, 230, 230));
            } else {
                c.setForeground(new Color(100, 20, 20));
                c.setBackground(Color.WHITE);

            }
            JPanel holdBoth = new JPanel(new BorderLayout());
            holdBoth.add(label, BorderLayout.WEST);
            holdBoth.add(c, BorderLayout.CENTER);
            return holdBoth;
        }

        // setToolTipText(...);
        return c;
    }

}
