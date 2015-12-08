package org.bluenautilus.gui.sqlServerConfiguration;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.db.SqlTarget;
import org.bluenautilus.gui.FolderOpenButton;
import org.bluenautilus.gui.RunButtonPanel;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bstevens on 2/8/15.
 */
public class SqlConfigPanel extends JPanel {

    private static Log LOG = LogFactory.getLog(RunButtonPanel.class);

    SqlConfigItems fields = null;
    private JTextField dbNameField = new JTextField(30);
    private JTextField loginField = new JTextField(8);
    private JTextField passwordField = new JPasswordField(8);
    private JTextField scriptFolderField = new JTextField(35);
    private JTextField ipAddressField = new JTextField(15);
    private JTextField portField = new JTextField(8);
    private JComboBox<DBConnectionType> dbConnectionTypeField = new JComboBox<>();
    private UUID uuidField = null;
    private JComboBox<SqlTarget> targetDropDown = new JComboBox<>();

    private Color borderColor = new Color(180, 180, 180);

    public SqlConfigPanel(SqlConfigItems initialFields) {
        super(new GridBagLayout());
        this.fields = initialFields;
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        for (SqlTarget t : SqlTarget.values()) {
            this.targetDropDown.addItem(t);
        }

        for(DBConnectionType type :this.buildDBConnectionTypes()){
            this.dbConnectionTypeField.addItem(type);
        }

        this.setFields(this.fields);

        JLabel dbNameLabel = new JLabel("Database Name");
        JLabel ipAddress = new JLabel("IP Address/Host Name");
        JLabel userName = new JLabel("Login");
        JLabel password = new JLabel("Password");
        JLabel folderName = new JLabel("SQL Script Folder");
        JLabel portLabel = new JLabel("Port");
        JLabel dbConnectionTypeLabel = new JLabel("DB Connection Method");
        JLabel targetLabel = new JLabel("SqlTarget");
        FolderOpenButton openScriptFolderButton = new FolderOpenButton(this, this.scriptFolderField);


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


        for (int i = 0; i < SqlTarget.values().length; i++) {
            SqlTarget t = targetDropDown.getItemAt(i);
            if (t.equals(fields.getTarget())) {
                targetDropDown.setSelectedIndex(i);
            }
        }

        this.targetDropDown.setSelectedItem(fields.getTarget());

        String connectionString = fields.getDbConnectionType();
        DBConnectionType userSaved = DBConnectionType.getEnum(connectionString);

        this.dbConnectionTypeField.getModel().setSelectedItem(userSaved);

    }

    public SqlTarget getSelectedTarget() {
        int selected = this.targetDropDown.getSelectedIndex();
        if (selected >= 0) {
            return this.targetDropDown.getItemAt(selected);
        } else {
            return null;
        }
    }


    private ArrayList<DBConnectionType> buildDBConnectionTypes() {
        ArrayList<DBConnectionType> list = new ArrayList<>();

        for (DBConnectionType type : DBConnectionType.values()) {
            //Check if jdbc can work on this machine
            if (DBConnectionType.JDBC == type) {
                if (isJDBCEnabled()) {
                    list.add(type);
                }
            } else {
                if (type.worksInThisOS()) {
                    list.add(type);
                }
            }
        }//end of for loop

        return list;
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

    public String getScriptFolder() {
        return scriptFolderField.getText();
    }

}
