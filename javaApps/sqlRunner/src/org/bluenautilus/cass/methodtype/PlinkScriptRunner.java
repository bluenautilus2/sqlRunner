package org.bluenautilus.cass.methodtype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.cass.CassandraScriptRunner;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.NoRunException;
import org.bluenautilus.script.ScriptCompletionListener;
import org.bluenautilus.script.ScriptResultsEvent;
import org.bluenautilus.script.ScriptType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by bstevens on 8/24/14.
 */
public class PlinkScriptRunner implements CassandraScriptRunner {

    private static final String DB_ERROR_FLAG = "Bad Request";
    private static Log log = LogFactory.getLog(PlinkScriptRunner.class);
    private static final String CQL_OUTPUT_FILE = "cassout.txt";

    @Override
    public ScriptResultsEvent runCassandraScript(ArrayList<ScriptCompletionListener> completionListeners, CassConfigItems items, SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
        File filetorun = null;  //if this stays null something crazy is going on.

        if (ScriptType.REGULAR == type) {
            filetorun = scriptFile.getTheFile();
        } else if (ScriptType.ROLLBACK == type) {
            //returns a file obj even if the file may not exist
            filetorun = scriptFile.getRollbackFile();
        }

        File oldOutputFile = new File(CQL_OUTPUT_FILE);

        if (oldOutputFile.exists()) {
            oldOutputFile.delete();
        }


        String[] array = { "runplink.bat",
                items.getHostField(),
                filetorun.getAbsolutePath(),
                items.getContainer()};

        ArrayList<String> params = new ArrayList<String>();
        Collections.addAll(params, array);

        ProcessBuilder processBuilder = new ProcessBuilder(params);

        Process process = processBuilder.start();

        StringBuilder strbuilder = new StringBuilder();

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
