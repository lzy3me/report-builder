package com.flp.db;

import java.sql.*;

import javax.sql.DataSource;

/**
 * @author FLUKE
 * @version 1.0
 * @since 2019-11-14
 */

public class DBConnector {
	private static Connection connect = null;
	private static Statement stm;
	
	private static final String DB_NAME = "reportbuild";
	private static final String DB_SERVER = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "1234";
	
	public DBConnector() {
		initConnector();
	}
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		return connect;
	}
	
//	public static Connection getConnection() throws ClassNotFoundException, SQLException {
//		return getConnection(DB_NAME, DB_SERVER, DB_PORT, DB_USERNAME, DB_PASSWORD);
//	}
//	
//	public static Connection getConnection(String dbName, String dbServer
//			, String dbServerPort, String dbUsername, String dbPassword) throws ClassNotFoundException, SQLException {
//		// Get driver class
//		Class.forName("com.mysql.jdbc.Driver");
//		String connectionURL = "jdbc:mysql://"+dbServer+":"+dbServerPort+"/"+dbName+"?useUnicode=true&serverTimezone=UTC";
//		
//		connect = DriverManager.getConnection(connectionURL, dbUsername, dbPassword);
//		stm = connect.createStatement();
//		
//		if (connect != null)
//			System.out.println("DB Connected");
//		else
//			System.out.println("Failed");
//		if (stm != null)
//			System.out.println("Statement Created");
//		else
//			System.out.println("Failed");
//		
//		return connect;
//	}

	private void initConnector() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				connect = DriverManager.getConnection("jdbc:mysql://"+DB_SERVER+":"
						+DB_PORT+"/"+DB_NAME+"?useUnicode=true&serverTimezone=UTC", 
						DB_USERNAME, DB_PASSWORD);
				stm = connect.createStatement();
				
				if (connect != null)
					System.out.println("DB Connected");
				else
					System.out.println("Failed");
				
				if (stm != null)
					System.out.println("Statement Created");
				else
					System.out.println("Failed");
				
			} catch (SQLException e) {
				printError(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql) throws SQLException {
		ResultSet rs = null;
		if (stm.execute(sql)) {
			rs = stm.getResultSet();
		} else {
			return null;
		}
		return rs;
	}
	
	public void execute(String sql) throws SQLException {
		stm.executeUpdate(sql);
	}
	
	public void close() {
		try {
			if (connect != null) {
				connect.close();
				System.out.println("Connection is closed");
			} else { 
				System.out.println("Connection is already close or Didn't connect");
			}
			
			if (stm != null) {
				stm.close();
				System.out.println("Statement is closed");
			} else {
				System.out.println("Statement is already close or Didn't create");
			}
		} catch (SQLException e) {
			printError(e);
		}
	}
	
	private void printError(SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
	    System.out.println("SQLState: " + e.getSQLState());
	    System.out.println("VendorError: " + e.getErrorCode());
	}
	
	public static void main(String[] args) {
//		DBConnector db = new DBConnector();
//		ResultSet rs = db.query("SELECT MAX(customer_id) FROM customer");
//		try {
//			while (rs.next()) {
//				System.out.println(rs.getInt(1)+1);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		db.close();
	}
}
