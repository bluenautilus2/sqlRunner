package org.bluenautilus.gui.sqlServerConfiguration;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.db.Target;
import org.bluenautilus.gui.FolderOpenButton;
import org.bluenautilus.gui.RunButtonPanel;
import org.bluenautilus.gui.UpdatePreferencesListener;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.MiscUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bstevens on 2/8/15.
 */
public class SqlConfigPanel extends JPanel {

    private static Log LOG = LogFactory.getLog(RunButtonPanel.class);

    SqlConfigItems fields = null;
    private JTextField dbNameField = new JTextField(15);
    private JTextField loginField = new JTextField(8);
    private JTextField passwordField = new JPasswordField(8);
    private JTextField scriptFolderField = new JTextField(35);
    private JTextField ipAddressField = new JTextField(15);
    private JTextField portField = new JTextField(8);
    private JComboBox dbConnectionTypeField;
    private UUID uuidField = null;
    private JComboBox<Target> targetDropDown = new JComboBox<>();

    private Color borderColor = new Color(180, 180, 180);

    ArrayList<UpdatePreferencesListener> updateListeners = new ArrayList<UpdatePreferencesListener>();

    public SqlConfigPanel(SqlConfigItems initialFields) {
        super(new GridBagLayout());
        this.fields = initialFields;
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        for (Target t : Target.values()) {
            this.targetDropDown.addItem(t);
        }

        this.setFields(this.fields);

        JLabel dbNameLabel = new JLabel("Database Name");
        JLabel ipAddress = new JLabel("IP Address");
        JLabel userName = new JLabel("Login");
        JLabel password = new JLabel("Password");
        JLabel folderName = new JLabel("SQL Script Folder");
        JLabel portLabel = new JLabel("Port");
        JLabel dbConnectionTypeLabel = new JLabel("DB Connection Method");
        JLabel targetLabel = new JLabel("Target");
        FolderOpenButton openScriptFolderButton = new FolderOpenButton(this, this.scriptFolderField);

        this.initDBConnectionDropDown();

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        //Center panel buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());

        centerPanel.add(folderName, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(dbNameLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(ipAddress, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(dbConnectionTypeLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(portLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(userName, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(password, new GridBagConstraints(0, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(targetLabel, new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        //------------------------------------------------------------------------

        //this fills up three spots
        JPanel scriptHolder = new JPanel(new BorderLayout());
        scriptHolder.add(this.scriptFolderField, BorderLayout.WEST);
        scriptHolder.add(openScriptFolderButton, BorderLayout.EAST);
        centerPanel.add(scriptHolder, new GridBagConstraints(1, 0, 5, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        centerPanel.add(this.dbNameField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.ipAddressField, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.dbConnectionTypeField, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.portField, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.loginField, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.passwordField, new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.targetDropDown, new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //-------------------------------------------------------------------------------

        JLabel iconLabel = new JLabel(SqlConfigUtil.sqlserverBig);
        centerPanel.add(iconLabel, new GridBagConstraints(2, 2, 4, 4, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        ///==============================================================
        centerPanel.setBorder(new LineBorder(this.borderColor));
        this.add(centerPanel, BorderLayout.CENTER);

    }

    public SqlConfigItems pullFieldsFromGui() {
        return new SqlConfigItems(
                this.uuidField,
                this.dbNameField.getText(),
                this.loginField.getText(),
                this.passwordField.getText(),
                this.scriptFolderField.getText(),
                this.ipAddressField.getText(),
                this.portField.getText(),
                this.dbConnectionTypeField.getModel().getSelectedItem().toString(),
                getSelectedTarget()
        );
    }

    public void setFields(SqlConfigItems fields) {
        this.dbNameField.setText(fields.getDbNameField());
        this.loginField.setText(fields.getLoginField());
        this.passwordField.setText(fields.getPasswordField());
        if(Strings.isNullOrEmpty(fields.getScriptFolderField())){
            this.scriptFolderField.setText(DataStoreGroupConfigUtil.getLastUsedFileFolderSql());
        }else{
            this.scriptFolderField.setText(fields.getScriptFolderField());
        }

        this.ipAddressField.setText(fields.getIpAddressField());
        this.portField.setText(fields.getPort());
        this.uuidField = fields.getUniqueId();


        for (int i = 0; i < Target.values().length; i++) {
            Target t = targetDropDown.getItemAt(i);
            if (t.equals(fields.getTarget())) {
                targetDropDown.setSelectedIndex(i);
            }
        }

        this.targetDropDown.setSelectedItem(fields.getTarget());

        String connectionString = fields.getDbConnectionType();
        DBConnectionType userSaved = DBConnectionType.getEnum(connectionString);

        if (this.dbConnectionTypeField != null) {
            this.dbConnectionTypeField.getModel().setSelectedItem(userSaved.toString());
        } else {
            String[] options = this.buildDBConnectionTypes();
            this.dbConnectionTypeField = new JComboBox(options);
            this.dbConnectionTypeField.getModel().setSelectedItem(userSaved.toString());
        }



    }

    public Target getSelectedTarget() {
        int selected = this.targetDropDown.getSelectedIndex();
        if (selected >= 0) {
            return this.targetDropDown.getItemAt(selected);
        } else {
            return null;
        }
    }


    private String[] buildDBConnectionTypes() {
        ArrayList<String> list = new ArrayList<String>();

        for (DBConnectionType type : DBConnectionType.values()) {
            //Check if jdbc can work on this machine
            if (DBConnectionType.JDBC == type) {
                if (isJDBCEnabled()) {
                    list.add(type.toString());
                }
            } else {
                if (canAddConnectionType(type)) {
                    list.add(type.toString());
                }
            }
        }//end of for loop

        return list.toArray(new String[list.size()]);
    }

    private boolean canAddConnectionType(DBConnectionType type) {
        if (MiscUtil.isThisWindows() && type.supportsWindows()) {
            return true;
        }
        if (MiscUtil.isThisLinux() && type.supportsLinux()) {
            return true;
        }
        return false;
    }

    private boolean isJDBCEnabled() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return true;
        } catch (ClassNotFoundException e) {
            LOG.info("Cannot load JDBC driver... oops. Install a JDBC driver to use JDBC Runtime method");
        }
        return false;
    }

    private void initDBConnectionDropDown() {

        this.dbConnectionTypeField.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {

                // I shouldn't have had to write this code (disgusted)
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            Object item = e.getItem();
                            dbConnectionTypeField.getModel().setSelectedItem(item.toString());
                            for (UpdatePreferencesListener listener : updateListeners) {
                                listener.preferencesUpdated();
                            }
                        }
                    }
                });

            }
        });
    }

    public String getScriptFolder() {
        return scriptFolderField.getText();
    }


}
