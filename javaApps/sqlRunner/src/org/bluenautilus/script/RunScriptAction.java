package org.bluenautilus.script;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.DBConnectionType;
import org.bluenautilus.db.ScriptRunner;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/10/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunScriptAction implements Runnable {

    private FieldItems items;
    private SqlScriptFile file;
    private ArrayList<ScriptCompletionListener> completionListeners = new ArrayList<ScriptCompletionListener>();
    private ArrayList<ScriptStatusChangeListener> statusListeners = new ArrayList<ScriptStatusChangeListener>();
    private ArrayList<ScriptKickoffListener> kickoffListeners = new ArrayList<ScriptKickoffListener>();
    private ScriptType type = null;
	private DBConnectionType dbConnectionType;

    public RunScriptAction(FieldItems items, SqlScriptFile sqlScriptFile, ScriptType type, DBConnectionType dbConnectionType) {
        this.items = items;
        this.file = sqlScriptFile;
        this.type = type;
		this.dbConnectionType = dbConnectionType;
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
			switch (dbConnectionType) {
				case JDBC    : // Fall through
				case OSQL    : // Fall through
				case SQL_CMD :
                    ScriptRunner runner = dbConnectionType.getScriptRunner();
                    event = runner.runSqlCmdScript(completionListeners, items, file, type);
                    break;
				default : return;
			}
			if (event != null) {
				this.fireScriptCompletion(event);

				ScriptStatus finishStatus = event.isDbProblem() ? ScriptStatus.EXAMINE_OUTPUT : justRunStatus;

				this.fireStatusChanges(file, finishStatus);
			}
		} catch (Exception e) {
			event = new ScriptResultsEvent(e.getMessage(), file, this.type, false, e);
			this.fireScriptCompletion(event);

			if (e instanceof NoRunException){
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
