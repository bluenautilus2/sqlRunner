package org.bluenautilus.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/2/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptTable extends JTable {

    SqlTableModel mymodel;
    ScriptRenderer render;

    public ScriptTable(SqlTableModel mymodel) {
        super(mymodel);
        this.mymodel = mymodel;
        this.render = new ScriptRenderer();
        this.setDefaultRenderer(String.class, this.render);

    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        return this.render;
    }

}
