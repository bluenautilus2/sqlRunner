package org.bluenautilus;

import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.DatabaseRefreshIOListener;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by bstevens on 8/25/14.
 */
public class RefreshAction {

    protected JPanel parent;
    protected ArrayList<DatabaseRefreshIOListener> listeners;
    protected ArrayList<SqlScriptFile> results = new ArrayList<SqlScriptFile>();

    public RefreshAction(JPanel parent) {
        this.parent = parent;
        listeners = new ArrayList<DatabaseRefreshIOListener>();
    }

    protected RefreshAction(){
        //nothin'
    }

    public void addListener(DatabaseRefreshIOListener newListener) {
        this.listeners.add(newListener);
    }

    protected void fireDBIOStart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (DatabaseRefreshIOListener dbl : listeners) {
                    dbl.databaseRefreshStarted();
                }
            }
        });
    }

    protected void fireDBIOEnd() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (DatabaseRefreshIOListener dbl : listeners) {
                    dbl.databaseRefreshCompleted(results);
                }
            }
        });
    }
}
