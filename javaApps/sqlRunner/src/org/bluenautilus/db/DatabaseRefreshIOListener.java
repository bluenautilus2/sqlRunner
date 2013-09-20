package org.bluenautilus.db;

import org.bluenautilus.data.SqlScriptFile;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 9/19/13
 * Time: 7:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DatabaseRefreshIOListener {

    public void databaseRefreshStarted();

    public void databaseRefreshCompleted(ArrayList<SqlScriptFile> results);
}
