package org.bluenautilus.cass.methodtype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.cass.CassandraScriptRunner;
import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.SqlScriptRunner;
import org.bluenautilus.script.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by bstevens on 8/24/14.
 */
public class SshScriptRunner implements CassandraScriptRunner {


    private static final String CMD = "./cass_ssh.sh";
    //@todo find real error flag
    private static final String DB_ERROR_FLAG = "severity 16";
    private static Log log = LogFactory.getLog(SshScriptRunner.class);
    private static final String CQL_OUTPUT_FILE = "cassout.txt";

    @Override
    public ScriptResultsEvent runCassandraScript(ArrayList<ScriptCompletionListener> completionListeners, CassFieldItems items, SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
        File filetorun = null;  //if this stays null something crazy is going on.

        if (ScriptType.REGULAR == type) {
            filetorun = scriptFile.getTheFile();
        } else if (ScriptType.ROLLBACK == type) {
            //returns a file obj even if the file may not exist
            filetorun = scriptFile.getRollbackFile();
        }

        //@todo with this commented out, we won't find the NO_RUN command (maybe that's ok)
        //ScriptModifier modifier = new ScriptModifier(oldfile);
        //completionListeners.add(modifier);
        //File newFile = modifier.createModifiedCopy();

        File oldOutputFile = new File(CQL_OUTPUT_FILE);

        if (oldOutputFile.exists()) {
            oldOutputFile.delete();
        }

        ProcessBuilder builder = new ProcessBuilder(this.CMD,
                "-S", items.getHostField(),
                "-U", "root",
                "-P", "catfox",
                "-f", filetorun.getAbsolutePath());

        Process process = builder.start();

        StringBuilder strbuilder = new StringBuilder();

//Might need this code someday for debugging.
        //strbuilder.append("\n OUTPUT FROM STDIN: \n");
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String linex;


        while ((linex = br.readLine()) != null) {

            strbuilder.append(linex);
            strbuilder.append("\n");
        }

        InputStream iserr = process.getErrorStream();
        InputStreamReader isrerr = new InputStreamReader(iserr);
        BufferedReader brerr = new BufferedReader(isrerr);
        String line;
        boolean dbProblem = false;

        boolean dbProblemerr = false;

        strbuilder.append("\nOutput from stderr: \n");
        while ((line = brerr.readLine()) != null) {
            if (null != line) {
                int i = line.indexOf(DB_ERROR_FLAG);
                if (i != -1) {
                    dbProblem = true;
                }
            }
            strbuilder.append(line + "\n");
        }

        strbuilder.append("\nOutput from cmd line:  \n");
        FileInputStream fis;
        BufferedReader reader = null;
        File newOutputFile = new File(CQL_OUTPUT_FILE);

        try {
            fis = new FileInputStream(newOutputFile);
            reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            while ((line = reader.readLine()) != null) {
                if (null != line) {
                    int i = line.indexOf(DB_ERROR_FLAG);
                    if (i != -1) {
                        dbProblem = true;
                    }
                }
                strbuilder.append(line);
                strbuilder.append("\n");
            } //end of reading lines
        } finally {
            if (null != reader) {
                reader.close();
            }
        }

        ScriptResultsEvent event = new ScriptResultsEvent(strbuilder.toString(), scriptFile, type, dbProblem);
        return event;
    }

}
