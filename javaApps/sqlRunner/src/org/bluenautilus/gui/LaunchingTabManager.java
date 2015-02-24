package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by bstevens on 2/16/15.
 */
public class LaunchingTabManager implements LaunchButtonListener {
    private static Log log = LogFactory.getLog(LaunchingTabManager.class);

    JTabbedPane tabbedPane = new JTabbedPane();

    RunButtonPanel buttonPanel = null;
    JPanel parentPanel = null;

    public LaunchingTabManager(RunButtonPanel runButtons, JPanel parentPanel) {
        this.buttonPanel = runButtons;
        this.parentPanel = parentPanel;

        JPanel blankPanel = new JPanel();
        Dimension theSize = new Dimension(1100, 550);
        blankPanel.setMinimumSize(theSize);
        blankPanel.setMaximumSize(theSize);
        blankPanel.setPreferredSize(theSize);

        tabbedPane.addTab("Chose a datastore group", blankPanel);
    }


    @Override
    public void launchButtonPressed(final DataStoreGroup groupToLaunch) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                List<UuidConfigItem> listOfDataStores = groupToLaunch.retrieveFullObjectsFromFiles();
                tabbedPane.removeAll();

                //now add the new stuff
                int i = 0;
                for (UuidConfigItem dataStore : listOfDataStores) {
                    TripletPanel panel = new TripletPanel(dataStore);

                    JButton closeButton = makeCloseButton();

                    JPanel titlePanel = new JPanel(new BorderLayout());
                    titlePanel.add(new JLabel(dataStore.toString() + "  "), BorderLayout.CENTER);
                    titlePanel.add(closeButton, BorderLayout.EAST);
                    titlePanel.setName(Integer.toString(closeButton.hashCode()));

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

                    i++;
                }

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
        Dimension dim = new Dimension(12,9);
        closeButton.setPreferredSize(dim);
        closeButton.setMaximumSize(dim);

        closeButton.addActionListener(new CloseButtonActionListener());
        return closeButton;
    }


    class CloseButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            JButton btn = (JButton) ae.getSource();
            Component toDelete = null;
            int index = -1;
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component tab = tabbedPane.getTabComponentAt(i);
                String hashString = Integer.toString(btn.hashCode());
                if (tab.getName().equals(hashString)) {
                    index = i;
                }
            }

            if (index != -1) {
                tabbedPane.remove(index);
            }
        }
    }

}
