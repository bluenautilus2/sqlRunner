package org.bluenautilus.gui;

import org.bluenautilus.cass.CassandraRowRetriever;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.data.UuidConfigItem;
import org.bluenautilus.db.DBRowRetriever;
import org.bluenautilus.gui.cassServerConfiguration.CassConfigPanel;
import org.bluenautilus.gui.sqlServerConfiguration.SqlConfigPanel;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by bstevens on 2/6/15.
 */
public class SqlOrCassEditDialog extends JPanel {

    private JTabbedPane tabbedPane = new JTabbedPane();

    private SqlConfigPanel sqlPanel = null;
    private CassConfigPanel cassPanel = null;
    private WasFor wasFor = WasFor.BOTH;

    private JButton testButton = new JButton("Test It!");
    private Color oldBackgroundColor = testButton.getBackground();

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
        testButton.setText("WAIT......");
        testButton.setBackground(Color.yellow);

        int index = tabbedPane.getSelectedIndex();

        if (this.wasFor.equals(WasFor.SQL) || index == 0) {
            SqlConfigItems items = sqlPanel.pullFieldsFromGui();
            DBRunnerSql runner = new DBRunnerSql(items, this, sqlPanel.getScriptFolder());
            SwingUtilities.invokeLater(runner);
        }
        if (this.wasFor.equals(WasFor.CASS) || index == 1) {
            CassConfigItems items = cassPanel.pullFieldsFromGui();
            DBRunnerCass runner = new DBRunnerCass(items, this, cassPanel.getScriptFolder());
            SwingUtilities.invokeLater(runner);
        }

    }

    public class DBRunnerCass implements Runnable {
        Exception exceptionEncountered = null;
        ArrayList<SqlScriptRow> rows = null;
        CassConfigItems items = null;
        JPanel panel = null;
        String scriptFolder = null;

        public DBRunnerCass(CassConfigItems items, JPanel panel, String folderName) {
            this.items = items;
            this.panel = panel;
            this.scriptFolder = folderName;
        }

        @Override
        public void run() {
            try {
                testFolder(scriptFolder, WasFor.CASS);
                CassandraRowRetriever retriever = new CassandraRowRetriever(items);
                rows = retriever.readDataBase();

            } catch (Exception e) {
                exceptionEncountered = e;
            }
            testButton.setBackground(oldBackgroundColor);
            testButton.setText("Test it!");
            JOptionPane.showMessageDialog(panel, makeDialogComponent(wasSuccessful(), exceptionEncountered), "Test Complete", JOptionPane.NO_OPTION);
        }

        public boolean wasSuccessful() {
            return (rows != null);

        }

    }


    public class DBRunnerSql implements Runnable {
        Exception exceptionEncountered = null;
        ArrayList<SqlScriptRow> rows = null;
        SqlConfigItems items = null;
        JPanel panel = null;
        String scriptFolder = null;

        public DBRunnerSql(SqlConfigItems items, JPanel panel, String folderName) {
            this.items = items;
            this.panel = panel;
            this.scriptFolder = folderName;
        }

        @Override
        public void run() {

            try {
                testFolder(scriptFolder, WasFor.SQL);
                DBRowRetriever retriever = new DBRowRetriever(items);
                rows = retriever.readDataBase();

            } catch (Exception e) {
                exceptionEncountered = e;
            }
            testButton.setBackground(oldBackgroundColor);
            testButton.setText("Test it!");
            JOptionPane.showMessageDialog(panel, makeDialogComponent(wasSuccessful(), exceptionEncountered), "Test Complete", JOptionPane.NO_OPTION);
        }

        public boolean wasSuccessful() {
            return (rows != null);
        }

    }

    private String testFolder(String folderName, WasFor wasFor) throws Exception {
        File f = new File(folderName);
        if (f.exists()) {
            if (f.canRead()) {
                if(wasFor == WasFor.CASS){
                    DataStoreGroupConfigUtil.updateLastUsedFileFolderCass(folderName);
                }else{
                    DataStoreGroupConfigUtil.updateLastUsedFileFolderSql(folderName);
                }
                return "Folder found: " + folderName;

            } else {
                throw new Exception("Don't have read access to: " + folderName);
            }
        } else {
            throw new FileNotFoundException("Could not find folder: "+ folderName);
        }
    }

    private Component makeDialogComponent(boolean wasSuccessfull, Exception e) {

        if (wasSuccessfull) {
            JLabel answerLabel = new JLabel("Test was successful!!");
            answerLabel.setForeground(new Color(10, 100, 50));
            return answerLabel;
        }

        String errorString = "Test Failed: ";
        if (e == null) {
            errorString = errorString + " but no error was returned ";
        } else {
            errorString = errorString + e.getMessage();
        }

        JEditorPane pane = new JEditorPane();
        pane.setText(errorString);
        pane.setForeground(new Color(150, 10, 10));
        JScrollPane scroll = new JScrollPane(pane);
        scroll.setPreferredSize(new Dimension(400, 400));
        return scroll;
    }
}