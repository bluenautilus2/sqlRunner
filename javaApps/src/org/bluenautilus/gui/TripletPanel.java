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

    UuidConfigItem sqlOrCassConfiguration = null;

    public TripletPanel(UuidConfigItem sqlOrCassConfiguration) {

        this.sqlOrCassConfiguration = sqlOrCassConfiguration;


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

        this.add(outerSplitPane);

    }

    public OutputPanel getOutputPanel() {
        return outputPanel;
    }

    public void setOutputPanel(OutputPanel outputPanel) {
        this.outputPanel = outputPanel;
    }

    public ScriptViewPanel getScriptViewPanel() {
        return scriptViewPanel;
    }

    public void setScriptViewPanel(ScriptViewPanel scriptViewPanel) {
        this.scriptViewPanel = scriptViewPanel;
    }

    public SqlScriptTablePanel getTableHolderPanel() {
        return tableHolderPanel;
    }

    public void setTableHolderPanel(SqlScriptTablePanel tableHolderPanel) {
        this.tableHolderPanel = tableHolderPanel;
    }

    public UuidConfigItem getSqlOrCassConfiguration() {
        return sqlOrCassConfiguration;
    }

    public void setSqlOrCassConfiguration(UuidConfigItem sqlOrCassConfiguration) {
        this.sqlOrCassConfiguration = sqlOrCassConfiguration;
    }
}
