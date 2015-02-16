package org.bluenautilus.gui;

import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.UuidConfigItem;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bstevens on 2/16/15.
 */
public class LaunchingTabManager implements LaunchButtonListener {

    JTabbedPane tabbedPane = new JTabbedPane();
    Map<Integer, TripletPanelMgr> tabMap = new HashMap<>();
    RunButtonPanel buttonPanel = null;
    JPanel parentPanel = null;

    public LaunchingTabManager(RunButtonPanel runButtons,  JPanel parentPanel) {
        this.buttonPanel = runButtons;
        this.parentPanel = parentPanel;
    }

    @Override
    public void launchButtonPressed(DataStoreGroup groupToLaunch) {
        List<UuidConfigItem> listOfDataStores = groupToLaunch.retrieveFullObjectsFromFiles();
        Set<Integer> tabKeys = tabMap.keySet();
        tabbedPane.removeAll();

        for (Integer i : tabKeys) {
            tabMap.remove(i);
        }
        //now add the new stuff
        int i = 0;
        for (UuidConfigItem dataStore : listOfDataStores) {
            TripletPanel panel = new TripletPanel(dataStore);

            tabbedPane.addTab(dataStore.toString(), panel);
            final TripletPanelMgr tripletPanelMgr = new TripletPanelMgr(panel.getOutputPanel(), panel.getScriptViewPanel(), panel.getTableHolderPanel(), buttonPanel, parentPanel);
            tabMap.put(i, tripletPanelMgr);
            i++;
        }

        //refresh only the one visible
        //sqlTripletPanelMgr.refreshAction();

    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}
