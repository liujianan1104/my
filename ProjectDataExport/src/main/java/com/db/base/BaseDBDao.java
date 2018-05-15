package com.db.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

public class BaseDBDao {

	private String driverUrl;

	private String userName;

	private String password;

	/**
	 * 
	 * @param className
	 *            MYSQL,ORACLE
	 * @param driverUrl
	 *            mysql :jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&
	 *            characterEncoding=utf8
	 *            oracle:jdbc:oracle:thin:@103.41.52.3:1521:orcl
	 * @param userName
	 * @param password
	 */
	public BaseDBDao(ClassName className, String driverUrl, String userName,
			String password) {
		super();
		this.driverUrl = driverUrl;
		this.userName = userName;
		this.password = password;
		if (className == ClassName.MYSQL) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Map<String, Object>> queryForMapList(Connection conn,
			String sql, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.query(conn, sql, new MapListHandler(), params);
	}

	public Map<String, Object> queryForMap(Connection conn, String sql,
			Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.query(conn, sql, new MapHandler(), params);
	}

	public int update(Connection conn, String sql, Object... params)
			throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.update(conn, sql, params);
	}

	public int[] batch(Connection conn, String sql, Object[][] params)
			throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.batch(conn, sql, params);
	}

	public Connection getConnection() throws SQLException,
			ClassNotFoundException {
		Connection conn = DriverManager.getConnection(driverUrl, userName,
				password);
		return conn;
	}

	public static enum ClassName {
		MYSQL("com.mysql.jdbc.Driver"), ORACLE(
				"oracle.jdbc.driver.OracleDriver");
		private String name;

		private ClassName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
