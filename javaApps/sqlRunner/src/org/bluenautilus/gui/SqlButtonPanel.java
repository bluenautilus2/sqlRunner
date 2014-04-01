package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.script.ScriptKickoffListener;
import org.bluenautilus.script.ScriptType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 10:33 PM
 */
public class SqlButtonPanel extends JPanel {

	private static Log LOG = LogFactory.getLog(SqlButtonPanel.class);

    FieldItems fields = null;
    private JTextField dbNameField = new JTextField(15);
    private JTextField loginField = new JTextField(8);
    private JTextField passwordField = new JPasswordField(8);
    private JTextField scriptFolderField = new JTextField(35);
    private JTextField ipAddressField = new JTextField(15);
    private JTextField portField = new JTextField(8);
	private JComboBox<DBConnectionType> dbConnectionTypeField = new JComboBox<>(DBConnectionType.values());
    private JButton refreshButton = new JButton("REFRESH");
    private JButton selectedScriptButton = new JButton("Run Selected");
    private JButton runAllButton = new JButton("Run All");
    private JButton rollbackButton = new JButton("Rollback Selected");
    private Color defaultForeground;
    private Color defaultBackground;
    private Color borderColor =  new Color(180,180,180);
	private boolean jdbcEnabled = false;
	private boolean osqlEnabled = false;
	private boolean sqlCmdEnabled = false;

    public SqlButtonPanel(FieldItems initialFields) {
        super(new GridBagLayout());

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			jdbcEnabled = true;
		} catch (ClassNotFoundException e) {
			LOG.info("Cannot load JDBC driver... oops.");
		}

		// TODO: find some way of deciding if osql is set up on the machine.
		osqlEnabled = true;

		// TODO: find some way of deciding if sqlCmd is present on the machine.
		sqlCmdEnabled = false;

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
        JLabel portLabel = new JLabel("Port");
		JLabel dbConnectionTypeLabel = new JLabel("DB Connection Method");

        this.refreshButton.setToolTipText("Rescans File Directory and Database");
        this.selectedScriptButton.setToolTipText("Run only the script(s) that are selected");
        this.runAllButton.setToolTipText("Runs all scripts showing as \'Need to Run\'");
        this.rollbackButton.setToolTipText("Runs Rollback Script for Selected rows");

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
        centerPanel.add(dbNameLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(ipAddress, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(userName, new GridBagConstraints(4, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(password, new GridBagConstraints(4, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(folderName, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(portLabel, new GridBagConstraints(4,0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

		centerPanel.add(dbConnectionTypeLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 2, 2));

        //TEXT FIELDS
        centerPanel.add(this.dbNameField, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.ipAddressField, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.loginField, new GridBagConstraints(5, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.passwordField, new GridBagConstraints(5, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.portField, new GridBagConstraints(5, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

		centerPanel.add(this.dbConnectionTypeField, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 2, 2));

        //this fills up three spots
        centerPanel.add(this.scriptFolderField, new GridBagConstraints(1, 0, 3, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.setBorder(new LineBorder(this.borderColor));

        this.add(leftCornerPanel, new GridBagConstraints(0, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 4, 4, 4), 2, 2));

        this.add(centerPanel, new GridBagConstraints(1, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(4, 4, 4, 4), 2, 2));


		dbConnectionTypeField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateSelection();
			}
		});
		validateSelection();
    }

	private void validateSelection() {
		boolean enableButtons = getSelectedDBConnectionType() != DBConnectionType.NONE;
		selectedScriptButton.setEnabled(enableButtons);
		runAllButton.setEnabled(enableButtons);
		rollbackButton.setEnabled(enableButtons);

		switch (getSelectedDBConnectionType()) {
			case JDBC:
				if (!jdbcEnabled) {
					JOptionPane.showMessageDialog(null, "No JDBC driver was found, this is disabled.");
					dbConnectionTypeField.setSelectedItem(DBConnectionType.NONE);
				}
				break;
			case OSQL:
				if (!osqlEnabled) {
					JOptionPane.showMessageDialog(null, "OSQL is disabled.");
					dbConnectionTypeField.setSelectedItem(DBConnectionType.NONE);
				}
				break;
			case SQL_CMD:
				if (!sqlCmdEnabled) {
					JOptionPane.showMessageDialog(null, "SQL CMD is disabled.");
					dbConnectionTypeField.setSelectedItem(DBConnectionType.NONE);
				}
				break;
		}
	}

    public FieldItems pullFieldsFromGui() {
        return new FieldItems(
                this.dbNameField.getText(),
                this.loginField.getText(),
                this.passwordField.getText(),
                this.scriptFolderField.getText(),
                this.ipAddressField.getText(),
                this.portField.getText());
    }

    public void setFields(FieldItems fields) {
        this.dbNameField.setText(fields.getDbNameField());
        this.loginField.setText(fields.getLoginField());
        this.passwordField.setText(fields.getPasswordField());
        this.scriptFolderField.setText(fields.getScriptFolderField());
        this.ipAddressField.setText(fields.getIpAddressField());
        this.portField.setText(fields.getPort());
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

	public DBConnectionType getSelectedDBConnectionType() {
		return (DBConnectionType)dbConnectionTypeField.getSelectedItem();
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

    private void setRefreshButtonRed(){
        this.defaultBackground = this.refreshButton.getBackground();
        this.defaultForeground= this.refreshButton.getForeground();

        this.refreshButton.setText("Refreshing");
        this.refreshButton.setForeground(Color.WHITE);
        this.refreshButton.setBackground(new Color(150,60,60));

    }

    public void setRefreshButtonNormal(){
        this.refreshButton.setText("Refresh");
        this.refreshButton.setForeground(this.defaultForeground);
        this.refreshButton.setBackground(this.defaultBackground);
    }

}
