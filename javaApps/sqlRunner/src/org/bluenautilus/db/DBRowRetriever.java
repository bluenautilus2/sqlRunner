package org.bluenautilus.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlScriptRow;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;


public class DBRowRetriever {
	private static Log log = LogFactory.getLog(DBRowRetriever.class);

	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private SqlConfigItems fields = null;


	public DBRowRetriever(SqlConfigItems fields) {
		this.fields = fields;
	}


	public ArrayList<SqlScriptRow> readDataBase() throws Exception {
		ArrayList<SqlScriptRow> scriptRows = new ArrayList<SqlScriptRow>();

		try {
			// This will load the ms sql server driver, each DB has its own driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			// Setup the connection with the DB
			connect = DriverManager.getConnection(this.getConnectString(), fields.getLoginField(), fields.getPasswordField());
			log.info(this.getConnectString());

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("select * from system_db_updates order by db_update_date desc");

			while (resultSet.next()) {
				String s = resultSet.getString("DB_Update_Date");
                Timestamp time = resultSet.getTimestamp("Created_Date");
				SqlScriptRow row = new SqlScriptRow(s, new DateTime(time));
				scriptRows.add(row);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
		return scriptRows;
	}


	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}


    private String getConnectString() throws Exception {

        StringBuffer buff = new StringBuffer();
        buff.append("jdbc:sqlserver://");
        buff.append(fields.getIpAddressField());
        buff.append(":" + fields.getPort() + ";" + "DatabaseName=");
        buff.append(fields.getDbNameField());

        return buff.toString();

    }

}
