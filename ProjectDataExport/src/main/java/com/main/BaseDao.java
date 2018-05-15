package com.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

public class BaseDao {
	/**
	 * 执行查询方法
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> query(Connection conn, String sql,
			Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.query(conn, sql, new MapListHandler(), params);
	}

	/**
	 * 执行update语句
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int update(Connection conn, String sql, Object... params)
			throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.update(conn, sql, params);
	}

	public int[] batch(Connection conn, String sql, Object[][] params) throws SQLException {
		QueryRunner runner = new QueryRunner();
		return runner.batch(conn, sql, params);
	}

	/**
	 * 获取MySQL连接
	 * 
	 * @param host
	 * @param database
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnectionFromMysql(String host,
			String database, String userName, String password)
			throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + host
				+ "/" + database + "?useUnicode=true&characterEncoding=utf8",
				userName, password);
		return conn;
	}

	public static Connection getConnectionFromOracle(String host,
			String database, String userName, String password)
			throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");

		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@"
				+ host + ":" + database, userName, password);
		return conn;
	}
}
