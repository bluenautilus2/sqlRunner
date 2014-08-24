package org.bluenautilus.gui;

import org.bluenautilus.cass.CassandraRefreshAction;
import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.util.ConfigUtil;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by bstevens on 8/24/14.
 */
public class CassPanelMgr extends PanelMgr{

    public CassButtonPanel cassButtonPanel;


    public CassPanelMgr(OutputPanel outputPanel,
                    ScriptViewPanel scriptViewPanel,
                    SqlScriptTablePanel sqlTablePanel, CassButtonPanel cassButtonPanel, JFrame parentFrame) {
        this.outputPanel = outputPanel;
        this.scriptViewPanel = scriptViewPanel;
        this.sqlTablePanel = sqlTablePanel;
        this.parentFrame = parentFrame;
        this.sqlTablePanel.addTableListener(this);
        this.cassButtonPanel = cassButtonPanel;
        this.cassButtonPanel.addRefreshListener(this);
        this.cassButtonPanel.addScriptKickoffListener(this);
        this.cassButtonPanel.addScriptRunAllToRunListener(this);
        this.cassButtonPanel.addUpdatePreferencesListener(this);
        this.scriptViewPanel.addPopOutListener(this);

    }


    private CassPanelMgr(OutputPanel outputPanel,
                        ScriptViewPanel scriptViewPanel,
                        SqlScriptTablePanel sqlTablePanel, SqlButtonPanel cassButtonPanel, JFrame parentFrame) {
        //do nothing
    }

    public CassPanelMgr(){
        //does nothing
    }

    @Override
    public void refreshAction() {

        CassFieldItems items = this.cassButtonPanel.pullFieldsFromGui();
        ConfigUtil.saveOffCurrent(items, this.cassButtonPanel);
        CassandraRefreshAction action = new CassandraRefreshAction(items, this.cassButtonPanel, this);

        action.addListener(this);
        //runs in its own thread
        Thread newThread = new Thread(action);
        newThread.start();
    }


    @Override
    public void preferencesUpdated() {
        CassFieldItems items = this.cassButtonPanel.pullFieldsFromGui();
        ConfigUtil.saveOffCurrent(items, this.cassButtonPanel);
    }

    @Override
    public void databaseRefreshCompleted(ArrayList<SqlScriptFile> results) {
        this.cassButtonPanel.setRefreshButtonNormal();
        this.refresh(results);
    }
}
