package org.bluenautilus.db;

import org.bluenautilus.data.ScriptContentFilter;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.gui.TripletPanelMgr;
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
public class DBRefreshAction extends org.bluenautilus.RefreshAction implements Runnable {

    private SqlConfigItems fields;
    private TripletPanelMgr tripletPanelMgr;

    public DBRefreshAction(SqlConfigItems fields, JPanel parent, TripletPanelMgr tripletPanelMgr) {
        super(parent);
        this.fields = fields;
        this.tripletPanelMgr = tripletPanelMgr;
    }

    public void refresh() {

        fireDBIOStart();
        results = new ArrayList<SqlScriptFile>();

        SqlScriptMgr sqlmgr;
        DBRowRetriever retriever;
        ScriptContentFilter filter = new ScriptContentFilter(SqlTarget.targetPattern, fields.getTarget().getHeaderTag());
        try {
            sqlmgr = new SqlScriptMgr(new File(fields.getScriptFolderField()), SqlScriptMgr.DataStoreType.SQL, filter);
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
