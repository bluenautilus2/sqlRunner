package org.bluenautilus.gui;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidItem;
import org.bluenautilus.gui.cassServerConfiguration.CassConfigPanel;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStoreConfigChangedListener;
import org.bluenautilus.gui.sqlServerConfiguration.SqlConfigPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bstevens on 2/6/15.
 */
public class SqlOrCassOuterPanel extends JPanel {

    private List<DataStoreConfigChangedListener> listeners = new ArrayList<>();

    public JRadioButton cassButton = new JRadioButton("Cassandra");
    public JRadioButton sqlButton = new JRadioButton("MS Sql Server");
    public ButtonGroup buttonGroup = new ButtonGroup();
    public JPanel buttonPanel = new JPanel(new BorderLayout());

    SqlConfigPanel sqlPanel = null;
    CassConfigPanel cassPanel = null;

    boolean isSql = true;

    public SqlOrCassOuterPanel(UuidItem toUpdate){
        this.setLayout(new BorderLayout());

        if(toUpdate==null) {  //make both types available
            SqlConfigItems newSqlItems = new SqlConfigItems();
            newSqlItems.generateUniqueId();
            sqlPanel = new SqlConfigPanel(newSqlItems);
            CassConfigItems newCassItems = new CassConfigItems();
            newCassItems.generateUniqueId();
            cassPanel = new CassConfigPanel(newCassItems);

            cassButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    cassSelected();
                }
            });

            sqlButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    sqlSelected();
                }
            });

            sqlPanel.setVisible(false);
            cassPanel.setVisible(false);

            buttonGroup.add(cassButton);
            buttonGroup.add(sqlButton);

            buttonPanel.add(cassButton, BorderLayout.NORTH);
            buttonPanel.add(sqlButton, BorderLayout.SOUTH);

            this.add(buttonPanel, BorderLayout.NORTH);
            this.isSql = true;
            this.add(sqlPanel,BorderLayout.CENTER);

        }else {  //this is updating existing objects
           if(toUpdate instanceof SqlConfigItems){
               sqlPanel = new SqlConfigPanel((SqlConfigItems)toUpdate);
               this.add(sqlPanel,BorderLayout.CENTER);
           }
           if(toUpdate instanceof CassConfigItems){
               cassPanel = new CassConfigPanel((CassConfigItems)toUpdate);
               this.add(cassPanel, BorderLayout.CENTER);
           }
        }
    }

    private void cassSelected() {
        if (isSql) {
            sqlPanel.setVisible(false);
            this.remove(sqlPanel);
            this.add(cassPanel, BorderLayout.CENTER);
            cassPanel.setVisible(true);
            isSql = false;
        }
    }

    private void sqlSelected() {
        if (!isSql) {
            cassPanel.setVisible(false);
            this.remove(cassPanel);
            this.add(sqlPanel, BorderLayout.CENTER);
            sqlPanel.setVisible(true);
            isSql = true;
        }
    }

    public void addListener(DataStoreConfigChangedListener newListener){
        this.listeners.add(newListener);
    }

    public UuidItem getWhatWasEdited(){
        if(isSql){
            return sqlPanel.pullFieldsFromGui();
        }else{
            return cassPanel.pullFieldsFromGui();
        }
    }
}
