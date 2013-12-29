package org.bluenautilus.script;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/10/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunScriptAction implements Runnable {


     private static final String CMD = "/home/bstevens/perlscripts/output_maker.pl";

    //private static final String CMD = "osql";
    private static final String DB_ERROR_FLAG = "Level 16, State";


    private FieldItems items;
    private SqlScriptFile file;
    private JPanel parentPanel;
    private ArrayList<ScriptCompletionListener> completionListeners = new ArrayList<ScriptCompletionListener>();
    private ArrayList<ScriptStatusChangeListener> statusListeners = new ArrayList<ScriptStatusChangeListener>();
    private ArrayList<ScriptKickoffListener> kickoffListeners = new ArrayList<ScriptKickoffListener>();
    private ScriptType type = null;

    public RunScriptAction(FieldItems items, SqlScriptFile sqlScriptFile, JPanel parentPanel, ScriptType type) {
        this.items = items;
        this.file = sqlScriptFile;
        this.parentPanel = parentPanel;
        this.type = type;
    }

    public void addCompletionListener(ScriptCompletionListener listener) {
        this.completionListeners.add(listener);
    }

    public void addStatusListener(ScriptStatusChangeListener listener) {
        this.statusListeners.add(listener);
    }

    public void addKickoffListener(ScriptKickoffListener listener) {
        this.kickoffListeners.add(listener);
    }


	private void fireScriptAction(ScriptType type) {

		ScriptStatus runningStatus = (ScriptType.ROLLBACK == type) ? ScriptStatus.ROLLING_BACK : ScriptStatus.RUNNING;
		ScriptStatus justRunStatus = (ScriptType.ROLLBACK == type) ? ScriptStatus.RECENTLY_ROLLED : ScriptStatus.RECENTLY_RUN;

		this.fireStatusChanges(file, runningStatus);
		this.fireSingleScriptStarting(file, type);
		ScriptResultsEvent event;
		try {
			event = this.runScript(file, type);
			this.fireScriptCompletion(event);

            ScriptStatus finishStatus = event.isDbProblem()?ScriptStatus.EXAMINE_OUTPUT:justRunStatus;

			this.fireStatusChanges(file, finishStatus);

		} catch (Exception e) {
			event = new ScriptResultsEvent(e.getMessage(), file, this.type, false, e);
			this.fireScriptCompletion(event);

			if(e instanceof NoRunException){
				this.fireStatusChanges(file,ScriptStatus.NORUN);
				return;
			}

            if (ScriptType.ROLLBACK == type) {
                if(!file.getRollbackFile().exists()){
                    this.fireStatusChanges(file, ScriptStatus.NO_ROLLBACK);  
                    return;
                }
            }
			this.fireStatusChanges(file, ScriptStatus.RUN_ERROR);
		}

	}

    private ScriptResultsEvent runScript(SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
        File oldfile = null;  //if this stays null something crazy is going on.

        if(ScriptType.REGULAR == type){
              oldfile = scriptFile.getTheFile();
        }else if(ScriptType.ROLLBACK == type){
              //returns a file obj even if the file may not exist
              oldfile = scriptFile.getRollbackFile();
        }

        ScriptModifier modifier = new ScriptModifier(oldfile);
        this.addCompletionListener(modifier);

		File newFile = modifier.createModifiedCopy();

        ProcessBuilder builder = new ProcessBuilder(this.CMD,
                "-U", items.getLoginField(),
                "-P", items.getPasswordField(),
                "-S", items.getIpAddressField() + "," + items.getPort(),
                "-d", items.getDbNameField(),
                "-i", newFile.getAbsolutePath());


        Process process = builder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        StringBuilder strbuilder = new StringBuilder();

        boolean dbProblem = false;

        while ((line = br.readLine()) != null) {
            if(null != line){
               int i = line.indexOf(DB_ERROR_FLAG);
               if(i!=-1){
                   dbProblem = true;
               }
            }
            strbuilder.append(line);
            strbuilder.append("\n");
        }

        ScriptResultsEvent event = new ScriptResultsEvent(strbuilder.toString(), scriptFile, type, dbProblem);
        return event;
    }

    private void fireStatusChanges(final SqlScriptFile file, final ScriptStatus newStatus) {

        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        for (ScriptStatusChangeListener listener : statusListeners) {
                            listener.updateTableRowStatus(file, newStatus);
                        }
                    }
                }
        );

    }

    private void fireScriptCompletion(final ScriptResultsEvent event) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        for (ScriptCompletionListener listener : completionListeners) {
                            listener.scriptComplete(event);
                        }
                    }
                }
        );
    }

    private void fireSingleScriptStarting(final SqlScriptFile file, final ScriptType type) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        for (ScriptKickoffListener listener : kickoffListeners) {
                            listener.singleScriptStarting(file, type);
                        }
                    }
                }
        );
    }

    @Override
    public void run() {
        this.fireScriptAction(this.type);
    }


}
