package org.bluenautilus.cass;

import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.db.DatabaseRefreshIOListener;
import org.bluenautilus.db.SqlScriptMgr;
import org.bluenautilus.gui.CassTripletPanelMgr;
import org.bluenautilus.util.GuiUtil;
import org.bluenautilus.util.MiscUtil;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/1/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class CassandraRefreshAction extends org.bluenautilus.RefreshAction implements Runnable {

	private CassConfigItems fields;
	private CassTripletPanelMgr panelMgr;

    public CassandraRefreshAction(){
        //nothin'
    }

	public CassandraRefreshAction(CassConfigItems fields, JPanel parent, CassTripletPanelMgr panelMgr) {
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
			retriever = new CassandraRowRetriever(fields,this.parent);

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
