package org.bluenautilus.gui;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.UuidConfigItem;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bstevens on 2/16/15.
 */
public class LaunchingTabManager implements LaunchButtonListener {

    JTabbedPane tabbedPane = new JTabbedPane();
    Map<Integer, TripletPanelMgr> tabMap = new HashMap<>();
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

                tabMap = new HashMap<Integer, TripletPanelMgr>();

                //now add the new stuff
                int i = 0;
                for (UuidConfigItem dataStore : listOfDataStores) {
                    TripletPanel panel = new TripletPanel(dataStore);
                    tabbedPane.addTab(dataStore.toString(), panel);

                    TripletPanelMgr theMgr = null;
                    if(dataStore instanceof SqlConfigItems) {
                        SqlConfigItems items = (SqlConfigItems)dataStore;
                        theMgr = new TripletPanelMgr(items,panel.getOutputPanel(), panel.getScriptViewPanel(), panel.getTableHolderPanel(), buttonPanel, parentPanel);
                    }else if(dataStore instanceof CassConfigItems) {
                        CassConfigItems items = (CassConfigItems) dataStore;
                        theMgr = new CassTripletPanelMgr(items, panel.getOutputPanel(), panel.getScriptViewPanel(), panel.getTableHolderPanel(), buttonPanel, parentPanel);
                    }
                    tabMap.put(i,theMgr);
                    i++;
                }
            }
        });


    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}
