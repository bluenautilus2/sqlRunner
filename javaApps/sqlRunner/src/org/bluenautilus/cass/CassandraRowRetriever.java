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

import java.util.ArrayList;
import java.util.Date;


public class CassandraRowRetriever {

    private CassConfigItems items = null;

    private static Log log = LogFactory.getLog(CassandraRowRetriever.class);

    public CassandraRowRetriever(CassConfigItems items) {
        this.items = items;
    }


    public synchronized ArrayList<SqlScriptRow> readDataBase() throws Exception {
        ArrayList<SqlScriptRow> scriptRows = new ArrayList<SqlScriptRow>();
        DateTime today = new DateTime();
        Integer year = today.getYear();

        final String queryString = "SELECT * FROM cql_script_executions WHERE year IN (" + (year - 1) + ", " + year + ");\n";

        ResultSet results = null;

        try {
            Cluster cluster = Cluster.builder().addContactPoint(items.getHostField()).build();
            Session session = cluster.connect(items.getKeyspace());

            results = session.execute(queryString);
            session.close();

        } catch (Exception e) {
            String newError = "Datastax Driver is reporting a problem: \n" + e.toString();
            throw new Exception(newError);
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
}