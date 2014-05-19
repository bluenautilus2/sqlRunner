package org.bluenautilus.db.methodtype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.db.ScriptRunner;
import org.bluenautilus.script.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by beth may 18
 * tsql script runner.
 */
public class tSqlScriptRunner implements ScriptRunner {

    private static final String CMD = "./tsql.sh";
    private static final String DB_ERROR_FLAG = "severity 16";
    private static Log log = LogFactory.getLog(tSqlScriptRunner.class);
    private static final String TSQL_OUTPUT_FILE = "tsqlout.txt";

    @Override
    public ScriptResultsEvent runSqlCmdScript(ArrayList<ScriptCompletionListener> completionListeners, FieldItems items, SqlScriptFile scriptFile, ScriptType type) throws IOException, NoRunException {
        File oldfile = null;  //if this stays null something crazy is going on.

        if (ScriptType.REGULAR == type) {
            oldfile = scriptFile.getTheFile();
        } else if (ScriptType.ROLLBACK == type) {
            //returns a file obj even if the file may not exist
            oldfile = scriptFile.getRollbackFile();
        }

        ScriptModifier modifier = new ScriptModifier(oldfile);
        completionListeners.add(modifier);

        File newFile = modifier.createModifiedCopy();

        File oldOutputFile = new File(TSQL_OUTPUT_FILE);

        if (oldOutputFile.exists()) {
            oldOutputFile.delete();
        }

        ProcessBuilder builder = new ProcessBuilder(this.CMD,
                "-S", items.getIpAddressField(),
                "-p", items.getPort(),
                "-U", items.getLoginField(),
                "-P", items.getPasswordField(),
                "-D", items.getDbNameField(),
                "f", newFile.getAbsolutePath());

        Process process = builder.start();

        StringBuilder strbuilder = new StringBuilder();
        String line;
        FileInputStream fis;
        BufferedReader reader = null;
        boolean dbProblem = false;
        File newOutputFile = new File(TSQL_OUTPUT_FILE);

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
