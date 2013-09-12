package org.bluenautilus.gui;

import org.bluenautilus.script.ScriptStatus;
import org.bluenautilus.data.SqlScriptFile;

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
public class ScriptRenderer extends DefaultTableCellRenderer {

    public ScriptRenderer() {
    }

    public Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        Component c = super.getTableCellRendererComponent(table, color, isSelected, hasFocus, row, column);

        SqlTableModel model = (SqlTableModel) table.getModel();

        SqlScriptFile f = model.getSqlScriptFile(row);
        ScriptStatus status = ScriptStatus.TOO_OLD;

        if (f != null) {
            status = f.getStatus();
        }
        c.setFont(new Font("Sans Serif", Font.PLAIN, 12));

        if (isSelected) {
            c.setForeground(new Color(40, 40, 40));
            c.setBackground(new Color(221, 160, 221));
        } else {
            c.setForeground(status.getTextColor());
            c.setBackground(status.getBgColor());

        }


        // setToolTipText(...);
        return c;
    }

}
