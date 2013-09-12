package org.bluenautilus.db;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.gui.PanelMgr;
import org.bluenautilus.util.GuiUtil;
import org.bluenautilus.script.ScriptStatus;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.data.SqlScriptRow;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/1/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RefreshAction {

    private JPanel parent;
    private FieldItems fields;
    private PanelMgr panelMgr;

    public RefreshAction(FieldItems fields, JPanel parent, PanelMgr panelMgr) {
        this.parent = parent;
        this.fields = fields;
        this.panelMgr = panelMgr;
    }

    public void refresh() {
        SqlScriptMgr sqlmgr;
        DBRowRetriever retriever;

        try {
            sqlmgr = new SqlScriptMgr(new File(fields.getScriptFolderField()));
            retriever = new DBRowRetriever(fields);

            ArrayList<SqlScriptRow> rows = retriever.readDataBase();
            ArrayList<SqlScriptFile> files = sqlmgr.getSqlList();

            ArrayList<SqlScriptFile> updated = combine(rows, files);
            panelMgr.refresh(updated);
        } catch (Exception e) {
            GuiUtil.showErrorModalDialog(e, parent);
        }
    }

    public ArrayList<SqlScriptFile> combine(ArrayList<SqlScriptRow> rows, ArrayList<SqlScriptFile> files) {
        ArrayList<SqlScriptFile> answer = new ArrayList<SqlScriptFile>();

        Collections.sort(rows);
        Collections.sort(files);
        //both collections guaranteed to not be empty

        SqlScriptRow firstRow = rows.get(0);

        boolean done = false;
        boolean beginningWasFound = false;

        int i = 0;
        while (!done) {
            SqlScriptFile currentFile = files.get(i);

            if (firstRow.compareTo(currentFile) > 0) {
                //this file is older, keep going
            }
            if (firstRow.compareTo(currentFile) <= 0) {
                //we found a file that is at least as new as the oldest db script
                beginningWasFound = true;
                done = true;
            }
            if (files.size() == i + 1) {
                done = true;
            } else if (!done) {
                i++;
            }
        }

        if (!beginningWasFound) {
            return files;
            //@todo throw a warning
        }

        for (int k = i; k < files.size(); k++) {
            SqlScriptFile currentFile = files.get(k);
            currentFile.setStatus(ScriptStatus.NEED_TO_RUN);

            for (SqlScriptRow row : rows) {
                if (currentFile.compareTo(row) == 0) {
                    currentFile.setStatus(ScriptStatus.ALREADY_RUN);
                }
            }
        }
        Collections.reverse(files);
        return files;
    }


}
