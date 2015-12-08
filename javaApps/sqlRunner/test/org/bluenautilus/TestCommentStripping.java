package org.bluenautilus;

import org.bluenautilus.script.SqlScriptModifier;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/31/13
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCommentStripping {

    public static void main(String args[]) {

        File f = new File("test1.sql");
        SqlScriptModifier modifier = new SqlScriptModifier(f);
        try{
            modifier.createModifiedCopy();
        }    catch(Exception e){
            System.err.print(e);
        }


    }
}
