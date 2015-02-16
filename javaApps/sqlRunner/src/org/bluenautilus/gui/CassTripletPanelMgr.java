package org.bluenautilus.gui;

//import org.bluenautilus.cass.CassandraRefreshAction;
//import org.bluenautilus.data.CassConfigItems;
//import org.bluenautilus.data.SqlScriptFile;
//import org.bluenautilus.script.RunScriptAction;
//import org.bluenautilus.script.ScriptType;
//
//import javax.swing.*;
//import java.util.ArrayList;
//
///**
// * Created by bstevens on 8/24/14.
// */
//public class CassTripletPanelMgr extends TripletPanelMgr {
//
//    public CassTripletPanelMgr(OutputPanel outputPanel,
//                               ScriptViewPanel scriptViewPanel,
//                               SqlScriptTablePanel sqlTablePanel, RunButtonPanel buttonPanel, JFrame parentFrame) {
//        this.outputPanel = outputPanel;
//        this.scriptViewPanel = scriptViewPanel;
//        this.sqlTablePanel = sqlTablePanel;
//        this.parentFrame = parentFrame;
//        this.sqlTablePanel.addTableListener(this);
//        this.buttonPanel = buttonPanel;
//        this.buttonPanel.addRefreshListener(this);
//        this.buttonPanel.addScriptKickoffListener(this);
//        this.buttonPanel.addScriptRunAllToRunListener(this);
//        this.buttonPanel.addUpdatePreferencesListener(this);
//        this.scriptViewPanel.addPopOutListener(this);
//
//    }
//
//    public CassTripletPanelMgr(){
//        //does nothing
//    }
//
//    @Override
//    public void refreshAction() {
//
//        CassConfigItems items = this.cassButtonPanel.pullFieldsFromGui();
//      //  ConfigUtil.saveOffCurrent(items, this.cassButtonPanel);
//        CassandraRefreshAction action = new CassandraRefreshAction(items, this.cassButtonPanel, this);
//
//        action.addListener(this);
//        //runs in its own thread
//        Thread newThread = new Thread(action);
//        newThread.start();
//    }
//
//
//    @Override
//    public void preferencesUpdated() {
//        CassConfigItems items = this.cassButtonPanel.pullFieldsFromGui();
//       // ConfigUtil.saveOffCurrent(items, this.cassButtonPanel);
//    }
//
//    @Override
//    public void databaseRefreshCompleted(ArrayList<SqlScriptFile> results) {
//        this.cassButtonPanel.setRefreshButtonNormal();
//        this.refresh(results);
//    }
//
//    @Override
//    protected void runOneScript(ScriptType type){
//        if(this.filesBeingRun.isEmpty()){
//            return;
//        }
//
//        SqlScriptFile scriptFile = this.filesBeingRun.get(0);
//        this.filesBeingRun.remove(0);
//
//        RunScriptAction action = new RunScriptAction(this.cassButtonPanel.pullFieldsFromGui(), scriptFile, type);
//        action.addCompletionListener(this);
//        action.addCompletionListener(this.sqlTablePanel);
//        action.addStatusListener(this.sqlTablePanel);
//        action.addKickoffListener(this);
//
//        //runs in its own thread
//        Thread newThread = new Thread(action);
//        newThread.start();
//    }
//}
