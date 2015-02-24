package org.bluenautilus.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A script can run many times and have many entries in the database.
 * It's rollback script can also run many times.
 * This class holds all database entries for a given script.
 */
public class SqlRowHolder {

    //there may be a lot of scripts that don't have a row.
    //do a lazy construction for these lists.
    private ArrayList<CreatedDateRow> rowList =  null;

    public void addScript(SqlScriptRow row){
        if(rowList == null){
            rowList = new ArrayList<CreatedDateRow>();
        }

        this.rowList.add(new CreatedDateRow(row));
    }


    public SqlScriptRow getLastCreatedRow(){
         if(this.rowList==null || this.rowList.isEmpty()){
             return null;
         }

         Collections.sort(this.rowList);
         return this.rowList.get((rowList.size()-1)).getRow();

    }


    private class CreatedDateRow implements Comparable<CreatedDateRow>{

        private SqlScriptRow myRow = null;

        public CreatedDateRow(SqlScriptRow myRow){
            this.myRow = myRow;
        }

        public SqlScriptRow  getRow(){
            return myRow;
        }

        @Override
        public int compareTo(CreatedDateRow o) {
            DateTime myDate = this.myRow.getRowCreated();
            DateTime otherDate = o.myRow.getRowCreated();

            if (myDate == null) {
                return -1;
            }

            if (otherDate == null) {
                return 1;
            }

            return myDate.compareTo(otherDate);
        }
    }
}
