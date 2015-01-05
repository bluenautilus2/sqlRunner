package org.bluenautilus.cass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.SqlScriptRow;
import org.bluenautilus.util.GuiUtil;
import org.bluenautilus.util.MiscUtil;
import org.joda.time.DateTime;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class CassandraRowRetriever {

    private CassFieldItems fields = null;

    private static Log log = LogFactory.getLog(CassandraRowRetriever.class);
    private static final String CQL_OUTPUT_FILE = "cassout.txt";
    private static final String QUERY_FILE = "getRows.cql";
    private JPanel parentPanel = null;


    public CassandraRowRetriever(CassFieldItems fields, JPanel parentPanel) {
        this.fields = fields;
        this.parentPanel = parentPanel;
    }


    public ArrayList<SqlScriptRow> readDataBase() throws Exception {
        ArrayList<SqlScriptRow> scriptRows = new ArrayList<SqlScriptRow>();
        DateTime today = new DateTime();
        Integer year = today.getYear();

        File filetorun = new File(QUERY_FILE);

        if (filetorun.exists()) {
            filetorun.delete();
        }

        OutputStream outputStream;
        BufferedWriter writer = null;

        try {
            outputStream = new FileOutputStream(filetorun);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
            writer.write("use pa;\n");

            //the current year and the year before this year.
            writer.write("SELECT * FROM cql_script_executions WHERE year IN (" + (year - 1) + ", " + year + ") ORDER BY tag;\n");
        } finally {
            if (null != writer) {
                writer.close();
            }
        }


        File oldOutputFile = new File(CQL_OUTPUT_FILE);

        if (oldOutputFile.exists()) {
            oldOutputFile.delete();
        }

        ProcessBuilder processBuilder;

        if (MiscUtil.isThisLinux()) {
            ArrayList<String> params = new ArrayList<String>();
            String[] array = {"./cass_ssh.sh", "-S", fields.getHostField(),
                    "-U", "root",
                    "-P", "catfox",
                    "-f", filetorun.getAbsolutePath()};

            Collections.addAll(params, array);
            if (fields.useCertificate()) {
                if (fields.certFileExists()) {
                    params.add("-c");
                    params.add(fields.getCertificateFileField());
                }
            }
            processBuilder = new ProcessBuilder(params);
        } else {
            String[] array = {"runplink.bat",
                    fields.getHostField(),
                    filetorun.getAbsolutePath()};

            ArrayList<String> params = new ArrayList<String>();
            Collections.addAll(params, array);
            processBuilder = new ProcessBuilder(params);
        }

        Process process = processBuilder.start();

        InputStream iserr = process.getErrorStream();
        InputStreamReader isrerr = new InputStreamReader(iserr);
        BufferedReader brerr = new BufferedReader(isrerr);
        String line;
        StringBuilder errorString = new StringBuilder();
        while ((line = brerr.readLine()) != null) {
            errorString.append(line + "\n");
        }

        boolean problemReported = false;
        //if there is an issue, show the error string but continue to try to connect.
        if ((null != errorString.toString()) && (errorString.toString().length() > 0)) {
            String newError = "StdErr is reporting a problem: \n" + errorString.toString();
            GuiUtil.showErrorModalDialog(new Exception(newError), parentPanel);
            problemReported = true;
        }


        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String outputLine;
        final StringBuilder strbuilder = new StringBuilder();
        while ((outputLine = br.readLine()) != null) {
            strbuilder.append(outputLine);
            strbuilder.append("\n");
        }

        FileInputStream fis;
        BufferedReader reader = null;
        File newOutputFile = new File(CQL_OUTPUT_FILE);

        try {
            fis = new FileInputStream(newOutputFile);
            reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            while ((line = reader.readLine()) != null) {
                if (null != line) {
                    int i = line.indexOf("20");
                    if (i != -1) {
                        SqlScriptRow rowObj = this.getScriptRow(line);
                        if (rowObj != null) {
                            scriptRows.add(rowObj);
                        }
                    }
                }
            } //end of reading lines
        } finally {
            if (null != reader) {
                reader.close();
            }
        }
        if (scriptRows.isEmpty()) {
            log.info(strbuilder.toString());
            if (!problemReported) {
                throw new Exception("Could not connect to Cassandra DB: " + strbuilder.toString());
            }
        }

        return scriptRows;
    }


    private SqlScriptRow getScriptRow(String rawString) throws Exception {

        String[] parts = rawString.split("\\|");
        String tag = parts[1];
        String date = parts[2];

        tag = tag.trim();
        date = date.trim();

        //2014-03-16 22:01:15+0100
        Date d;
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss+SSSS");
        try {
            d = format.parse(date);
        } catch (ParseException pe) {
            try {
                SimpleDateFormat format2 =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss-SSSS");
                d = format2.parse(date);
            } catch (ParseException pe2) {
                throw new Exception("Error parsing rows from DB...: " + pe.getMessage(), pe);
            }
        }
        return new SqlScriptRow(tag, new DateTime(d));

    }
}