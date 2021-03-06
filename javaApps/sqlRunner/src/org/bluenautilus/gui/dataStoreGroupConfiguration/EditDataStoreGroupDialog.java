package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.bluenautilus.data.UuidConfigItem;
import org.bluenautilus.gui.PrettyPlusMinusPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by bstevens on 1/31/15.
 */
public class EditDataStoreGroupDialog extends PrettyPlusMinusPanel {

    JLabel nicknameLabel = new JLabel("DataStore Nickname: ");
    JLabel dbLabel = new JLabel("Available Datastores: ");

    JTextField nickNameField = new JTextField(20);
    JButton toLeftButton = new JButton("<");
    JButton toRightButton = new JButton(">");

    /**
     * If group is null, we are making a new group.
     */
    public EditDataStoreGroupDialog(String nickname, final DataStoreTable tableFull, final DataStoreTable tableSublist) {
        super(true);
        this.loadImages();
        this.setLayout(new GridBagLayout());
        nickNameField.setToolTipText("Examples: 'local', 'dalcenstg17', 'altostratum3'");
        nickNameField.setText(nickname);

        Dimension tableDimension = new Dimension(300, 400);

        JPanel groupingRightPanel = new JPanel(new BorderLayout());
        JPanel nickNamePanel = new JPanel(new BorderLayout());
        nickNamePanel.add(nicknameLabel, BorderLayout.NORTH);
        nickNamePanel.add(nickNameField, BorderLayout.CENTER);
        nickNamePanel.add(new JLabel("  "), BorderLayout.SOUTH);
        groupingRightPanel.add(nickNamePanel, BorderLayout.NORTH);
        JScrollPane subScroll = new JScrollPane(tableSublist);

        groupingRightPanel.add(subScroll, BorderLayout.CENTER);

        JPanel overAndBackPanel = new JPanel(new GridBagLayout());

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets(top,left,bottom,right), int ipadx, int ipady

        toRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = tableFull.getSelectedRow();
                if (selected >= 0) {
                    UuidConfigItem selectedObject = tableFull.getDataStoreTableModel().getRowObject(selected);
                    tableSublist.getDataStoreTableModel().addUuidItem(selectedObject);
                }
            }
        });

        toLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = tableSublist.getSelectedRow();
                if (selected >= 0) {
                    UuidConfigItem selectedObject = tableSublist.getDataStoreTableModel().getRowObject(selected);
                    tableSublist.getDataStoreTableModel().removeUuidItem(selectedObject);
                }
            }
        });

        overAndBackPanel.add(toRightButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(50, 2, 50, 2), 2, 2));
        overAndBackPanel.add(toLeftButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(50, 2, 50, 2), 2, 2));

        JPanel databaseLeftPanel = new JPanel(new BorderLayout());

        databaseLeftPanel.add(dbLabel, BorderLayout.NORTH);
        JScrollPane fullScroll = new JScrollPane(tableFull);

        databaseLeftPanel.add(fullScroll, BorderLayout.CENTER);
        databaseLeftPanel.add(this.buttonPanel, BorderLayout.SOUTH);

        databaseLeftPanel.setPreferredSize(tableDimension);
        this.add(databaseLeftPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));

        this.add(overAndBackPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));

        groupingRightPanel.setPreferredSize(tableDimension);
        this.add(groupingRightPanel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));
    }

    public JTextField getNickNameField() {
        return nickNameField;
    }

}
