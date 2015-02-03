package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.OldExecutable;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.DataStoreGroupList;
import org.bluenautilus.gui.PrettyButtonListener;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

import javax.swing.*;
import java.util.List;

/**
 * Created by bstevens on 1/27/15.
 */
public class DataStorePanelManager implements PrettyButtonListener, DataStoreConfigChangedListener{

    private static Log log = LogFactory.getLog(OldExecutable.class);
    DataStoreGroupPanel myDsgPanel = null;

    public DataStorePanelManager(DataStoreGroupPanel dsgPanel) {
        myDsgPanel = dsgPanel;
        dsgPanel.addListener(this);
    }

    @Override
    public void dataStoreConfigChanged(List<DataStoreGroupList> newOrChangedItems) {

    }

    @Override
    public void prettyButtonClicked(ButtonType type) {
        DataStoreGroup group = myDsgPanel.getCurrentJComboBox();

        switch(type){
            case GEAR:
                JOptionPane.showOptionDialog(myDsgPanel,
                        new EditDataStoreGroupDialog(group) ,
                        "Edit DataStoreGroup",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new String[]{"Save", "Cancel"}, // this is the array
                        "default");
            case MINUS:
                DataStoreGroupConfigUtil.removeDataStoreGroup(group.getUniqueId());
                DataStoreGroupConfigUtil.saveOffCurrent();
                myDsgPanel.updateComboBoxList();
                break;
            case PLUS:
                break;

        }

    }


}
