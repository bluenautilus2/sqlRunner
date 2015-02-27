package org.bluenautilus.gui;

import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptStatus;
import org.bluenautilus.script.ScriptStatusChangeListener;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.ArrayList;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 11:47 AM
 */
public class SqlScriptTablePanel extends JPanel implements ScriptStatusChangeListener, ScriptCompletionListener {

    ScriptTable theTable = null;
    SqlTableModel tableModel = null;


    public SqlScriptTablePanel() {
        super(new BorderLayout());
        //fill with empty array
        ArrayList<SqlScriptFile> fileList = new ArrayList<SqlScriptFile>();
        tableModel = new SqlTableModel(fileList);
        theTable = new ScriptTable(tableModel);
        JScrollPane scroll = new JScrollPane(theTable);

        scroll.setPreferredSize(new Dimension(350,600));
        this.add(scroll, BorderLayout.CENTER);

        this.setVisible(true);
    }




    public void setValues(ArrayList<SqlScriptFile> fileList) {
        tableModel = new SqlTableModel(fileList);
        theTable.setModel(tableModel);
        this.updateTable();
        theTable.fixColumns();
    }

    public void updateTable() {
        TableModelEvent event = new TableModelEvent(this.tableModel);
        this.tableModel.fireTableChanged(event);
    }

    public void addTableListener(ListSelectionListener listener) {
        this.theTable.getSelectionModel().addListSelectionListener(listener);
    }

    public SqlScriptFile getFile(int row) {
        return this.tableModel.getSqlScriptFile(row);
    }

    public ArrayList<SqlScriptFile> getAllSelected() {

        ArrayList<SqlScriptFile> answer = new ArrayList<SqlScriptFile>();
        int[] selList = this.theTable.getSelectedRows();

        for (int index : selList) {
            answer.add(this.getFile(index));

        }
        return answer;

    }

    public ArrayList<SqlScriptFile> getAllToRun() {

        ArrayList<SqlScriptFile> answer = new ArrayList<SqlScriptFile>();

        ArrayList<SqlScriptFile> allList = this.tableModel.getAllRows();

        for(SqlScriptFile file: allList){
            if(ScriptStatus.NEED_TO_RUN.equals(file.getStatus())){
                answer.add(file);
            }
        }
        return answer;

    }

    @Override
    public void updateTableRowStatus(SqlScriptFile file, ScriptStatus newStatus) {
        this.theTable.clearSelection();
        this.tableModel.getSqlScriptFile(file.getTableRowIndex()).setStatus(newStatus);
        this.updateTable();
    }

    @Override
    public void scriptComplete(ScriptResultsEvent e) {
        this.tableModel.getSqlScriptFile(e.getScriptFile().getTableRowIndex()).setResultsString(e.getOutput());
    }
}
