package org.bluenautilus.gui;

import org.bluenautilus.cass.CassandraRowRetriever;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.data.UuidConfigItem;
import org.bluenautilus.db.DBRowRetriever;
import org.bluenautilus.gui.cassServerConfiguration.CassConfigPanel;
import org.bluenautilus.gui.sqlServerConfiguration.SqlConfigPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by bstevens on 2/6/15.
 */
public class SqlOrCassEditDialog extends JPanel {

    public JTabbedPane tabbedPane = new JTabbedPane();

    private SqlConfigPanel sqlPanel = null;
    private CassConfigPanel cassPanel = null;
    private WasFor wasFor = WasFor.BOTH;

    private JButton testButton = new JButton("Test It!");

    /**
     * toUpdate can be null
     *
     * @param toUpdate
     */
    public SqlOrCassEditDialog(UuidConfigItem toUpdate) {
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testAndLaunchDialog();
            }
        });

        this.setLayout(new BorderLayout());

        if (toUpdate == null) {  //make both types available
            //and make SqlConfig the default
            SqlConfigItems newSqlItems = new SqlConfigItems();
            newSqlItems.generateUniqueId();
            sqlPanel = new SqlConfigPanel(newSqlItems);
            CassConfigItems newCassItems = new CassConfigItems();
            newCassItems.generateUniqueId();
            cassPanel = new CassConfigPanel(newCassItems);

            sqlPanel.setVisible(true);
            cassPanel.setVisible(false);

            tabbedPane.addTab("SQL DB", sqlPanel);
            tabbedPane.addTab("Cassandra", cassPanel);

            this.tabbedPane.setSelectedIndex(0);
            this.add(tabbedPane, BorderLayout.CENTER);

        } else {  //this is updating existing objects or cloning
            if (toUpdate instanceof SqlConfigItems) {
                sqlPanel = new SqlConfigPanel((SqlConfigItems) toUpdate);
                this.add(sqlPanel, BorderLayout.CENTER);
                this.wasFor = WasFor.SQL;
            }
            if (toUpdate instanceof CassConfigItems) {
                cassPanel = new CassConfigPanel((CassConfigItems) toUpdate);
                this.add(cassPanel, BorderLayout.CENTER);
                this.wasFor = WasFor.CASS;
            }
        }
        this.add(testButton, BorderLayout.SOUTH);
    }

    public UuidConfigItem getWhatWasEdited() {
        int index = tabbedPane.getSelectedIndex();
        if (wasFor.equals(WasFor.SQL) || index == 0) {
            return sqlPanel.pullFieldsFromGui();
        } else {
            return cassPanel.pullFieldsFromGui();
        }
    }

    private enum WasFor {
        SQL,
        CASS,
        BOTH;
    }

    private void testAndLaunchDialog() {
        int index = tabbedPane.getSelectedIndex();
        String answerMessage = "Test was successful";

        if (this.wasFor.equals(WasFor.SQL) || index == 0) {
             SqlConfigItems items = sqlPanel.pullFieldsFromGui();
             DBRunnerSql runner = new DBRunnerSql(items);
            SwingUtilities.invokeLater(runner);
            if(!runner.wasSuccessful()){
                answerMessage = "Test encountered an error: "+ runner.exceptionEncountered;
            }
        }
        if (this.wasFor.equals(WasFor.CASS) || index == 1) {
            CassConfigItems items = cassPanel.pullFieldsFromGui();
            DBRunnerCass runner = new DBRunnerCass(items,this);
            SwingUtilities.invokeLater(runner);
            if(!runner.wasSuccessful()){
                answerMessage = "Test encountered an error: "+ runner.exceptionEncountered;
            }
        }

        JLabel resultLabel = new JLabel(answerMessage);
        final int i = JOptionPane.showConfirmDialog(this,
                resultLabel,
                "Testing Datastore",
                JOptionPane.YES_OPTION, // this is the array
                JOptionPane.PLAIN_MESSAGE);
    }

    public class DBRunnerCass implements Runnable {
        Exception exceptionEncountered = null;
        ArrayList<SqlScriptRow> rows = null;
        CassConfigItems items = null;
        JPanel panel = null;

        public DBRunnerCass(CassConfigItems items, JPanel panel) {
            this.items = items;
            this.panel  = panel;
        }

        @Override
        public void run() {
            try {
                CassandraRowRetriever retriever = new CassandraRowRetriever(items, panel);
                ArrayList<SqlScriptRow> rows = retriever.readDataBase();
            } catch (Exception e) {
                exceptionEncountered = e;
            }
        }

        public boolean wasSuccessful() {
            return (rows != null);

        }

    }


    public class DBRunnerSql implements Runnable {
        Exception exceptionEncountered = null;
        ArrayList<SqlScriptRow> rows = null;
        SqlConfigItems items = null;

        public DBRunnerSql(SqlConfigItems items) {
            this.items = items;
        }

        @Override
        public void run() {
            try {
                DBRowRetriever retriever = new DBRowRetriever(items);
                ArrayList<SqlScriptRow> rows = retriever.readDataBase();
            } catch (Exception e) {
                exceptionEncountered = e;
            }
        }
        public boolean wasSuccessful() {
            return (rows != null);
        }

    }
}