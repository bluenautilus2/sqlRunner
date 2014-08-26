package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.cass.CassandraConnectionType;
import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.script.ScriptKickoffListener;
import org.bluenautilus.script.ScriptType;
import org.bluenautilus.util.MiscUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;


/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 10:33 PM
 */
public class CassButtonPanel extends JPanel {

    private static Log LOG = LogFactory.getLog(CassButtonPanel.class);

    CassFieldItems fields = null;

    private JTextField scriptFolderField = new JTextField(35);
    private JTextField hostNameField = new JTextField(20);
    private JCheckBox useCert = new JCheckBox();
    private JTextField certFileField = new JTextField(35);

    private JButton refreshButton = new JButton("REFRESH");
    private JButton selectedScriptButton = new JButton("Run Selected");
    private JButton runAllButton = new JButton("Run All");
    private JButton rollbackButton = new JButton("Rollback Selected");
    private Color defaultForeground;
    private Color defaultBackground;
    private Color borderColor = new Color(180, 180, 180);
    private final JLabel certName = new JLabel("Certificate file");

    ArrayList<UpdatePreferencesListener> updateListeners = new ArrayList<UpdatePreferencesListener>();

    public CassButtonPanel(CassFieldItems initialFields) {
        super(new GridBagLayout());

        this.fields = initialFields;
        this.init();

    }

    private void init() {

        this.setFields(this.fields);


        JLabel hostName = new JLabel("Cassandra Host Name");
        JLabel folderName = new JLabel("CQL Script Folder");
        JLabel checkBoxName = new JLabel("Use Cert?");
        FolderOpenButton openScriptFolderButton = new FolderOpenButton(this,this.scriptFolderField);
        FileOpenButton openCertFileButton = new FileOpenButton(this,this.certFileField);

        this.refreshButton.setToolTipText("Rescans File Directory and Database");
        this.selectedScriptButton.setToolTipText("Run only the script(s) that are selected");
        this.runAllButton.setToolTipText("Runs all scripts showing as \'Need to Run\'");
        this.rollbackButton.setToolTipText("Runs Rollback Script for Selected rows");
        this.useCert.setToolTipText("Check if you are running the scripts on an AltoStratum");

        ActionListener ghostCertListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              syncCheckBoxDisabling();
            }
        };
        useCert.addActionListener(ghostCertListener);
       this.syncCheckBoxDisabling();

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady


        //Corner Buttons
        JPanel leftCornerPanel = new JPanel(new GridBagLayout());

        leftCornerPanel.add(this.runAllButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 4, 4, 4), 2, 2));

        leftCornerPanel.add(this.selectedScriptButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));

        leftCornerPanel.add(this.rollbackButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 10, 4), 2, 2));

        leftCornerPanel.setBorder(new LineBorder(this.borderColor));

        //Center panel buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());

        //Refresh Button
        centerPanel.add(this.refreshButton, new GridBagConstraints(0, 4, 6, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 2, 2), 20, 2));

        //LABELS
         centerPanel.add(hostName, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));



        centerPanel.add(folderName, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(checkBoxName, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        centerPanel.add(certName, new GridBagConstraints(0,3, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //FIELDS
        centerPanel.add(this.hostNameField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //this fills up three spots
        centerPanel.add(this.scriptFolderField, new GridBagConstraints(1, 0, 3, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));
        centerPanel.add(openScriptFolderButton, new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        centerPanel.add(this.useCert, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //this fills up three spots
        centerPanel.add(this.certFileField, new GridBagConstraints(1, 3, 3, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));
        centerPanel.add(openCertFileButton, new GridBagConstraints(4, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));
        centerPanel.setBorder(new LineBorder(this.borderColor));

        this.add(leftCornerPanel, new GridBagConstraints(0, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 4, 4, 4), 2, 2));

        this.add(centerPanel, new GridBagConstraints(1, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 4, 4, 4), 2, 2));

    }

    public void addUpdatePreferencesListener(UpdatePreferencesListener listener) {
        this.updateListeners.add(listener);
    }

    public CassFieldItems pullFieldsFromGui() {
        return new CassFieldItems(
                this.scriptFolderField.getText(),
                this.hostNameField.getText(),
                getStringForConfigCheckbox(this.useCert),
                this.certFileField.getText());

    }

    private String getStringForConfigCheckbox(JCheckBox box){
        if(box.isSelected()){
            return "true";
        }
        return "false";
    }


    public void setFields(CassFieldItems fields) {

        this.scriptFolderField.setText(fields.getScriptFolderField());
        this.hostNameField.setText(fields.getHostField());
        this.useCert.setSelected(new Boolean(fields.getUseCertificate()));
        this.certFileField.setText(fields.getCertificateFileField());
        syncCheckBoxDisabling();
    }

    public void addRefreshListener(final RefreshListener listener) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRefreshButtonRed();
                listener.refreshAction();
            }
        };
        this.refreshButton.addActionListener(actionListener);
    }

    public void addScriptKickoffListener(final ScriptKickoffListener listener) {
        ActionListener regularActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.kickoffSelectedScripts(ScriptType.REGULAR);
            }
        };

        ActionListener rollbackActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.kickoffSelectedScripts(ScriptType.ROLLBACK);
            }
        };

        this.selectedScriptButton.addActionListener(regularActionListener);
        this.rollbackButton.addActionListener(rollbackActionListener);
    }

    public CassandraConnectionType getCassConnectionType() {
       return CassandraConnectionType.SSH;
    }

    public void addScriptRunAllToRunListener(final ScriptKickoffListener listener) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.kickoffAllToRunScripts();
            }
        };
        this.runAllButton.addActionListener(actionListener);

    }

    private void setRefreshButtonRed() {
        this.defaultBackground = this.refreshButton.getBackground();
        this.defaultForeground = this.refreshButton.getForeground();

        this.refreshButton.setText("Refreshing");
        this.refreshButton.setForeground(Color.WHITE);
        this.refreshButton.setBackground(new Color(150, 60, 60));

    }

    public void setRefreshButtonNormal() {
        this.refreshButton.setText("Refresh");
        this.refreshButton.setForeground(this.defaultForeground);
        this.refreshButton.setBackground(this.defaultBackground);
    }



    private void syncCheckBoxDisabling(){
        certFileField.setEnabled(useCert.isSelected());
        certName.setEnabled(useCert.isSelected());
    }
}
