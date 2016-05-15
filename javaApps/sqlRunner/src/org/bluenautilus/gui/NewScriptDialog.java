package org.bluenautilus.gui;

import org.bluenautilus.script.SqlScriptCreator;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.GuiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by bstevens on 5/15/16.
 */
public class NewScriptDialog extends JFrame {

    NewScriptPanel newScriptPanel;
    JButton saveButton = new JButton("Create New File");
    JButton cancelButton = new JButton("Cancel");
    final JFrame selfReference = this;

    public NewScriptDialog(String title, final RefreshListener refreshListener) {
        super(title);
        this.newScriptPanel = new NewScriptPanel();

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selfReference.dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataStoreGroupConfigUtil.updateLastUsedAuthorName(newScriptPanel.getAuthorName());
                DataStoreGroupConfigUtil.updateLastUsedAuthorEmail(newScriptPanel.getAuthorEmail());
                SqlScriptCreator creator = new SqlScriptCreator(newScriptPanel.getDeliveryDate(), newScriptPanel.getJiraDescription(), newScriptPanel.getDataLanguage(),
                        newScriptPanel.getSelectedTarget(), newScriptPanel.getExecutionPlan(), newScriptPanel.getAuthorName(), newScriptPanel.getAuthorEmail(),
                        newScriptPanel.getScriptFolder());
                try {
                    creator.outputScript();
                    creator.outputRollback();
                } catch (IOException ioe) {
                    GuiUtil.showErrorModalDialog(ioe, newScriptPanel);
                }
                //refresh the triplets so they show the new file.
                refreshListener.refreshAction();
                //close out this JFrame
                selfReference.dispose();
            }
        });


        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.add(newScriptPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 2, 2));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(cancelButton, BorderLayout.WEST);
        buttonPanel.add(saveButton, BorderLayout.EAST);
        outerPanel.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));
        this.setContentPane(outerPanel);
        this.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE);
    }

}
