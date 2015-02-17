package org.bluenautilus.gui;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStoreTable;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;

/**
 * Created by bstevens on 2/6/15.
 */
public class SqlOrCassEditorManager implements PrettyButtonListener {

    private JPanel parentPanel = null;
    private DataStoreTable subListTable;
    private DataStoreTable fullTable;


    public SqlOrCassEditorManager(JPanel parentPanel, DataStoreTable fullTable, DataStoreTable subListTable) {
        this.parentPanel = null;
        this.subListTable = subListTable;
        this.fullTable = fullTable;

    }

    @Override
    public void prettyButtonClicked(ButtonType type) {
        UuidConfigItem editedItem = null;
        int index = fullTable.getSelectedRow();
        if (index >= 0) {
            editedItem = fullTable.getDataStoreTableModel().getRowObject(index);
        }

        UuidConfigItem newItem  = null;

        switch (type) {
            case MINUS:
                if(editedItem!=null){
                    if(editedItem instanceof SqlConfigItems){
                        SqlConfigUtil.removeAndSave((SqlConfigItems) editedItem);
                    }
                    if(editedItem instanceof CassConfigItems){
                        CassConfigUtil.removeAndSave((CassConfigItems) editedItem);
                    }
                    fullTable.getDataStoreTableModel().removeUuidItem(editedItem);
                    subListTable.getDataStoreTableModel().removeUuidItem(editedItem);
                    DataStoreGroupConfigUtil.removeDataStoreFromAllGroupsAndSave(editedItem);
                }
                break;
            case PLUS:
                 newItem = launchDialog(null);
                break;
            case GEAR:
                UuidConfigItem updatedItem = launchDialog(editedItem);
                if(updatedItem!=null){
                    if(updatedItem instanceof SqlConfigItems){
                        SqlConfigUtil.replaceWithUpdatesAndSave((SqlConfigItems)updatedItem);
                    }
                    if(updatedItem instanceof CassConfigItems){
                        CassConfigUtil.replaceWithUpdatesAndSave((CassConfigItems) updatedItem);
                    }
                    //cheap, I know.
                    fullTable.getDataStoreTableModel().replaceUuidItem(updatedItem);
                }
                break;
            case COPY:
                newItem = launchDialog(editedItem.clone());
                break;
        }

        if(newItem!=null){
            if(newItem instanceof SqlConfigItems){
                SqlConfigUtil.addAndSave((SqlConfigItems)newItem);
            }
            if(newItem instanceof CassConfigItems){
                CassConfigUtil.addAndSave((CassConfigItems) newItem);
            }
            fullTable.getDataStoreTableModel().addUuidItem(newItem);
        }

    }

    private UuidConfigItem launchDialog(UuidConfigItem toUpdate) {

        SqlOrCassOuterPanel panel = new SqlOrCassOuterPanel(toUpdate);

        final int i = JOptionPane.showOptionDialog(parentPanel,
                panel,
                "Edit DataStoreGroup",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Save", "Cancel"}, // this is the array
                "default");
        if (i == JOptionPane.OK_OPTION) {
            return panel.getWhatWasEdited();
        }
        return null;
    }

}
