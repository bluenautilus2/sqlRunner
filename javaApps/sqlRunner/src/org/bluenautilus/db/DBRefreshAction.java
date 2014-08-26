package org.bluenautilus.db;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.gui.PanelMgr;
import org.bluenautilus.script.ScriptStatus;
import org.bluenautilus.util.GuiUtil;
import org.bluenautilus.util.MiscUtil;

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
public class DBRefreshAction extends org.bluenautilus.RefreshAction implements Runnable {

    private FieldItems fields;
	private PanelMgr panelMgr;


    public DBRefreshAction(FieldItems fields, JPanel parent, PanelMgr panelMgr) {
        super(parent);
        this.fields = fields;
		this.panelMgr = panelMgr;
    }


	public void refresh() {

		fireDBIOStart();

		results = new ArrayList<SqlScriptFile>();

		SqlScriptMgr sqlmgr;
		DBRowRetriever retriever;

		try {
			sqlmgr = new SqlScriptMgr(new File(fields.getScriptFolderField()));
			retriever = new DBRowRetriever(fields);

			ArrayList<SqlScriptRow> rows = retriever.readDataBase();
			ArrayList<SqlScriptFile> files = sqlmgr.getSqlList();
			results = MiscUtil.combine(rows, files);

		} catch (Exception e) {
			GuiUtil.showErrorModalDialog(e, parent);
		} finally {
			this.fireDBIOEnd();
		}
	}


    @Override
    public void run() {
        refresh();
    }
}
