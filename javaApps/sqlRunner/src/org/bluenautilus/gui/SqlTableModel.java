package org.bluenautilus.gui;

import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.ScriptStatus;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 11:53 AM
 */
public class SqlTableModel extends AbstractTableModel {
    private final String columnNames[] = {"Script Name", "Status"};
    private ArrayList<SqlScriptFile> fileList = new ArrayList<SqlScriptFile>();

    public SqlTableModel(ArrayList<SqlScriptFile> fileList) {

        this.fileList = fileList;
        for (int i = 0; i < fileList.size(); i++) {
            this.fileList.get(i).setRow(i);
        }
    }

    @Override
    public String getColumnName(int col) {
        return this.columnNames[col];
    }

    @Override
    public int getRowCount() {
        if (fileList == null) {
            return 0;
        }
        return fileList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    public boolean isEmpty() {
        if (fileList == null) {
            return true;
        }
        return fileList.isEmpty();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (this.fileList == null || this.isEmpty() || rowIndex >= this.fileList.size()) {
            return null;
        }

        SqlScriptFile script = fileList.get(rowIndex);
        if (columnIndex == 0) {
            return script;
        } else if (columnIndex == 1) {
            return script.getStatus();
        } else {
            return "Column unknown";
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public Class getColumnClass(int c) {
        if (c == 0) {
            return SqlScriptFile.class;
        } else if (c == 1) {
            return ScriptStatus.class;
        } else {
            //this might be a bad idea
            return String.class;
        }

    }

    public SqlScriptFile getSqlScriptFile(int index) {
        if (index < 0 || index >= this.fileList.size()) {
            return null;
        }
        return this.fileList.get(index);
    }

    public ArrayList<SqlScriptFile> getAllRows(){
        return this.fileList;
    }

}
