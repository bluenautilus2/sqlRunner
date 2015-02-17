package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.SqlConfigItems;
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
public class RunButtonPanel extends JPanel {

    private static Log LOG = LogFactory.getLog(RunButtonPanel.class);


    private JButton refreshButton = new JButton("REFRESH");
    private JButton selectedScriptButton = new JButton("Run Selected");
    private JButton runAllButton = new JButton("Run All");
    private JButton rollbackButton = new JButton("Rollback Selected");
    private Color defaultForeground;
    private Color defaultBackground;
    private Color borderColor = new Color(180, 180, 180);

    ArrayList<UpdatePreferencesListener> updateListeners = new ArrayList<UpdatePreferencesListener>();

    public RunButtonPanel() {
        super(new GridBagLayout());
        this.init();
    }

    private void init() {

        this.refreshButton.setToolTipText("Re-scans File Directory and Database");
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
                new Insets(4, 4, 4, 4), 2, 2));

        leftCornerPanel.add(this.selectedScriptButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));

        leftCornerPanel.add(this.rollbackButton, new GridBagConstraints(2,0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));

        leftCornerPanel.setBorder(new LineBorder(this.borderColor));

        JPanel  refreshPanel = new JPanel(new GridBagLayout());
        refreshPanel.setBorder(new LineBorder(this.borderColor));
        refreshPanel.add(this.refreshButton, new GridBagConstraints(0,0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));
        //Refresh Button


        this.add(leftCornerPanel, new GridBagConstraints(0, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10,10,10,10), 2, 2));

        this.add(refreshPanel, new GridBagConstraints(1, 0, 6, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 10), 2, 2));


    }

    public void addUpdatePreferencesListener(UpdatePreferencesListener listener) {
        this.updateListeners.add(listener);
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


    public SqlConfigItems pullFieldsFromGui(){
        return null;
    }

}
