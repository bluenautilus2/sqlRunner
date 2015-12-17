package org.bluenautilus.gui.cassServerConfiguration;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.cass.CassTarget;
import org.bluenautilus.cass.CassandraConnectionType;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.gui.FolderOpenButton;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.UUID;

/**
 * Created by bstevens on 2/8/15.
 */
public class CassConfigPanel extends JPanel {


    private static Log LOG = LogFactory.getLog(CassConfigPanel.class);

    CassConfigItems fields = null;

    private JTextField scriptFolderField = new JTextField(35);
    private JTextField hostNameField = new JTextField(20);
    private JTextField portField = new JTextField(20);
    private JTextField keyspaceField = new JTextField(20);
    private JTextField containerField = new JTextField(35);
    private JTextField loginField = new JTextField(25);
    private UUID uuidField = null;
    JLabel hostName = new JLabel("Cassandra Host Name");
    JLabel loginName = new JLabel("Host login");
    FolderOpenButton openScriptFolderButton = new FolderOpenButton(this, this.scriptFolderField);
    JComboBox<CassandraConnectionType> connectTypePulldown = new JComboBox<>();
    JComboBox<CassTarget> targetPulldown = new JComboBox<>();

    public CassConfigPanel(CassConfigItems initialFields) {
        super(new GridBagLayout());
        this.fields = initialFields;
        this.init();
    }

    private void init() {

        for (CassandraConnectionType type : CassandraConnectionType.values()) {
            if (type.worksInThisOS()) {
                connectTypePulldown.addItem(type);
            }
        }
        for (CassTarget target : CassTarget.values()) {
            targetPulldown.addItem(target);
        }

        this.setFields(this.fields);

        Color borderColor = new Color(180, 180, 180);
        JLabel folderName = new JLabel("CQL Script Folder");
        JLabel portName = new JLabel("Port");
        JLabel keyspaceName = new JLabel("Keyspace");
        JLabel connectionTypeName = new JLabel("Connection Type");
        JLabel containerName = new JLabel("Docker Container Name");
        JLabel targetName = new JLabel("Target");

        initConnectionDropDown();
        CassandraConnectionType type = (CassandraConnectionType) connectTypePulldown.getSelectedItem();
        syncHiddenFields(type);
        //always off for now
        portName.setEnabled(false);
        portField.setEnabled(false);
        targetName.setEnabled(false);
        targetPulldown.setEnabled(false);

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        //Center panel buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());

        //LABELS
        centerPanel.add(folderName, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(containerName, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        centerPanel.add(keyspaceName, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        centerPanel.add(connectionTypeName, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(hostName, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(loginName, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(targetName, new GridBagConstraints(0, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(portName, new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //FIELDS

        //this fills up three spots
        JPanel scriptHolder = new JPanel(new BorderLayout());
        scriptHolder.add(this.scriptFolderField, BorderLayout.WEST);
        scriptHolder.add(openScriptFolderButton, BorderLayout.EAST);

        centerPanel.add(scriptHolder, new GridBagConstraints(1, 0, 4, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.containerField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.keyspaceField, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.connectTypePulldown, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.hostNameField, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.loginField, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.targetPulldown, new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.portField, new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        JLabel iconLabel = new JLabel(CassConfigUtil.cassandraBig);
        centerPanel.add(iconLabel, new GridBagConstraints(2, 1, 3, 3, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.setBorder(new LineBorder(borderColor));

        this.add(centerPanel, new GridBagConstraints(1, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 4, 4, 4), 2, 2));
    }

    public CassConfigItems pullFieldsFromGui() {
        return new CassConfigItems(
                this.uuidField,
                this.scriptFolderField.getText(),
                this.hostNameField.getText(),
                this.portField.getText(),
                this.keyspaceField.getText(),
                this.connectTypePulldown.getSelectedItem().toString(),
                this.containerField.getText(),
                this.loginField.getText(),
                this.targetPulldown.getSelectedItem().toString());
    }

    public void setFields(CassConfigItems fields) {
        this.hostNameField.setText(fields.getHostField());
        this.uuidField = fields.getUniqueId();
        if (Strings.isNullOrEmpty(fields.getScriptFolderField())) {
            this.scriptFolderField.setText(DataStoreGroupConfigUtil.getLastUsedFileFolderCass());
        } else {
            this.scriptFolderField.setText(fields.getScriptFolderField());
        }

        CassandraConnectionType typeToUse = CassandraConnectionType.DOCKER_LOCAL;
        if (fields.getConnectionType() != null) {
            typeToUse = CassandraConnectionType.getEnum(fields.getConnectionType());
        }
        this.connectTypePulldown.setSelectedItem(typeToUse);

        CassTarget targetToUse = CassTarget.NONE;
        if (fields.getTarget() != null) {
            targetToUse = CassTarget.valueOf(fields.getTarget());
        }
        this.targetPulldown.setSelectedItem(targetToUse);

        this.portField.setText(fields.getPort());
        this.keyspaceField.setText(fields.getKeyspace());
        this.containerField.setText(fields.getContainer());
        this.loginField.setText(fields.getLogin());
    }

    private void syncHiddenFields(CassandraConnectionType type) {
        switch (type) {
            case DOCKER_REMOTE:
                setHost("Cassandra Host Name", null, true, true);
                break;
            case DOCKER_LOCAL:
                setHost("Cassandra Host Name", "localhost", false, false);
                break;
            case DOCKER_PLINK:
                setHost("Putty Session Name", null, true, false);
                break;
            case VIEW_ONLY:
                setHost("Cassandra Host Name", null, true, true);
        }
    }

    public void setHost(final String label, final String value, final boolean hostEnabled, final boolean loginEnabled) {
        if (value != null) {
            this.hostNameField.setText(value);
        }

        this.hostNameField.setEnabled(hostEnabled);
        this.hostName.setText(label);
        this.hostName.setEnabled(hostEnabled);
        loginField.setEnabled(loginEnabled);
        loginName.setEnabled(loginEnabled);
    }

    public String getScriptFolder() {
        return scriptFolderField.getText();
    }

    private void initConnectionDropDown() {
        this.connectTypePulldown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            Object o = connectTypePulldown.getSelectedItem();
                            if (o instanceof CassandraConnectionType) {
                                syncHiddenFields((CassandraConnectionType) o);
                            }
                        }
                    }
                });
            }
        });
    }


}
