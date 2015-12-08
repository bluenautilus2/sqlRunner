package org.bluenautilus.gui.cassServerConfiguration;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.cass.CassandraConnectionType;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.gui.FolderOpenButton;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.UUID;

/**
 * Created by bstevens on 2/8/15.
 */
public class CassConfigPanel extends JPanel {


    private static Log LOG = LogFactory.getLog(CassConfigPanel.class);

    CassConfigItems fields = null;

    private JTextField scriptFolderField = new JTextField(35);
    private JTextField hostNameField = new JTextField(20);
    private JTextField portField = new JTextField(10);
    private JTextField keyspaceField = new JTextField(10);
    private UUID uuidField = null;
    FolderOpenButton openScriptFolderButton = new FolderOpenButton(this, this.scriptFolderField);
    JComboBox<CassandraConnectionType> connectTypePulldown = new JComboBox<>();

    public CassConfigPanel(CassConfigItems initialFields) {
        super(new GridBagLayout());
        this.fields = initialFields;
        this.init();
    }

    private void init() {

        for(CassandraConnectionType type: CassandraConnectionType.values()){
            if(type.worksInThisOS()){
                connectTypePulldown.addItem(type);
            }
        }

        this.setFields(this.fields);

        Color borderColor = new Color(180, 180, 180);
        JLabel folderName = new JLabel("CQL Script Folder");
        JLabel portName = new JLabel("Port");
        JLabel keyspaceName = new JLabel("Keyspace");
        JLabel hostName = new JLabel("Cassandra Host Name");
        JLabel connectionTypeName = new JLabel("Connection Type");

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

        centerPanel.add(hostName, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(keyspaceName, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(portName, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
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

        centerPanel.add(this.hostNameField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.keyspaceField, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.portField, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
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
                this.connectTypePulldown.getSelectedItem().toString());
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
        if(fields.getConnectionType() !=null){
            typeToUse = CassandraConnectionType.getEnum(fields.getConnectionType());
        }

        this.connectTypePulldown.setSelectedItem(typeToUse);
        this.portField.setText(fields.getPort());
        this.keyspaceField.setText(fields.getKeyspace());
    }


    public String getScriptFolder() {
        return scriptFolderField.getText();
    }

}
