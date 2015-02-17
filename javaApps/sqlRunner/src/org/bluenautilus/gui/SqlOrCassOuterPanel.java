package org.bluenautilus.gui;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;
import org.bluenautilus.gui.cassServerConfiguration.CassConfigPanel;
import org.bluenautilus.gui.sqlServerConfiguration.SqlConfigPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bstevens on 2/6/15.
 */
public class SqlOrCassOuterPanel extends JPanel {

    public JTabbedPane tabbedPane = new JTabbedPane();

    private SqlConfigPanel sqlPanel = null;
    private CassConfigPanel cassPanel = null;
     private WasFor wasFor = WasFor.BOTH;

    /**
     * toUpdate can be null
     *
     * @param toUpdate
     */
    public SqlOrCassOuterPanel(UuidConfigItem toUpdate) {
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
    }

    public UuidConfigItem getWhatWasEdited() {
        int index = tabbedPane.getSelectedIndex();
        if (wasFor.equals(WasFor.SQL) || index==0) {
            return sqlPanel.pullFieldsFromGui();
        } else{
            return cassPanel.pullFieldsFromGui();
        }
    }

    private enum WasFor{
        SQL,
        CASS,
        BOTH;
    }
}
