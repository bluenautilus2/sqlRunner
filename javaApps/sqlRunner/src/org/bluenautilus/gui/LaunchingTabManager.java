package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bstevens on 2/16/15.
 */
public class LaunchingTabManager implements LaunchButtonListener {
    private static Log log = LogFactory.getLog(LaunchingTabManager.class);

    final private JPanel blankPanel = new JPanel();
    JTabbedPane tabbedPane = new JTabbedPane();
    RunButtonPanel buttonPanel = null;
    JPanel parentPanel = null;
    Map<String, TripletPanelMgr> panelManagers = new HashMap<>();

    public LaunchingTabManager(RunButtonPanel runButtons, JPanel parentPanel) {
        this.buttonPanel = runButtons;
        this.parentPanel = parentPanel;

        Dimension theSize = new Dimension(800, 500);
        blankPanel.setPreferredSize(theSize);
        tabbedPane.addTab("Chose a datastore group", blankPanel);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer index = tabbedPane.getSelectedIndex();
                if (index >= 0) {
                    Component selectedTab = tabbedPane.getTabComponentAt(index);
                    if (selectedTab != null) {
                        String selectedTrackingName = selectedTab.getName();
                        if (selectedTrackingName != null) {
                            handleTabSelectionEvent(selectedTrackingName);
                        }
                    }
                }
            }
        });
    }


    @Override
    public void launchButtonPressed(final DataStoreGroup groupToLaunch) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                tabbedPane.remove(blankPanel);
                List<UuidConfigItem> listOfDataStores = groupToLaunch.retrieveFullObjectsFromFiles();
                removeAllExisting();

                //now add the new stuff
                int i = 0;
                for (UuidConfigItem dataStore : listOfDataStores) {
                    TripletPanel panel = new TripletPanel(dataStore);

                    JButton closeButton = makeCloseButton();

                    JPanel titlePanel = new JPanel(new BorderLayout());
                    titlePanel.add(new JLabel(dataStore.toString() + "  "), BorderLayout.CENTER);
                    titlePanel.add(closeButton, BorderLayout.EAST);
                    String trackingName = Integer.toString(closeButton.hashCode());
                    titlePanel.setName(trackingName);

                    tabbedPane.addTab(null, panel);
                    tabbedPane.setTabComponentAt(i, titlePanel);

                    TripletPanelMgr theMgr = null;
                    if (dataStore instanceof SqlConfigItems) {
                        SqlConfigItems items = (SqlConfigItems) dataStore;
                        theMgr = new TripletPanelMgr(items, panel.getOutputPanel(), panel.getScriptViewPanel(), panel.getTableHolderPanel(), buttonPanel, parentPanel);
                    } else if (dataStore instanceof CassConfigItems) {
                        CassConfigItems items = (CassConfigItems) dataStore;
                        theMgr = new CassTripletPanelMgr(items, panel.getOutputPanel(), panel.getScriptViewPanel(), panel.getTableHolderPanel(), buttonPanel, parentPanel);
                    }
                    panelManagers.put(trackingName, theMgr);

                    i++;
                }

                //"press" the refresh button
                buttonPanel.actionPerformed(null);
            }
        });
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JButton makeCloseButton() {

        Image close = null;
        try {
            close = ImageIO.read(new File("small_x.png"));
        } catch (IOException ioe) {
            //who cares
            log.error(ioe);
        }

        ImageIcon closeIcon = new ImageIcon(close);
        JButton closeButton = new JButton(closeIcon);

        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        Dimension dim = new Dimension(12, 9);
        closeButton.setPreferredSize(dim);
        closeButton.setMaximumSize(dim);

        closeButton.addActionListener(new CloseButtonActionListener());
        return closeButton;
    }


    class CloseButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            JButton btn = (JButton) ae.getSource();
            String trackingName = Integer.toString(btn.hashCode());
            closePanelAndCleanUp(trackingName);
        }
    }

    private void closePanelAndCleanUp(String trackingName) {
        int index = -1;

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component tab = tabbedPane.getTabComponentAt(i);

            if (tab.getName().equals(trackingName)) {
                index = i;
            }
        }

        //if found, remove
        if (index != -1) {
            tabbedPane.remove(index);
        }
        //tell manager to stop functioning
        TripletPanelMgr mgr = panelManagers.get(trackingName);
        if (mgr != null) {
            mgr.stopListening();
        }
        //remove manager
        panelManagers.remove(trackingName);
    }

    private void removeAllExisting() {

        Set<String> names = panelManagers.keySet();
        //make a copy, because we are modifying the keyset
        //with the closePanelAndCleanUp method
        Set<String> cloneOfNames = new HashSet<>();
        cloneOfNames.addAll(names);
        for (String trackingName : cloneOfNames) {
            closePanelAndCleanUp(trackingName);
        }
    }

    private void handleTabSelectionEvent(String selectedTrackingName) {
        TripletPanelMgr selectedMgr = panelManagers.get(selectedTrackingName);
        selectedMgr.tabWasSelected();

        for (String trackingName : panelManagers.keySet()) {
            if (!trackingName.equals(selectedTrackingName)) {
                panelManagers.get(trackingName).tabWasDeselected();
            }
        }
    }

}
