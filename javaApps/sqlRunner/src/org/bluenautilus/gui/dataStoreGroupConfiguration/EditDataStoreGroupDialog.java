package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * Created by bstevens on 1/31/15.
 */
public class EditDataStoreGroupDialog extends JPanel {

    JLabel nicknameLabel = new JLabel("Nickname");

    JTextField nickNameField = new JTextField(20);
    JButton toLeftButton = new JButton("<");
    JButton toRightButton = new JButton(">");

    /**
     * If group is null, we are making a new group.
     * @param group
     */
    public EditDataStoreGroupDialog(DataStoreGroup group){


        this.setLayout(new GridBagLayout());
        nickNameField.setToolTipText("Examples: 'local', 'dalcenstg17', 'altostratum3'");

        DataStoreTableModel tableModelFull = new DataStoreTableModel(CassConfigUtil.getCassConfigItemsList(), SqlConfigUtil.getSqlConfigItemsList());
        DataStoreTable tableFull = new DataStoreTable(tableModelFull);



        if(group!=null){
            nickNameField.setText(group.getNickname());
            List<CassConfigItems> cassItems = CassConfigUtil.getUuidList(group.getDataStores());
            List<SqlConfigItems> sqlItems = SqlConfigUtil.getUuidList(group.getDataStores());
            DataStoreTableModel tableModelSublist = new DataStoreTableModel(cassItems,sqlItems);
            DataStoreTable tableSublist = new DataStoreTable(tableModelFull);
        }else{
            DataStoreTableModel tableModelSublist = new DataStoreTableModel();
            DataStoreTable tableSublist = new DataStoreTable(tableModelFull);
        }


        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        this.add(this.nicknameLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));
    }
}
