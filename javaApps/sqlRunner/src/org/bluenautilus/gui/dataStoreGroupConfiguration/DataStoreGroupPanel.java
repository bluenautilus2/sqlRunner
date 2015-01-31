package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.DataStoreGroupList;
import org.bluenautilus.gui.ParentPlusMinusPanel;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

import javax.swing.*;
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

    @Override
    public void dataStoreConfigChanged(List<DataStoreGroupList> newOrChangedItems) {

    }

    public DataStoreGroupPanel() {
        super();
    }

    public void init() {
        loadImages();
        initComboBox();
        this.add(nicknameDropdown);
        this.add(buttonPanel);


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
                            DataStoreGroup newGroup = (DataStoreGroup) nicknameDropdown.getSelectedItem();
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


}
