package org.bluenautilus.gui;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStoreConfigChangedListener;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStoreTable;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;

/**
 * Created by bstevens on 2/6/15.
 */
public class SqlOrCassEditorManager implements DataStoreConfigChangedListener, PrettyButtonListener {

    private JPanel parentPanel = null;
    private DataStoreTable table;

    public SqlOrCassEditorManager(JPanel parentPanel, DataStoreTable fullTable) {
        this.parentPanel = null;
        table = fullTable;
    }

    @Override
    public void prettyButtonClicked(ButtonType type) {
        UuidConfigItem editedItem = null;
        int index = table.getSelectedRow();
        if (index >= 0) {
            editedItem = table.getDataStoreTableModel().getRowObject(index);
        }

        UuidConfigItem updatedItem = null;

        switch (type) {
            case MINUS:
                if(editedItem!=null){
                    if(editedItem instanceof SqlConfigItems){
                        SqlConfigUtil.removeAndSave((SqlConfigItems) editedItem);
                    }
                    if(editedItem instanceof CassConfigItems){
                        CassConfigUtil.removeAndSave((CassConfigItems) editedItem);
                    }
                    table.getDataStoreTableModel().removeUuidItem(editedItem);
                }
                break;
            case PLUS:
                UuidConfigItem newItem = launchDialog(null);
                if(newItem!=null){
                    if(newItem instanceof SqlConfigItems){
                        SqlConfigUtil.addAndSave((SqlConfigItems)newItem);
                    }
                    if(newItem instanceof CassConfigItems){
                        CassConfigUtil.addAndSave((CassConfigItems) newItem);
                    }
                    table.getDataStoreTableModel().addUuidItem(newItem);
                }
                break;
            case GEAR:
                updatedItem = launchDialog(editedItem);
                break;
            case COPY:
                updatedItem = launchDialog(editedItem.clone());
                break;
        }


        if(updatedItem!=null){
            if(updatedItem instanceof SqlConfigItems){
                SqlConfigUtil.replaceWithUpdatesAndSave((SqlConfigItems)updatedItem);
            }
            if(updatedItem instanceof CassConfigItems){
                CassConfigUtil.replaceWithUpdatesAndSave((CassConfigItems) updatedItem);
            }
            //cheap, I know.
            table.getDataStoreTableModel().replaceUuidItem(updatedItem);
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


    @Override
    public void newSqlConfig(SqlConfigItems newSql) {
        //sql or cass panel

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
    public void deletedDataStore(UuidConfigItem deletedItem) {

    }


}
