package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidItem;
import org.bluenautilus.gui.ParentPlusMinusPanel;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bstevens on 1/26/15.
 */
public class DataStoreGroupPanel extends ParentPlusMinusPanel implements DataStoreConfigChangedListener {

    JComboBox<DataStoreGroup> nicknameDropdown = null;

    List<NewDataStoreGroupChosenListener> newGroupChosenListeners = new ArrayList<>();

    public void addNewGroupChosenListener(NewDataStoreGroupChosenListener listener) {
        this.newGroupChosenListeners.add(listener);
    }

    public DataStoreGroup getCurrentJComboBox() {
        return (DataStoreGroup) nicknameDropdown.getSelectedItem();
    }

    public void init() {
this.setLayout(new GridBagLayout());
        loadImages();
        initComboBox();


        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        this.add(this.nicknameDropdown, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));

        this.add(this.buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));


    }

    public void initComboBox() {
        nicknameDropdown = new JComboBox<DataStoreGroup>();
        for (DataStoreGroup group : DataStoreGroupConfigUtil.getDataStoreGroupList().getDataStoreGroupList()) {
            nicknameDropdown.addItem(group);
        }

        this.nicknameDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (e.getStateChange() == ItemEvent.SELECTED) {

                            Object o = nicknameDropdown.getModel().getSelectedItem();
                            DataStoreGroup newGroup = null;
                            if (o != null) {
                                newGroup = (DataStoreGroup) o;
                            } else {
                                newGroup = nicknameDropdown.getModel().getElementAt(0);
                            }

                            nicknameDropdown.getModel().setSelectedItem(newGroup);
                            for (NewDataStoreGroupChosenListener listener : newGroupChosenListeners) {
                                listener.dataGroupChosen(newGroup);
                            }
                        }
                    }
                });

            }
        });
    }

    public void updateComboBoxList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                nicknameDropdown.removeAllItems();

                for (DataStoreGroup group : DataStoreGroupConfigUtil.getDataStoreGroupList().getDataStoreGroupList()) {
                    nicknameDropdown.addItem(group);
                }
            }
        });
    }

    @Override
    public void newSqlConfig(SqlConfigItems newSql) {

    }

    @Override
    public void newCassConfig(CassConfigItems newCass) {

    }

    @Override
    public void updatedSqlConfig(SqlConfigItems updatedSql) {

    }

    @Override
    public void updatedCassConfig(CassConfigItems updatedCass) {

    }

    @Override
    public void deletedDataStore(UuidItem deletedItem) {

    }
}
