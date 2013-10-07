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
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/10/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunScriptAction implements Runnable {

    //  "C:\\PathToExe\\MyExe.exe","param1","param2").start();
    // private static final String CMD = "/home/bstevens/perlscripts/output_maker.pl";

    private static final String CMD = "osql";
    private FieldItems items;
    private SqlScriptFile file;
    private JPanel parentPanel;
    private ArrayList<ScriptCompletionListener> completionListeners = new ArrayList<ScriptCompletionListener>();
    private ArrayList<ScriptStatusChangeListener> statusListeners = new ArrayList<ScriptStatusChangeListener>();
    private ArrayList<ScriptKickoffListener> kickoffListeners = new ArrayList<ScriptKickoffListener>();

    public RunScriptAction(FieldItems items, SqlScriptFile sqlScriptFile, JPanel parentPanel) {
        this.items = items;
        this.file = sqlScriptFile;
        this.parentPanel = parentPanel;
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

    private void fireScriptAction() {

            this.fireStatusChanges(file, ScriptStatus.RUNNING);
            this.fireSingleScriptStarting(file);
            ScriptResultsEvent event;
            try {
                event = this.runScript(file);
                this.fireScriptCompletion(event);
                this.fireStatusChanges(file, ScriptStatus.RECENTLY_RUN);
            } catch (Exception e) {
                event = new ScriptResultsEvent(e.getMessage(), file, e);
                this.fireScriptCompletion(event);
                this.fireStatusChanges(file, ScriptStatus.RUN_ERROR);
            }

    }

    private ScriptResultsEvent runScript(SqlScriptFile scriptFile) throws IOException {
        ScriptModifier modifier = new ScriptModifier(scriptFile.getTheFile());
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

        while ((line = br.readLine()) != null) {
            strbuilder.append(line);
            strbuilder.append("\n");
        }

        ScriptResultsEvent event = new ScriptResultsEvent(strbuilder.toString(), scriptFile);
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

    private void fireSingleScriptStarting(final SqlScriptFile file) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        for (ScriptKickoffListener listener : kickoffListeners) {
                            listener.singleScriptStarting(file);
                        }
                    }
                }
        );
    }

    @Override
    public void run() {
        this.fireScriptAction();
    }
}
