package com.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 迁移mysql中的数据到oracle中,要求两张表名称，字段完全相同
 * @author tong
 *
 */
@SuppressWarnings("static-access")
public class MysqlToOracle extends BaseDao {

	private Connection mysqlConn = null;

	private Connection oracleConn = null;

	{
		try {
			mysqlConn = this.getConnectionFromMysql("127.0.0.1", "oecv5_test",
					"root", "root");
			oracleConn = this.getConnectionFromOracle("192.168.75.128:1521",
					"orcl2", "oec_v421", "oec_v421123");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 一次导一张表中的数据
	 * @param tableName
	 * @throws SQLException
	 */
	public void selectDataByTableName(String tableName) throws SQLException {
		List<Map<String, Object>> results = this.query(mysqlConn,
				"select * from " + tableName);
		System.out.println("query:" + results.size());
		if (results.size() > 0) {
			List<String> fields = new ArrayList<String>(results.get(0).keySet());
			StringBuffer preSql = new StringBuffer("insert into " + tableName
					+ " (");
			StringBuffer afterSql = new StringBuffer(")values(");
			for (String field : fields) {
				preSql.append(field + ",");
				afterSql.append("?,");
			}
			if (preSql.substring(preSql.length() - 1).equals(",")) {
				preSql.deleteCharAt(preSql.length() - 1);
			}
			if (afterSql.substring(afterSql.length() - 1).equals(",")) {
				afterSql.deleteCharAt(afterSql.length() - 1);
			}
			afterSql.append(")");
			String insertSql = preSql.toString() + afterSql.toString();
			System.out.println(insertSql);
			// 分批保存数据
			batchSave(insertSql, results);
		}
	}

	/**
	 * 分批导同一张表中的数据
	 * @param tableName
	 * @param orderField
	 * @throws SQLException
	 */
	public void batchSelectDataByTableName(String tableName, String orderField)
			throws SQLException {

		List<Map<String, Object>> countMaps = this.query(mysqlConn,
				"select count(1) as total from " + tableName);
		System.out.println("total:" + countMaps.get(0).get("total").toString());
		int total = Integer.parseInt(countMaps.get(0).get("total").toString());
		int limit = 100000;
		int skip = 0;
		int times = total % limit;
		if ((total % limit) > 0) {
			times++;
		}
		for (int time = 0; time < times; time++) {
			skip = time * limit;
			List<Map<String, Object>> results = this.query(mysqlConn,
					"select * from " + tableName + " order by " + orderField
							+ " limit " + skip + "," + limit);
			System.out.println("query:" + results.size());
			if (results.size() > 0) {
				List<String> fields = new ArrayList<String>(results.get(0)
						.keySet());
				StringBuffer preSql = new StringBuffer("insert into "
						+ tableName + " (");
				StringBuffer afterSql = new StringBuffer(")values(");
				for (String field : fields) {
					preSql.append(field + ",");
					afterSql.append("?,");
				}
				if (preSql.substring(preSql.length() - 1).equals(",")) {
					preSql.deleteCharAt(preSql.length() - 1);
				}
				if (afterSql.substring(afterSql.length() - 1).equals(",")) {
					afterSql.deleteCharAt(afterSql.length() - 1);
				}
				afterSql.append(")");
				String insertSql = preSql.toString() + afterSql.toString();
				System.out.println(insertSql);
				// 分批保存数据
				batchSave(insertSql, results);
			}
		}

	}

	private void batchSave(String insertSql, List<Map<String, Object>> results)
			throws SQLException {
		int batchSize = 1000;
		List<String> fields = new ArrayList<String>(results.get(0).keySet());
		int times = results.size() / batchSize;
		// 成批部分
		for (int time = 0; time < times; time++) {
			Object[][] params = new Object[batchSize][fields.size()];
			for (int i = 0; i < batchSize; i++) {
				Map<String, Object> map = results.get(time * batchSize + i);
				for (int j = 0; j < fields.size(); j++) {
					Object obj = map.get(fields.get(j)) == null ? new String("")
							: map.get(fields.get(j));
					if (obj.toString().equalsIgnoreCase("null")
							|| obj.toString().length() == 0) {
						obj = new String(" ");
					}
					params[i][j] = obj;
				}
			}
			int[] result = this.batch(oracleConn, insertSql, params);
			System.out.println(results.size() + "-" + (time * batchSize) + "-"
					+ result.length);
		}
		// 剩余部分
		if ((results.size() % batchSize) > 0) {
			Object[][] params = new Object[results.size() % batchSize][fields
					.size()];
			for (int i = 0; i < (results.size() % batchSize); i++) {
				Map<String, Object> map = results.get(times * batchSize + i);
				for (int j = 0; j < fields.size(); j++) {
					Object obj = map.get(fields.get(j)) == null ? new String("")
							: map.get(fields.get(j));
					if (obj.toString().equalsIgnoreCase("null")
							|| obj.toString().length() == 0) {
						obj = new String(" ");
					}
					params[i][j] = obj;
				}
			}
			int[] result = this.batch(oracleConn, insertSql, params);
			System.out.println(results.size() + "-"
					+ (results.size() % batchSize) + "-" + result.length);
		}
	}

	public static void main(String[] args) throws SQLException {
		String[] tables = { "oec_res_concept", "oec_res_concept_resource",
				"oec_field_cate", "oec_field_word", "oec_field",
				"oec_field_resource", "oec_base_word", "oec_user_word",
				"oec_syn_index", "oec_syn_word", "oec_field_syn_index",
				"oec_field_syn_word", "oec_user_syn_index",
				"oec_user_syn_word", "oec_semantic" };
//		new MysqlToOracle().batchSelectDataByTableName("oec_field_resource",
//				"create_time");
	}

}
