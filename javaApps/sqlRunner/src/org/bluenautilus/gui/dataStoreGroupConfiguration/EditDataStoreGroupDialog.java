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
    JLabel dbLabel = new JLabel("Available Databases");

    JTextField nickNameField = new JTextField(20);
    JButton toLeftButton = new JButton("<");
    JButton toRightButton = new JButton(">");

    DataStoreTableModel tableModelFull = null;
    DataStoreTableModel tableModelSublist = null;
    DataStoreTable tableSublist = null;
    DataStoreTable tableFull = null;

    JPanel DatabaseLeftPanel = null;
    JPanel GroupingRightPanel = null;

    /**
     * If group is null, we are making a new group.
     *
     * @param group
     */
    public EditDataStoreGroupDialog(DataStoreGroup group) {

        this.setLayout(new GridBagLayout());
        nickNameField.setToolTipText("Examples: 'local', 'dalcenstg17', 'altostratum3'");

        tableModelFull = new DataStoreTableModel(CassConfigUtil.getCassConfigItemsList(), SqlConfigUtil.getSqlConfigItemsList());
        tableFull = new DataStoreTable(tableModelFull);

        if (group != null) {
            nickNameField.setText(group.getNickname());
            List<CassConfigItems> cassItems = CassConfigUtil.getUuidList(group.getDataStores());
            List<SqlConfigItems> sqlItems = SqlConfigUtil.getUuidList(group.getDataStores());
            DataStoreTableModel tableModelSublist = new DataStoreTableModel(cassItems, sqlItems);
            tableSublist = new DataStoreTable(tableModelSublist);
        } else {
            DataStoreTableModel tableModelSublist = new DataStoreTableModel();
            tableSublist = new DataStoreTable(tableModelSublist);
        }

        JPanel groupingRightPanel = new JPanel(new BorderLayout());
        JPanel nickNamePanel = new JPanel(new BorderLayout());
        nickNamePanel.add(nicknameLabel, BorderLayout.WEST);
        nickNamePanel.add(nickNameField, BorderLayout.EAST);
        groupingRightPanel.add(nickNamePanel, BorderLayout.NORTH);
        groupingRightPanel.add(tableSublist, BorderLayout.CENTER);

        JPanel overAndBackPanel = new JPanel(new GridBagLayout());

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        overAndBackPanel.add(toRightButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(2, 2, 100, 100), 2, 2));
        overAndBackPanel.add(toLeftButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(2, 2, 100, 100), 2, 2));

        JPanel databaseLeftPanel = new JPanel(new BorderLayout());
        databaseLeftPanel.add(dbLabel, BorderLayout.NORTH);
        databaseLeftPanel.add(tableFull, BorderLayout.CENTER);

        this.add(databaseLeftPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));

        this.add(overAndBackPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));

        this.add(groupingRightPanel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));
    }
}
