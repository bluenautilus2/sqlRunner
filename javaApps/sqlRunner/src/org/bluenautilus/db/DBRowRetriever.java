package org.bluenautilus.db;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.data.SqlScriptRow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class DBRowRetriever {

	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private FieldItems fields = null;


	public DBRowRetriever(FieldItems fields) {
		this.fields = fields;

	}


	public ArrayList<SqlScriptRow> readDataBase() throws Exception {
		ArrayList<SqlScriptRow> strings = new ArrayList<SqlScriptRow>();

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			// Setup the connection with the DB

			connect = DriverManager.getConnection(this.getConnectString(), fields.getLoginField(), fields.getPasswordField());
			System.out.println(this.getConnectString());

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("select * from system_db_updates order by db_update_date desc");

			while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number
				// which starts at 1
				// e.g. resultSet.getSTring(2);
				String s = resultSet.getString("DB_Update_Date");
				SqlScriptRow row = new SqlScriptRow(s);
				strings.add(row);

			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
		return strings;
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
