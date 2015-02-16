package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.UuidConfigItem;

import javax.swing.*;
import java.awt.*;


/**
 * Created by bstevens on 2/16/15.
 * <p/>
 * this becomes a tab that holds scripts and their output
 */
public class TripletPanel extends JPanel {

    private static Log log = LogFactory.getLog(TripletPanel.class);

    OutputPanel outputPanel = new OutputPanel();
    ScriptViewPanel scriptViewPanel = new ScriptViewPanel();
    SqlScriptTablePanel tableHolderPanel = new SqlScriptTablePanel();
    RunButtonPanel buttonPanel = null;
    UuidConfigItem sqlOrCassConfiguration = null;

    JPanel outermostSqlPanel = new JPanel(new BorderLayout());

    public TripletPanel(RunButtonPanel runButtonPanel, UuidConfigItem sqlOrCassConfiguration) {

        this.sqlOrCassConfiguration = sqlOrCassConfiguration;
        this.buttonPanel = runButtonPanel;

        JSplitPane innerSplitPane = new JSplitPane();
        innerSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        innerSplitPane.setLeftComponent(scriptViewPanel);
        innerSplitPane.setRightComponent(outputPanel);
        innerSplitPane.setDividerLocation(0.5);

        JSplitPane outerSplitPane = new JSplitPane();
        outerSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        outerSplitPane.setLeftComponent(tableHolderPanel);
        outerSplitPane.setRightComponent(innerSplitPane);
        outerSplitPane.setDividerLocation(0.5);

        outermostSqlPanel.add(buttonPanel, BorderLayout.NORTH);
        outermostSqlPanel.add(outerSplitPane, BorderLayout.CENTER);

        final TripletPanelMgr sqlTripletPanelMgr = new TripletPanelMgr(outputPanel, scriptViewPanel, tableHolderPanel, buttonPanel, this);
        sqlTripletPanelMgr.refreshAction();
        this.add(outermostSqlPanel);

    }

}
