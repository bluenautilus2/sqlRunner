package org.bluenautilus.gui;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.DatabaseRefreshIOListener;
import org.bluenautilus.db.RefreshAction;
import org.bluenautilus.script.RunScriptAction;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptKickoffListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;
import org.bluenautilus.util.ConfigUtil;
import org.bluenautilus.util.GuiUtil;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/1/13
 * Time: 10:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PanelMgr implements RefreshListener, ListSelectionListener, ScriptKickoffListener, ScriptCompletionListener, DatabaseRefreshIOListener {

    public OutputPanel outputPanel;
    public ScriptViewPanel scriptViewPanel;
    public SqlScriptTablePanel sqlTablePanel;
    public SqlButtonPanel buttonPanel;
    public SqlScriptFile lastSetFileObj;

    public ArrayList<SqlScriptFile> filesBeingRun;


    public PanelMgr(OutputPanel outputPanel,
                    ScriptViewPanel scriptViewPanel,
                    SqlScriptTablePanel sqlTablePanel, SqlButtonPanel buttonPanel) {
        this.outputPanel = outputPanel;
        this.scriptViewPanel = scriptViewPanel;
        this.sqlTablePanel = sqlTablePanel;
        this.sqlTablePanel.addTableListener(this);
        this.buttonPanel = buttonPanel;
        this.buttonPanel.addRefreshListener(this);
        this.buttonPanel.addScriptKickoffListener(this);
        this.buttonPanel.addScriptRunAllToRunListener(this);

    }

    private void refresh(ArrayList<SqlScriptFile> files) {
        this.sqlTablePanel.setValues(files);
        this.outputPanel.clearText();
        this.scriptViewPanel.clearText();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {

        ArrayList<SqlScriptFile> list = this.sqlTablePanel.getAllSelected();
        if (list.size() <= 0) {
            return;
        }

        if (list.size() > 1) {
            this.scriptViewPanel.clearText();
            return;
        }

        SqlScriptFile theFile = list.get(0);
        if (theFile == this.lastSetFileObj) {
            return;
        }

        this.lastSetFileObj = theFile;
        try {
            this.scriptViewPanel.setText(theFile);
            this.outputPanel.setText(theFile.getResultsString());

        } catch (Exception ex) {
            GuiUtil.showErrorModalDialog(ex, this.scriptViewPanel);
        }

    }

    @Override
    public void refreshAction() {

        FieldItems items = this.buttonPanel.pullFieldsFromGui();
        ConfigUtil.saveOffCurrent(items, this.buttonPanel);
        RefreshAction action = new RefreshAction(items, this.buttonPanel, this);

        action.addListener(this);
        //runs in its own thread
        Thread newThread = new Thread(action);
        newThread.start();
    }

    @Override
    public void kickoffSelectedScripts(ScriptType type) {
        ArrayList<SqlScriptFile> fileList = this.sqlTablePanel.getAllSelected();
        this.runScriptList(fileList, type);
    }

    @Override
    public void kickoffAllToRunScripts(){
        ArrayList<SqlScriptFile> fileList = this.sqlTablePanel.getAllToRun();
        this.runScriptList(fileList, ScriptType.REGULAR);
    }

    private void runScriptList(ArrayList<SqlScriptFile> fileList, ScriptType type){
        //run oldest first
        Collections.sort(fileList);
        this.filesBeingRun = fileList;

        //run the first one. when it completes it will trigger the running of the next one.
        runOneScript(type);
    }

    private void runOneScript(ScriptType type){
        if(this.filesBeingRun.isEmpty()){
            return;
        }

        SqlScriptFile scriptFile = this.filesBeingRun.get(0);
        this.filesBeingRun.remove(0);

        RunScriptAction action = new RunScriptAction(this.buttonPanel.pullFieldsFromGui(), scriptFile, this.buttonPanel, type);
        action.addCompletionListener(this);
        action.addCompletionListener(this.sqlTablePanel);
        action.addStatusListener(this.sqlTablePanel);
        action.addKickoffListener(this);

        //runs in its own thread
        Thread newThread = new Thread(action);
        newThread.start();
    }

    public void singleScriptStarting(SqlScriptFile file, ScriptType type) {
        this.outputPanel.clearText();
        try {
            this.scriptViewPanel.setText(file);
        } catch (IOException ex) {
            GuiUtil.showErrorModalDialog(ex, this.outputPanel);
        }
    }

    @Override
    public void scriptComplete(ScriptResultsEvent event) {
        try {
            this.outputPanel.setText(event.getOutput());
        } catch (IOException ex) {
            GuiUtil.showErrorModalDialog(ex, this.outputPanel);
        }

        ScriptType type = event.getType();

        //see if there are any more scripts to run and if so, run them.
        if(event.getTheException()==null){
		   runOneScript(type);
		}else{
		   //but, if there is an exception, clear out the queue
		   //and stop running scripts
		   this.filesBeingRun.clear();
		}

    }

    /**
     * takes place in the swing thread
     */
	@Override
	public void databaseRefreshStarted() {
        //nothing right now. later on might want to launch
        //modal dialog or something

	}


    @Override
    public void databaseRefreshCompleted(ArrayList<SqlScriptFile> results) {
        this.buttonPanel.setRefreshButtonNormal();
        this.refresh(results);
    }

   
}
