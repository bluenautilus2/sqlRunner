package org.bluenautilus.cass;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlScriptRow;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class CassandraRowRetriever {

    private CassConfigItems items = null;
    private static final String CQL_OUTPUT_FILE = "cassout.txt";
    private static final String QUERY_FILE = "getRows.cql";

    private static Log log = LogFactory.getLog(CassandraRowRetriever.class);

    public CassandraRowRetriever(CassConfigItems items) {
        this.items = items;
    }


    public  ArrayList<SqlScriptRow> readDataBase() throws Exception {
        ArrayList<SqlScriptRow> scriptRows = new ArrayList<SqlScriptRow>();

        ResultSet results = null;
        CassandraConnectionType type = CassandraConnectionType.getEnum(items.getConnectionType());

        if (type == CassandraConnectionType.DOCKER_PLINK) {

            synchronized (CQL_OUTPUT_FILE) {
                final File filetorun = createFileToRun();
                File oldOutputFile = new File(CQL_OUTPUT_FILE);
                if (oldOutputFile.exists()) {
                    oldOutputFile.delete();
                }
                String[] array = {"runplink.bat",
                        items.getHostField(),
                        filetorun.getAbsolutePath(),
                        items.getContainer()};
                ArrayList<String> params = new ArrayList<>();
                Collections.addAll(params, array);
                final ProcessBuilder processBuilder = new ProcessBuilder(params);
                scriptRows = runProcessPlink(processBuilder);
            }

        } else {
            try {
                Cluster cluster = Cluster.builder().addContactPoint(items.getHostField()).build();
                Session session = cluster.connect(items.getKeyspace());
                results = session.execute(createQuery());
                session.close();
            } catch (Exception e) {
                String newError = "Datastax Driver is reporting a problem: \n" + e.toString();
                throw new Exception(newError);
            }
        }

        if (results != null) {
            for (Row row : results) {
                scriptRows.add(getScriptRow(row.getString(1), row.getDate(2)));
            }
        }
        return scriptRows;
    }

    private SqlScriptRow getScriptRow(String tag, Date d) throws Exception {
        tag = tag.trim();
        return new SqlScriptRow(tag, new DateTime(d));
    }

    private File createFileToRun() throws  IOException {
        File filetorun = new File(QUERY_FILE);

        if (filetorun.exists()) {
            filetorun.delete();
        }
        OutputStream outputStream;
        BufferedWriter writer = null;

        try {
            outputStream = new FileOutputStream(filetorun);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
            writer.write("use " + items.getKeyspace() + ";\n");
            writer.write(createQuery());
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
        return filetorun;
    }

    private String createQuery() {
        DateTime today = new DateTime();
        Integer year = today.getYear();
        //the current year and the year before this year.
        final String queryString = "SELECT * FROM cql_script_executions WHERE year IN (" + (year - 1) + ", " + year + ");\n";
        return queryString;
    }

    private ArrayList<SqlScriptRow> runProcessPlink(ProcessBuilder processBuilder) throws Exception {
        ArrayList<SqlScriptRow> scriptRows = new ArrayList<SqlScriptRow>();
        StringBuilder errorString = new StringBuilder();

        File containingFolder = new File("./");
        if (containingFolder.exists() && containingFolder.canRead()) {
            if (!containingFolder.canWrite()) {
                errorString.append("Cannot connect - don't have write permission to the containing folder: " + containingFolder.getAbsolutePath());
            }
        } else {
            errorString.append("Cannot connect - don't have read permission to the containing folder: " + containingFolder.getAbsolutePath());
        }

        Process process = null;
        process = processBuilder.start();

        InputStream iserr = process.getErrorStream();
        InputStreamReader isrerr = new InputStreamReader(iserr);
        BufferedReader brerr = new BufferedReader(isrerr);
        String line;

        while ((line = brerr.readLine()) != null) {
            errorString.append(line + "\n");
        }

        boolean problemReported = false;
        //if there is an issue, show the error string but continue to try to connect.
        if ((null != errorString.toString()) && (errorString.toString().length() > 0)) {
            String newError = "StdErr is reporting a problem: \n" + errorString.toString();
            problemReported = true;
            throw new Exception(newError);
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
        process.destroy();
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