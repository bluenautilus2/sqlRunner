package org.bluenautilus.cass;

import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.db.DBRowRetriever;
import org.bluenautilus.db.DatabaseRefreshIOListener;
import org.bluenautilus.db.SqlScriptMgr;
import org.bluenautilus.gui.CassPanelMgr;
import org.bluenautilus.gui.PanelMgr;
import org.bluenautilus.script.ScriptStatus;
import org.bluenautilus.util.GuiUtil;

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
public class CassandraRefreshAction implements Runnable {

	private JPanel parent;
	private CassFieldItems fields;
	private CassPanelMgr panelMgr;
	private ArrayList<DatabaseRefreshIOListener> listeners;
	private ArrayList<SqlScriptFile> results = new ArrayList<SqlScriptFile>();


	public CassandraRefreshAction(CassFieldItems fields, JPanel parent, CassPanelMgr panelMgr) {
		this.parent = parent;
		this.fields = fields;
		this.panelMgr = panelMgr;
		listeners = new ArrayList<DatabaseRefreshIOListener>();
	}


	public void refresh() {

		fireDBIOStart();

		results = new ArrayList<SqlScriptFile>();

		SqlScriptMgr sqlmgr;
		CassandraRowRetriever retriever;

		try {
			sqlmgr = new SqlScriptMgr(new File(fields.getScriptFolderField()));
			retriever = new CassandraRowRetriever(fields);

			ArrayList<SqlScriptRow> rows = retriever.readDataBase();
			ArrayList<SqlScriptFile> files = sqlmgr.getSqlList();
			results = combine(rows, files);

		} catch (Exception e) {
			GuiUtil.showErrorModalDialog(e, parent);
		} finally {
			this.fireDBIOEnd();
		}
	}


	public static ArrayList<SqlScriptFile> combine(ArrayList<SqlScriptRow> rows, ArrayList<SqlScriptFile> files) {
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


			for (SqlScriptRow row : rows) {
				if (currentFile.compareTo(row) == 0) {
					currentFile.addRowObjectToCollection(row);
				}
			}

            SqlScriptRow lastRow = currentFile.getLastRunRow();
            if(lastRow == null){
                currentFile.setStatus(ScriptStatus.NEED_TO_RUN);
            }else if(lastRow.isRollback()){
                currentFile.setStatus(ScriptStatus.ROLLED_BACK);
            }else{
                currentFile.setStatus(ScriptStatus.ALREADY_RUN);
            }

		}
		Collections.reverse(files);
		return files;
	}


	public void addListener(DatabaseRefreshIOListener newListener) {
		this.listeners.add(newListener);
	}


	private void fireDBIOStart() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (DatabaseRefreshIOListener dbl : listeners) {
					dbl.databaseRefreshStarted();
				}
			}
		});
	}


	private void fireDBIOEnd() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (DatabaseRefreshIOListener dbl : listeners) {
					dbl.databaseRefreshCompleted(results);
				}
			}
		});
	}

    @Override
    public void run() {
        refresh();
    }
}
