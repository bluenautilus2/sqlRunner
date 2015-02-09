package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.OldExecutable;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidItem;
import org.bluenautilus.gui.PrettyButtonListener;
import org.bluenautilus.gui.SqlOrCassEditorManager;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;
import java.util.List;

/**
 * Created by bstevens on 1/27/15.
 */
public class DataStorePanelManager implements PrettyButtonListener, DataStoreConfigChangedListener {

    private static Log log = LogFactory.getLog(OldExecutable.class);
    DataStoreGroupPanel myDsgPanel = null;

    DataStoreTableModel tableModelFull = null;
    DataStoreTableModel tableModelSublist = null;
    DataStoreTable tableSublist = null;
    DataStoreTable tableFull = null;

    SqlOrCassEditorManager myEditorManager = new SqlOrCassEditorManager(this);

    public DataStorePanelManager(DataStoreGroupPanel dsgPanel) {
        myDsgPanel = dsgPanel;
        dsgPanel.addListener(this);
    }


    @Override
    public void prettyButtonClicked(ButtonType type) {
        DataStoreGroup group = myDsgPanel.getCurrentJComboBox();

        switch (type) {
            case GEAR:
                launchDialog(group, type);
                break;
            case MINUS:
                DataStoreGroupConfigUtil.removeAndSave(group.getUniqueId());
                myDsgPanel.updateComboBoxList();
                break;
            case PLUS:
                launchDialog(null, type);
                break;
            case COPY:
                DataStoreGroup clonedGroup = group.clone();
                launchDialog(clonedGroup, type);
        }

    }

    private void launchDialog(DataStoreGroup editedGroup, ButtonType typeOfEdit) {
        tableModelFull = new DataStoreTableModel(CassConfigUtil.getCassConfigItemsList(), SqlConfigUtil.getSqlConfigItemsList());
        tableFull = new DataStoreTable(tableModelFull);

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
                }
            } else {  //it's new (plus or cloned)
                DataStoreGroup newGroup = new DataStoreGroup();
                newGroup.generateUniqueId();
                newGroup.setNickname(dialog.getNickNameField().getText());
                newGroup.setDataStoreItems(tableSublist.getDataStoreTableModel().getAllRows());
                DataStoreGroupConfigUtil.addAndSave(newGroup);
            }
            myDsgPanel.updateComboBoxList();

        }

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
