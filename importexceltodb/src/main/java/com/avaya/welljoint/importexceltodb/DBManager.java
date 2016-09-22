package com.avaya.welljoint.importexceltodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBManager {
//	private static String driver = "com.mysql.jdbc.Driver";
//	private static String url = "jdbc:mysql://localhost:3306/ace";
//	private static String username = "root";
//	private static String password = "admin";

	public Connection getConnection() {
		
		String driver = Cache.propertyInfo.getDriver();
		String url = Cache.propertyInfo.getUrl();
		String username = Cache.propertyInfo.getUsername();
		String password = Cache.propertyInfo.getPassword();

		Connection conn = null;
		try {
			Class.forName(driver); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void batchInsert(List<MobileOwnership> list) {

		Connection conn = this.getConnection();

		Statement statement = null;
		try {
			conn.setAutoCommit(false);
			statement = conn.createStatement();

			for (int i = 0; i < list.size(); i++) {
				String sql = "INSERT INTO MOBILE_OWNERSHIP(ID,MOBILE,PROVINCE,CITY,CORP,AREACODE,POSTCODE) VALUES ('"
						+ list.get(i).getId() + "','" + list.get(i).getMobile() + "','" + list.get(i).getProvince() + "','"
						+ list.get(i).getCity() + "','" + list.get(i).getCorp() + "','" + list.get(i).getAreacode() + "','"
						+ list.get(i).getPostcode() + "')";
				
				System.out.println("sql:" + sql);
				
				statement.executeUpdate(sql);
			}

			conn.commit();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				statement.close();				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
