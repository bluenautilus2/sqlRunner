package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.gui.PrettyButtonListener;
import org.bluenautilus.gui.SqlOrCassEditorManager;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by bstevens on 1/27/15.
 */
public class DataStorePanelManager implements PrettyButtonListener {

    private static Log log = LogFactory.getLog(DataStorePanelManager.class);
    DataStoreGroupPanel myDsgPanel = null;

    DataStoreTableModel tableModelFull = null;
    DataStoreTableModel tableModelSublist = null;
    DataStoreTable tableSublist = null;
    DataStoreTable tableFull = null;

    public DataStorePanelManager(DataStoreGroupPanel dsgPanel) {
        myDsgPanel = dsgPanel;
        myDsgPanel.addListener(this);
    }


    @Override
    public void prettyButtonClicked(ButtonType type) {
        DataStoreGroup group = myDsgPanel.getCurrentJComboBox();
        DataStoreGroup groupToReturn = null;
        switch (type) {
            case GEAR:
                groupToReturn = launchDialog(group, type);
                myDsgPanel.updateComboBoxList(groupToReturn);
                break;
            case MINUS:
                DataStoreGroupConfigUtil.removeAndSave(group.getUniqueId());
                myDsgPanel.updateComboBoxList(null);
                break;
            case PLUS:
                groupToReturn = launchDialog(null, type);
                myDsgPanel.updateComboBoxList(groupToReturn);
                break;
            case COPY:
                //currently not enabled
                DataStoreGroup clonedGroup = group.clone();
                groupToReturn = launchDialog(clonedGroup, type);
                myDsgPanel.updateComboBoxList(groupToReturn);
                break;
        }

    }

    private DataStoreGroup launchDialog(DataStoreGroup editedGroup, ButtonType typeOfEdit) {
        tableModelFull = new DataStoreTableModel(CassConfigUtil.getCassConfigItemsList(), SqlConfigUtil.getSqlConfigItemsList());
        tableFull = new DataStoreTable(tableModelFull);
        DataStoreGroup groupToReturn = null;

        if (editedGroup != null) {
            List<CassConfigItems> cassItems = CassConfigUtil.getUuidList(editedGroup.getDataStores());
            List<SqlConfigItems> sqlItems = SqlConfigUtil.getUuidList(editedGroup.getDataStores());
            tableModelSublist = new DataStoreTableModel(cassItems, sqlItems);
            tableSublist = new DataStoreTable(tableModelSublist);
        } else {
            tableModelSublist = new DataStoreTableModel();
            tableSublist = new DataStoreTable(tableModelSublist);
        }
        String nickname = editedGroup != null ? editedGroup.getNickname() : "";
        final EditDataStoreGroupDialog dialog = new EditDataStoreGroupDialog(nickname, tableFull, tableSublist);
        SqlOrCassEditorManager myEditorManager = new SqlOrCassEditorManager(dialog, tableFull, tableSublist);
        dialog.addListener(myEditorManager);

        final int i = JOptionPane.showOptionDialog(myDsgPanel,
                dialog,
                "Edit DataStoreGroup",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Save", "Cancel"}, // this is the array
                "default");

        if (i == JOptionPane.OK_OPTION) {
            if (typeOfEdit.equals(ButtonType.GEAR)) {  //editing existing
                if (editedGroup != null) {
                    editedGroup.setNickname(dialog.getNickNameField().getText());
                    editedGroup.setDataStoreItems(tableSublist.getDataStoreTableModel().getAllRows());
                    DataStoreGroupConfigUtil.replaceWithUpdatesAndSave(editedGroup);
                    groupToReturn = editedGroup;
                }
            } else {  //it's new (plus)
                DataStoreGroup newGroup = new DataStoreGroup();
                newGroup.generateUniqueId();
                newGroup.setNickname(dialog.getNickNameField().getText());
                newGroup.setDataStoreItems(tableSublist.getDataStoreTableModel().getAllRows());
                DataStoreGroupConfigUtil.addAndSave(newGroup);
                groupToReturn = newGroup;
            }
        }
        return groupToReturn;
    }


}
