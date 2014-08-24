package org.bluenautilus;


import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.bluenautilus.cass.SimpleClient;
import com.datastax.driver.core.ResultSet;

/**
 * Created by bstevens on 8/24/14.
 */
public class TestCassandra {

  // private String ip = "192.168.50.50";

    public static void main(String[] args) {
        SimpleClient client = new SimpleClient();
        client.connect("nucleus");

        Session session = client.getSession();
        ResultSet set = session.execute("SELECT * FROM pa.cql_script_executions WHERE year = 2014 ORDER BY tag");

        for (Row row : set) {
            System.out.println(String.format("%-30s\t%-20s\t%-20s", row.getInt("year"),
                    row.getString("tag"),  row.getString("tag")));
        }


        client.close();
    }
}
