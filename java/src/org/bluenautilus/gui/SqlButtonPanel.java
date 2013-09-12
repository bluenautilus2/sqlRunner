package org.bluenautilus.gui;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.script.ScriptKickoffListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 10:33 PM
 */
public class SqlButtonPanel extends JPanel {

    FieldItems fields = null;
    private JTextField dbNameField = new JTextField(15);
    private JTextField loginField = new JTextField(15);
    private JTextField passwordField = new JTextField(15);
    private JTextField scriptFolderField = new JTextField(35);
    private JTextField ipAddressField = new JTextField(15);
    private JButton refreshButton = new JButton("REFRESH");
    private JButton oneScriptButton = new JButton("Run Selected");
    private JButton allButton = new JButton("Run All");

    public SqlButtonPanel(FieldItems initialFields) {
        super(new GridBagLayout());
        this.fields = initialFields;
        this.init();
    }

    private void init() {

        this.setFields(this.fields);

        JLabel dbNameLabel = new JLabel("Database Name");
        JLabel ipAddress = new JLabel("IP Address");
        JLabel userName = new JLabel("Login");
        JLabel password = new JLabel("Password");
        JLabel folderName = new JLabel("SQL Script Folder");

        this.refreshButton.setToolTipText("Rescans File Directory and Database");
        this.oneScriptButton.setToolTipText("Run only the script(s) that are selected");
        this.allButton.setToolTipText("Runs all scripts showing as \'Need to Run\'");

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        //LABELS
        this.add(dbNameLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        this.add(ipAddress, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        this.add(userName, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        this.add(password, new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        this.add(folderName, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //TEXT FIELDS
        this.add(this.dbNameField, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        this.add(this.ipAddressField, new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        this.add(this.loginField, new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        this.add(this.passwordField, new GridBagConstraints(4, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //this fills up two spots
        this.add(this.scriptFolderField, new GridBagConstraints(2, 2, 3, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //Buttons
        this.add(this.refreshButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 2, 2), 2, 2));

        this.add(this.allButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 2, 2), 2, 2));

        this.add(this.oneScriptButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 2, 2), 2, 2));

    }

    public FieldItems pullFieldsFromGui() {
        return new FieldItems(
                this.dbNameField.getText(),
                this.loginField.getText(),
                this.passwordField.getText(),
                this.scriptFolderField.getText(),
                this.ipAddressField.getText());
    }

    public void setFields(FieldItems fields) {
        this.dbNameField.setText(fields.getDbNameField());
        this.loginField.setText(fields.getLoginField());
        this.passwordField.setText(fields.getPasswordField());
        this.scriptFolderField.setText(fields.getScriptFolderField());
        this.ipAddressField.setText(fields.getIpAddressField());
    }

    public void addRefreshListener(final RefreshListener listener) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.refreshAction();
            }
        };
        this.refreshButton.addActionListener(actionListener);
    }

    public void addScriptKickoffListener(final ScriptKickoffListener listener) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.kickoffSelectedScripts();
            }
        };
        this.oneScriptButton.addActionListener(actionListener);

    }

    public void addScriptRunAllToRunListener(final ScriptKickoffListener listener) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.kickoffAllToRunScripts();
            }
        };
        this.allButton.addActionListener(actionListener);

    }

}
