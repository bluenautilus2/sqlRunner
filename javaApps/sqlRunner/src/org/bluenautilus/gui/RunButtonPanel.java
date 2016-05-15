package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.script.ScriptKickoffListener;
import org.bluenautilus.script.ScriptType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 10:33 PM
 */
public class RunButtonPanel extends JPanel implements ActionListener, RefreshListener {

    private static Log LOG = LogFactory.getLog(RunButtonPanel.class);

    private JButton refreshButton = new JButton("REFRESH");
    private JButton selectedScriptButton = new JButton("Run Selected");
    private JButton newScriptButton = new JButton("New Script");
    private JButton rollbackButton = new JButton("Rollback Selected");
    private Color defaultForeground;
    private Color defaultBackground;
    private Color borderColor = new Color(180, 180, 180);
    private volatile Integer outstandingRefreshActions = 0;

    private static final String REFRESH = "Refresh";
    private static final String REFRESHING = "Refreshing";

    ArrayList<RefreshListener> refreshListeners = new ArrayList<>();


    public RunButtonPanel() {
        super(new GridBagLayout());
        this.init();
    }

    private void init() {
        this.refreshButton.addActionListener(this);
        this.refreshButton.setToolTipText("Re-scans File Directory and Database");
        this.defaultBackground = this.refreshButton.getBackground();
        this.defaultForeground = this.refreshButton.getForeground();

        this.newScriptButton.setToolTipText("Generate a new script");
        this.newScriptButton.setBackground(new Color(212, 237, 224));
        this.newScriptButton.setForeground(new Color(39, 69, 69));
        this.newScriptButton.addActionListener(this.makeNewScriptListener(this));
        this.selectedScriptButton.setToolTipText("Run only the script(s) that are selected");
        this.rollbackButton.setToolTipText("Runs Rollback Script for Selected rows");

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        //Corner Buttons
        JPanel leftCornerPanel = new JPanel(new GridBagLayout());

        //someday this will become the "run one week at a time" button
        //Don't remove it, there's a lot of framework in place to handle
        //the events it generates.
        leftCornerPanel.add(this.newScriptButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));

        leftCornerPanel.add(this.selectedScriptButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));

        leftCornerPanel.add(this.rollbackButton, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));

        leftCornerPanel.setBorder(new LineBorder(this.borderColor));

        JPanel refreshPanel = new JPanel(new GridBagLayout());
        refreshPanel.setBorder(new LineBorder(this.borderColor));
        refreshPanel.add(this.refreshButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 4, 4, 4), 2, 2));
        //Refresh Button


        this.add(leftCornerPanel, new GridBagConstraints(0, 0, 1, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 10), 2, 2));

        this.add(refreshPanel, new GridBagConstraints(1, 0, 6, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 10), 2, 2));


    }

    public void addRefreshListener(final RefreshListener listener) {
        this.refreshListeners.add(listener);
    }

    public void removeRefreshListener(final RefreshListener listener) {
        this.refreshListeners.remove(listener);
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

    //The ability to run all scripts has been disabled for the time being.

  /*  public void addScriptRunAllToRunListener(final ScriptKickoffListener listener) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.kickoffAllToRunScripts();
            }
        };
        this.newScriptButton.addActionListener(actionListener);

    }  */

    private void setRefreshButtonRed(int count) {
        if (count > 0) {
            this.refreshButton.setText(REFRESHING + ": " + count);
            this.refreshButton.setForeground(Color.WHITE);
            this.refreshButton.setBackground(new Color(150, 60, 60));
        }
    }

    private void setRefreshButtonNormal() {
        this.refreshButton.setText(REFRESH);
        this.refreshButton.setForeground(this.defaultForeground);
        this.refreshButton.setBackground(this.defaultBackground);
    }

    /**
     * this is used when someone hits the "refresh" button
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //the button has been pressed
        //how many refresh listeners?
        this.outstandingRefreshActions = refreshListeners.size();
        setRefreshButtonRed(this.outstandingRefreshActions);
        for (RefreshListener refreshListener : refreshListeners) {
            refreshListener.refreshAction();
        }
    }

    public synchronized void aSingleRefreshCompleted() {
        this.outstandingRefreshActions--;
        if (this.outstandingRefreshActions == 0) {
            this.setRefreshButtonNormal();
        } else {
            this.setRefreshButtonRed(this.outstandingRefreshActions);
        }
    }

    @Override
    public void refreshAction() {
        actionPerformed(null);
    }

    public ActionListener makeNewScriptListener(final RefreshListener refreshListener) {
        ActionListener newListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewScriptDialog newScriptDialog = new NewScriptDialog("Create new DB Script", refreshListener);
                newScriptDialog.pack();
                newScriptDialog.setVisible(true);
            }
        };
        return newListener;
    }
}
