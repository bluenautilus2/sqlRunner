package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.db.DataLanguage;
import org.bluenautilus.db.ExecutionPlan;
import org.bluenautilus.db.SqlTarget;
import org.bluenautilus.script.SqlScriptCreator;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by bstevens on 2/8/15.
 */
public class NewScriptPanel extends JPanel {

    private static Log LOG = LogFactory.getLog(RunButtonPanel.class);

    private JTextField scriptFolderField = new JTextField(35);
    private JComboBox<ExecutionPlan> executionPlanDropDown = new JComboBox<>();
    private JComboBox<DataLanguage> dataLanguageDropDown = new JComboBox<>();
    private JComboBox<SqlTarget> targetDropDown = new JComboBox<>();
    private JComboBox<String> thursdaysDropDown = new JComboBox<>();
    private JTextField authorNameField = new JTextField(25);
    private JTextField authorEmailField = new JTextField(30);
    private JTextField jiraDescriptionField = new JTextField(40);
    private JLabel dataLanguageExplanation = new JLabel("");

    private Color borderColor = new Color(180, 180, 180);

    public NewScriptPanel() {
        super(new GridBagLayout());
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        for (SqlTarget t : SqlTarget.values()) {
            this.targetDropDown.addItem(t);
        }

        for (ExecutionPlan executionPlan : ExecutionPlan.values()) {
            this.executionPlanDropDown.addItem(executionPlan);
        }

        for (DataLanguage dataLanguage : DataLanguage.values()) {
            this.dataLanguageDropDown.addItem(dataLanguage);
        }

        for (String thursday : SqlScriptCreator.theNextTenThursdays()) {
            this.thursdaysDropDown.addItem(thursday);
        }

        this.scriptFolderField.setText(DataStoreGroupConfigUtil.getLastUsedFileFolderSql());
        this.authorNameField.setText(DataStoreGroupConfigUtil.getLastUsedAuthorName());
        this.authorEmailField.setText(DataStoreGroupConfigUtil.getLastUsedAuthorEmail());
        this.jiraDescriptionField.setText("");
        this.dataLanguageExplanation.setForeground(new Color(150,150,150));

        this.dataLanguageDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer currentlySelected = dataLanguageDropDown.getSelectedIndex();
                DataLanguage current = dataLanguageDropDown.getItemAt(currentlySelected);
                dataLanguageExplanation.setText(current.getExplanation());
            }
        });

        JLabel folderName = new JLabel("SQL Script Folder");
        JLabel targetLabel = new JLabel("Target");

        JLabel executionPlanLabel = new JLabel("Execution Plan");
        JLabel dataLanguageLabel = new JLabel("Data Language");
        JLabel authorNameLabel = new JLabel("Author Name");
        JLabel authorEmailLabel = new JLabel("Author Email");
        JLabel jiraDescriptionLabel = new JLabel("Jira ID and description");
        JLabel deliveryDateLabel = new JLabel("Delivery Date");


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

        centerPanel.add(dataLanguageLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(targetLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(executionPlanLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(deliveryDateLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(jiraDescriptionLabel, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(authorNameLabel, new GridBagConstraints(0, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(authorEmailLabel, new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0,
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

        JPanel explanationPanel = new JPanel(new BorderLayout());
        explanationPanel.add(this.dataLanguageDropDown, BorderLayout.WEST);
        explanationPanel.add(this.dataLanguageExplanation, BorderLayout.EAST);
        centerPanel.add(explanationPanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.targetDropDown, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.executionPlanDropDown, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.thursdaysDropDown, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.jiraDescriptionField, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.authorNameField, new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        centerPanel.add(this.authorEmailField, new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 2, 2));

        ///==============================================================
        centerPanel.setBorder(new LineBorder(this.borderColor));
        this.add(centerPanel, BorderLayout.CENTER);
    }


    public SqlTarget getSelectedTarget() {
        int selected = this.targetDropDown.getSelectedIndex();
        if (selected >= 0) {
            return this.targetDropDown.getItemAt(selected);
        } else {
            return null;
        }
    }

    public ExecutionPlan getExecutionPlan() {
        int selected = this.executionPlanDropDown.getSelectedIndex();
        if (selected >= 0) {
            return this.executionPlanDropDown.getItemAt(selected);
        } else {
            return null;
        }
    }

    public DataLanguage getDataLanguage() {
        int selected = this.dataLanguageDropDown.getSelectedIndex();
        if (selected >= 0) {
            return this.dataLanguageDropDown.getItemAt(selected);
        } else {
            return null;
        }
    }

    public String getDeliveryDate() {
        int selected = this.thursdaysDropDown.getSelectedIndex();
        if (selected >= 0) {
            return this.thursdaysDropDown.getItemAt(selected);
        } else {
            return null;
        }
    }

    public String getScriptFolder() {
        return scriptFolderField.getText();
    }

    public String getAuthorName() {
        return authorNameField.getText();
    }

    public String getAuthorEmail() {
        return authorEmailField.getText();
    }

    public String getJiraDescription() {
        return jiraDescriptionField.getText();
    }


}
