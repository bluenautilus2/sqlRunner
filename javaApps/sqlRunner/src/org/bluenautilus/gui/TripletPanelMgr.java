package org.bluenautilus.gui;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.DBRefreshAction;
import org.bluenautilus.db.DatabaseRefreshIOListener;
import org.bluenautilus.script.OpenInSsmsEvent;
import org.bluenautilus.script.OpenInSsmsListener;
import org.bluenautilus.script.PopOutScriptEvent;
import org.bluenautilus.script.RunScriptAction;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptKickoffListener;
import org.bluenautilus.script.ScriptPopOutEventListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;
import org.bluenautilus.util.GuiUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/1/13
 * Time: 10:11 PM
 * To change this template use File | Settings | File Templates.
 *
 * Pretty much everything happens in this class, despite my efforts to de-centralize it.
 */
public class TripletPanelMgr implements RefreshListener, ListSelectionListener, ScriptKickoffListener, ScriptCompletionListener, DatabaseRefreshIOListener, ScriptPopOutEventListener, OpenInSsmsListener, TabSelectionListener {

    private static Log LOG = LogFactory.getLog(TripletPanelMgr.class);

    protected OutputPanel outputPanel;
    protected ScriptViewPanel scriptViewPanel;
    protected SqlScriptTablePanel sqlTablePanel;
    protected RunButtonPanel buttonPanel;
    protected SqlScriptFile lastSetFileObj;
    protected JPanel parentPanel;
    protected DisplayScriptDialog lastOpenedDialog;
    private SqlConfigItems myConfigItem;
    private boolean deactivated = false;

    public ArrayList<SqlScriptFile> filesBeingRun;


    public TripletPanelMgr(SqlConfigItems configItem, OutputPanel outputPanel,
                           ScriptViewPanel scriptViewPanel,
                           SqlScriptTablePanel sqlTablePanel, RunButtonPanel buttonPanel, JPanel parent) {
        this(outputPanel, scriptViewPanel, sqlTablePanel, buttonPanel, parent);
        this.myConfigItem = configItem;

    }

    public TripletPanelMgr(OutputPanel outputPanel,
                           ScriptViewPanel scriptViewPanel,
                           SqlScriptTablePanel sqlTablePanel, RunButtonPanel buttonPanel, JPanel parent) {
        this.outputPanel = outputPanel;
        this.scriptViewPanel = scriptViewPanel;
        this.sqlTablePanel = sqlTablePanel;
        this.parentPanel = parent;
        this.sqlTablePanel.addTableListener(this);
        this.buttonPanel = buttonPanel;
        this.buttonPanel.addRefreshListener(this);
        this.buttonPanel.addScriptKickoffListener(this);
        this.buttonPanel.addScriptRunAllToRunListener(this);
        this.scriptViewPanel.addPopOutListener(this);
        this.scriptViewPanel.addOpenInSsmsListner(this);
        this.scriptViewPanel.enableOpenSsmsButton();
        this.deactivated = false;
    }

    public void stopListening() {
        this.deactivated = true;
        this.sqlTablePanel.removeTableListener(this);
        this.buttonPanel.removeRefreshListener(this);
        this.scriptViewPanel.removePopOutListener(this);
        this.scriptViewPanel.removeOpenInSsmsListner(this);
    }

    public TripletPanelMgr() {
        //does nothing, for inheritance
    }

    protected void refresh(ArrayList<SqlScriptFile> files) {
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
        DBRefreshAction action = new DBRefreshAction(myConfigItem, this.buttonPanel, this);
        action.addListener(this);
        //runs in its own thread
        Thread newThread = new Thread(action);
        newThread.start();
    }

    @Override
    public void kickoffSelectedScripts(ScriptType type) {
        if (!deactivated) {
            ArrayList<SqlScriptFile> fileList = this.sqlTablePanel.getAllSelected();
            this.runScriptList(fileList, type);
        }
    }

    @Override
    public void kickoffAllToRunScripts() {
        if (!deactivated) {
            ArrayList<SqlScriptFile> fileList = this.sqlTablePanel.getAllToRun();
            this.runScriptList(fileList, ScriptType.REGULAR);
        }
    }

    private void runScriptList(ArrayList<SqlScriptFile> fileList, ScriptType type) {
        //run oldest first
        Collections.sort(fileList);

        if (ScriptType.ROLLBACK.equals(type)) {
            Collections.reverse(fileList);
        }

        this.filesBeingRun = fileList;

        //run the first one. when it completes it will trigger the running of the next one.
        runOneScript(type);
    }


    protected void runOneScript(ScriptType type) {
        if (this.filesBeingRun.isEmpty()) {
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

    @Override
    public void singleScriptStarting(SqlScriptFile file, ScriptType type) {
        if (!deactivated) {
            this.outputPanel.clearText();
            try {
                this.scriptViewPanel.setText(file);
            } catch (IOException ex) {
                GuiUtil.showErrorModalDialog(ex, this.outputPanel);
            }
        }
    }

    @Override
    public void scriptComplete(ScriptResultsEvent event) {
        if (!deactivated) {
            try {
                this.outputPanel.setText(event.getOutput());
            } catch (IOException ex) {
                GuiUtil.showErrorModalDialog(ex, this.outputPanel);
            }

            ScriptType type = event.getType();

            //see if there are any more scripts to run and if so, run them.
            if (event.getTheException() == null && !event.isDbProblem()) {
                runOneScript(type);
            } else {
                //but, if there is an exception, clear out the queue
                //and stop running scripts
                this.filesBeingRun.clear();
            }
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
        this.buttonPanel.aSingleRefreshCompleted();
        this.refresh(results);
    }

    /**
     * Launches a non-modal dialog box that we don't listen to. (yet)
     *
     * @param event
     */
    @Override
    public void popOutAScript(PopOutScriptEvent event) {

        //no files are selected, do nothing
        if (this.lastSetFileObj == null) {
            return;
        }

        //create a script display dialog
        File file = null;

        if (ScriptType.REGULAR.equals(event.getType())) {
            file = this.lastSetFileObj.getTheFile();
        } else {
            file = this.lastSetFileObj.getRollbackFile();
        }

        if (file != null) {
            try {
                DisplayScriptDialog dialog = new DisplayScriptDialog(file.getName(), file, this.parentPanel);
                dialog.pack();
                if (this.lastOpenedDialog != null) {
                    Point point = lastOpenedDialog.getLocation();
                    int x = point.x + 20;
                    int y = point.y + 10;
                    dialog.setLocation(x, y);
                }
                this.lastOpenedDialog = dialog;

                dialog.setVisible(true);
            } catch (Exception e) {
                GuiUtil.showErrorModalDialog(e, this.scriptViewPanel);
            }
        }

    }

    /**
     * Opens the selected SQL script in SQL server management studio.
     *
     * @param event
     */
    @Override
    public void openInSsms(final OpenInSsmsEvent event) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (lastSetFileObj != null) {// checking if a file is selected.
                    try {
                        final String selectedFilePath = lastSetFileObj.getTheFile().getAbsolutePath();

                        //runs ssms.exe for 2008 or later versions of SQL server management studio. Earlier versions use sqlwb.exe
                        final ProcessBuilder processBuilder = new ProcessBuilder("ssms.exe", "-S", myConfigItem.getIpAddressField(),
                                "-d", myConfigItem.getDbNameField(), "-U", myConfigItem.getLoginField(), "-P",
                                myConfigItem.getPasswordField(), selectedFilePath);
                        processBuilder.start();

                    } catch (final IOException e1) {
                        JOptionPane.showMessageDialog(null, "An error occurred while trying to open SQL server management studio.");
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * The tab this panel lives in was selected.
     */
    public void tabWasSelected(){
         //does nothing for now, here for future use.
    }

    /**
     * The tab this panel lives in is either now hidden, or continues to be hidden.
     */
    public void tabWasDeselected(){
        sqlTablePanel.deselectAllRows();
    }
}
