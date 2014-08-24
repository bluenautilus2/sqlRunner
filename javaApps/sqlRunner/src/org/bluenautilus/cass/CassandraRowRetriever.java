package org.bluenautilus.cass;

import com.datastax.driver.core.*;
import org.bluenautilus.data.CassFieldItems;

import org.bluenautilus.data.SqlScriptRow;
import org.joda.time.DateTime;


import java.util.ArrayList;
import java.util.Date;


public class CassandraRowRetriever {

	private CassFieldItems fields =null;

	public CassandraRowRetriever(CassFieldItems fields) {
		this.fields = fields;
	}


	public ArrayList<SqlScriptRow> readDataBase() throws Exception {
		ArrayList<SqlScriptRow> scriptRows = new ArrayList<SqlScriptRow>();
        SimpleClient client = new SimpleClient();

		try {

            client.connect(fields.getHostField());

            Session session = client.getSession();
            com.datastax.driver.core.ResultSet set = session.execute("SELECT * FROM pa.cql_script_executions WHERE year = 2014 ORDER BY tag");

            for (Row row : set) {
                String tag = row.getString("tag");
                Date date = row.getDate("last_date_applied");
                SqlScriptRow ssrow = new SqlScriptRow(tag, new DateTime(date));
                scriptRows.add(ssrow);
            }


            client.close();


		} catch (Exception e) {
			throw e;
		} finally {
			client.close();
		}
		return scriptRows;
	}



}
