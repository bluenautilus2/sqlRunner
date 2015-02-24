package org.bluenautilus.gui.cassServerConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.gui.FileOpenButton;
import org.bluenautilus.gui.FolderOpenButton;
import org.bluenautilus.gui.UpdatePreferencesListener;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.MiscUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bstevens on 2/8/15.
 */
public class CassConfigPanel extends JPanel {


    private static Log LOG = LogFactory.getLog(CassConfigPanel.class);

    CassConfigItems fields = null;

    private JTextField scriptFolderField = new JTextField(35);
    private JTextField hostNameField = new JTextField(20);
    private JCheckBox useCert = new JCheckBox();
    private JTextField certFileField = new JTextField(35);
    private UUID uuidField = null;

    private Color borderColor = new Color(180, 180, 180);
    private final JLabel certName = new JLabel("Certificate file");
    private JLabel folderName = new JLabel("CQL Script Folder");
    private JLabel checkBoxName = new JLabel("Use Cert?");

    ArrayList<UpdatePreferencesListener> updateListeners = new ArrayList<UpdatePreferencesListener>();

    public CassConfigPanel(CassConfigItems initialFields) {
        super(new GridBagLayout());

        this.fields = initialFields;
        this.init();
    }

    private void init() {

        this.setFields(this.fields);
        JLabel hostName;
        if (MiscUtil.isThisLinux()) {
            hostName = new JLabel("Cassandra Host Name");
        } else {
            hostName = new JLabel("Putty Saved Session Name");
        }


        FolderOpenButton openScriptFolderButton = new FolderOpenButton(this, this.scriptFolderField);
        FileOpenButton openCertFileButton = new FileOpenButton(this, this.certFileField);

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

        //Center panel buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());

        //LABELS
        centerPanel.add(folderName, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(hostName, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        //FIELDS

        //this fills up three spots
        JPanel scriptHolder = new JPanel(new BorderLayout());
        scriptHolder.add(this.scriptFolderField, BorderLayout.WEST);
        scriptHolder.add(openScriptFolderButton, BorderLayout.EAST);
        centerPanel.add(scriptHolder, new GridBagConstraints(1, 0, 3, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.hostNameField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        if (MiscUtil.isThisLinux()) {

            centerPanel.add(certName, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(2, 2, 2, 2), 2, 2));

            centerPanel.add(checkBoxName, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(2, 2, 2, 2), 2, 2));

            centerPanel.add(this.useCert, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 2, 2), 2, 2));

            //this fills up three spots
            JPanel certFileHolder = new JPanel(new BorderLayout());
            certFileHolder.add(this.certFileField, BorderLayout.WEST);
            certFileHolder.add(openCertFileButton, BorderLayout.EAST);
            centerPanel.add(certFileHolder, new GridBagConstraints(1, 4, 3, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 2, 2), 2, 2));
        }

        JLabel iconLabel = new JLabel(CassConfigUtil.cassandraBig);
        centerPanel.add(iconLabel, new GridBagConstraints(2, 2, 2, 2, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));


        centerPanel.setBorder(new LineBorder(this.borderColor));

        this.add(centerPanel, new GridBagConstraints(1, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 4, 4, 4), 2, 2));

    }

    public CassConfigItems pullFieldsFromGui() {
        return new CassConfigItems(
                this.uuidField,
                this.scriptFolderField.getText(),
                this.hostNameField.getText(),
                getStringForConfigCheckbox(this.useCert),
                this.certFileField.getText());
    }

    private String getStringForConfigCheckbox(JCheckBox box) {
        if (box.isSelected()) {
            return "true";
        }
        return "false";
    }

    public void setFields(CassConfigItems fields) {

        this.scriptFolderField.setText(fields.getScriptFolderField());
        this.hostNameField.setText(fields.getHostField());
        this.useCert.setSelected(new Boolean(fields.getUseCertificate()));
        this.certFileField.setText(fields.getCertificateFileField());
        this.uuidField = fields.getUniqueId();
        syncCheckBoxDisabling();
    }

    private void syncCheckBoxDisabling() {
        certFileField.setEnabled(useCert.isSelected());
        certName.setEnabled(useCert.isSelected());
    }

    public String getScriptFolder() {
        return scriptFolderField.getText();
    }

}
