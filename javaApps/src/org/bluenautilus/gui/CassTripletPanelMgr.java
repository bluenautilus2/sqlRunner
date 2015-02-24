package org.bluenautilus.gui;


import org.bluenautilus.cass.CassandraRefreshAction;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.RunScriptAction;
import org.bluenautilus.script.ScriptType;

import javax.swing.*;

/**
 * if you're reading this, you get a cookie
 * --beth
 */
public class CassTripletPanelMgr extends TripletPanelMgr {

    private CassConfigItems myConfigItem = null;

    public CassTripletPanelMgr(CassConfigItems configItem,OutputPanel outputPanel,
                               ScriptViewPanel scriptViewPanel,
                               SqlScriptTablePanel sqlTablePanel, RunButtonPanel buttonPanel, JPanel parent) {
       super(outputPanel,scriptViewPanel,sqlTablePanel,buttonPanel,parent);
        this.myConfigItem = configItem;
    }

    public CassTripletPanelMgr(){
        //does nothing
    }

    @Override
    public void refreshAction() {

        CassandraRefreshAction action = new CassandraRefreshAction(myConfigItem, buttonPanel, this);
        action.addListener(this);

        //runs in its own thread
        Thread newThread = new Thread(action);
        newThread.start();
    }


    @Override
    public void preferencesUpdated() {
       // CassConfigItems items = this.cassButtonPanel.pullFieldsFromGui();
       // ConfigUtil.saveOffCurrent(items, this.cassButtonPanel);
    }


    @Override
    protected void runOneScript(ScriptType type){
        if(this.filesBeingRun.isEmpty()){
            return;
        }

        SqlScriptFile scriptFile = this.filesBeingRun.get(0);
        this.filesBeingRun.remove(0);


        RunScriptAction action = new RunScriptAction(this.myConfigItem, scriptFile, type);
        action.addCompletionListener(this);
        action.addCompletionListener(this.sqlTablePanel);
        action.addStatusListener(this.sqlTablePanel);
        action.addKickoffListener(this);

        //runs in its own thread
        Thread newThread = new Thread(action);
        newThread.start();
    }
}
