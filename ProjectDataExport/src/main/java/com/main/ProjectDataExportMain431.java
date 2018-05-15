package com.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * ����Ŀ�����ݵ���
 * 
 * @version 4.3.1����
 * @author admin
 *
 */
public class ProjectDataExportMain431 {

	public static class ResourceType {
		public static final int oec_project = 1 << 0;
		public static final int oec_element = 1 << 1;
		public static final int oec_concept = 1 << 2;
		public static final int oec_classify = 1 << 3;
		public static final int oec_sclassify = 1 << 4;
		public static final int oec_extract = 1 << 5;
		public static final int oec_multi_tuple = 1 << 6;
		public static final int oec_ontology = 1 << 7;

		public static final int oec_all = Integer.MAX_VALUE;
	}
	
	//----ɾ����Ŀ--
	public boolean deleteOecProjectCode(Connection conn,String projectCode) throws SQLException{
		//1.��ѯ����
		String sqlForOntoId="select onto_id,onto_type_id from oec_ontology where project_code=?";
		List<Map<String,Object>> ontoIdMaps=this.query(conn,sqlForOntoId, projectCode);
		if(ontoIdMaps!=null){
			for (Map<String, Object> map : ontoIdMaps) {
				String ontoId=map.get("ONTO_ID").toString();
				int ontoTypeId=Integer.parseInt(map.get("ONTO_TYPE_ID").toString());
				switch(ontoTypeId){
				case 1:{//c����
					String sql1="delete from oec_classify_resource where clas_id in (select clas_id from oec_classify where onto_id=?)";
					String sql2="delete from oec_classify where onto_id=?";
					this.update(conn, sql1, ontoId);
					this.update(conn,sql2, ontoId);
				}break;
				case 2:{//s����
					String sql1="delete from oec_sclassify_corpus where clas_id in (select clas_id from oec_sclassify where onto_id=?)";
					String sql2="delete from oec_sclassify_model where clas_id in (select clas_id from oec_sclassify where onto_id=?)";
					String sql3="delete from oec_sclassify where onto_id=?";
					this.update(conn, sql1, ontoId);
					this.update(conn, sql2, ontoId);
					this.update(conn, sql3, ontoId);
				}break;
				case 3:{//��Ϣ��ȡ
					String sql1="delete from oec_extract_resource where ext_id in (select ext_id from oec_extract where onto_id=?)";
					String sql2="delete from oec_extract where onto_id=?";
					this.update(conn, sql1, ontoId);
					this.update(conn, sql2, ontoId);
				}break;
				case 4:{//��Ԫ��
					String sql1="delete from oec_tuple_member_relate where ORIGIN_TUP_MEM_ID in (select mem_id from oec_tuple_member where tup_id in (select tup_id from oec_tuple where MULTI_TUP_ID in (select MULTI_TUP_ID from oec_multi_tuple where onto_id=?)))";
					String sql2="delete from oec_tuple_member where tup_id in (select tup_id from oec_tuple where MULTI_TUP_ID in (select MULTI_TUP_ID from oec_multi_tuple where onto_id=?))";
					String sql3="delete from oec_tuple where MULTI_TUP_ID in (select MULTI_TUP_ID from oec_multi_tuple where onto_id=?)";
					String sql4="delete from oec_multi_tuple where onto_id=?";
					this.update(conn,sql1, ontoId);
					this.update(conn,sql2, ontoId);
					this.update(conn,sql3, ontoId);
					this.update(conn,sql4, ontoId);
				}break;
				}
				this.update(conn, "delete from oec_ontology where onto_id=?", ontoId);
			}
		}
		//2.ɾ��Ҫ��
		String sqlForEleResource="delete from oec_element_resource where element_id in (select element_id from oec_element where project_code=?)";
		String sqlForEle="delete from oec_element where project_code=?";
		this.update(conn, sqlForEleResource, projectCode);
		this.update(conn, sqlForEle, projectCode);
		//3.ɾ������
		String sqlForConResource="delete from oec_concept_resource where concept_id in (select concept_id from oec_concept where project_code=?)";
		String sqlForCon="delete from oec_concept where project_code=?";
		this.update(conn, sqlForConResource, projectCode);
		this.update(conn, sqlForCon, projectCode);
		
		//3.ɾ����Ŀ
		String sqlForLanuge="delete  from oec_project_langue where project_code=?";
		String sqlForTeam="delete  from oec_project_team where team_id in (select team_id from oec_project where project_code=?)";
		String sqlForTeamUser="delete from oec_team_user where team_id in (select team_id from oec_project where project_code=?)";
		String sqlForDaemon="delete from oec_project_daemon where project_code=?";
		String sqlForDic="delete from oec_custom_dictionary where project_code=?";
		String sqlForProject="delete from oec_project where project_code=?";
		this.update(conn, sqlForLanuge, projectCode);
		this.update(conn, sqlForTeam, projectCode);
		this.update(conn, sqlForTeamUser, projectCode);
		this.update(conn, sqlForDaemon, projectCode);
		this.update(conn, sqlForDic, projectCode);
		this.update(conn, sqlForProject, projectCode);
		return true;
	}
	
	
	// ---��Ŀ---
	public List<Map<String, Object>> selectOecProject(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_project where PROJECT_CODE=?";
		return query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecProjectLangue(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_project_langue where PROJECT_CODE=?";
		return query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecProjectTeam(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_project_team where team_id in ("
				+ "		select team_id from oec_project where project_code=?"
				+ "   )";
		return query(conn, sql, projectCode);
	}

	// ---����---
	public List<Map<String, Object>> selectOecOntology(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_ontology where PROJECT_CODE=?";
		return query(conn, sql, projectCode);
	}

	// ---Ҫ��---
	public List<Map<String, Object>> selectOecElement(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_element where PROJECT_CODE=?";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecElementResource(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_element_resource where element_id in ("
				+ "		select element_id from oec_element where PROJECT_CODE=?"
				+ "   )";
		return this.query(conn, sql, projectCode);
	}

	// ---����---
	public List<Map<String, Object>> selectOecConcept(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_concept where PROJECT_CODE=?";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecConceptResource(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_concept_resource where concept_id in("
				+ "		select concept_id from oec_concept where PROJECT_CODE=?"
				+ "	  )";
		return this.query(conn, sql, projectCode);
	}

	// ---c������Ϣ---
	public List<Map<String, Object>> selectOecClassify(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_classify where onto_id in("
				+ "		select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "   )";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecClassifyResource(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_classify_resource where clas_id in ("
				+ "		select clas_id from oec_classify where onto_id in("
				+ "			select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "		)" + "   )";
		return this.query(conn, sql, projectCode);
	}

	// ---s������Ϣ----
	public List<Map<String, Object>> selectOecSclassify(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_sclassify where onto_id in("
				+ "		 select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "	  )";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecSclassifyModel(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_sclassify_model where clas_id in ("
				+ "		select clas_id from oec_sclassify where onto_id in("
				+ "			select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "		)" + "   )";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecSclassifyCorpus(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_sclassify_corpus where clas_id in ("
				+ "		select clas_id from oec_sclassify where onto_id in("
				+ "			select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "		)" + "   )";
		return this.query(conn, sql, projectCode);
	}

	// ---e��ȡ---
	public List<Map<String, Object>> selectOecExtract(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_extract where onto_id in("
				+ "		select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "	  )";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecExtractResource(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_extract_resource where ext_id in("
				+ "		select ext_id from oec_extract where onto_id in("
				+ "			select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "     )" + "   )";
		return this.query(conn, sql, projectCode);
	}

	// ---��Ԫ��---
	public List<Map<String, Object>> selectOecMultiTuple(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_multi_tuple where onto_id in ("
				+ "		select onto_id from oec_ontology where PROJECT_CODE=?"
				+ " )";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecTuple(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_tuple where multi_tup_id in ("
				+ "		select multi_tup_id from oec_multi_tuple where onto_id in ("
				+ "			select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "		)" + "   )";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecTupleMember(Connection conn,
			String projectCode) throws SQLException {
		String sql = "select * from oec_tuple_member where tup_id in("
				+ "		select tup_id from oec_tuple where multi_tup_id in ("
				+ "			select multi_tup_id from oec_multi_tuple where onto_id in ("
				+ "				select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "			)" + "		)" + "	  )";
		return this.query(conn, sql, projectCode);
	}

	public List<Map<String, Object>> selectOecTupleMemberRelate(
			Connection conn, String projectCode) throws SQLException {
		String sql = "select * from oec_tuple_member_relate where origin_tup_mem_id in("
				+ "		select mem_id from oec_tuple_member where tup_id in("
				+ "			select tup_id from oec_tuple where multi_tup_id in ("
				+ "				select multi_tup_id from oec_multi_tuple where onto_id in ("
				+ "					select onto_id from oec_ontology where PROJECT_CODE=?"
				+ "				)" + "			)" + "		)" + "   )";
		return this.query(conn, sql, projectCode);
	}

	/**
	 * ִ�в�ѯ����
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
	 * ִ��update���
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int update(Connection conn,String sql,Object...params) throws SQLException{
		QueryRunner runner = new QueryRunner();
		return runner.update(conn, sql, params);
	}

	/**
	 * ��ȡMySQL����
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

	public static void createSqlToStream(List<Map<String, Object>> datas,
			String tableName, OutputStreamWriter os) throws IOException {
		if (datas != null && datas.size() > 0) {
			for (Map<String, Object> map : datas) {
				map.remove("ID");
				StringBuffer beforeValues = new StringBuffer("insert into "
						+ tableName + " ( ");
				StringBuffer afterValues = new StringBuffer(")values( ");
				for (Entry<String, Object> entry : map.entrySet()) {
					beforeValues.append("" + entry.getKey() + ",");
					afterValues.append(valueFormat(entry.getValue()) + ",");
				}
				beforeValues.deleteCharAt(beforeValues.length() - 1);
				afterValues.deleteCharAt(afterValues.length() - 1).append(")");
				os.write((beforeValues.toString() + afterValues.toString() + ";\n"));
			}
			os.flush();
		}
	}

	public static String valueFormat(Object value) {
		if(value==null){
			value="";
		}
		if (value instanceof String) {
			return "'" + value.toString().replaceAll("\\\\","\\\\\\\\").replaceAll("'", "\\\\'")+ "'";
		} else if (value instanceof java.sql.Timestamp) {
			java.sql.Timestamp time = (Timestamp) value;
			Date date = new Date(time.getTime());
			SimpleDateFormat formate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return "'" + formate.format(date) + "'";
		}
		return value.toString();
	}

	public void exportDataToSQL(String host, String database, String userName,
			String password, String projectCode,String newProjectCode, int options,
			OutputStreamWriter os) throws ClassNotFoundException, SQLException,
			IOException {
		if(newProjectCode==null || newProjectCode.length()!=projectCode.length()){
			newProjectCode=projectCode;
		}
		Connection conn = getConnectionFromMysql(host, database, userName,
				password);
		if ((options & ResourceType.oec_project) != 0) {
			System.out.println("export:oec_project");
			List<Map<String,Object>> projects=selectOecProject(conn, projectCode);
			for (Map<String, Object> map : projects) {
				if(map.containsKey("PROJECT_CODE")){
					map.put("PROJECT_CODE", newProjectCode);
				}
			}
			createSqlToStream(projects,"oec_project", os);
			
			List<Map<String,Object>> projectLangues=selectOecProjectLangue(conn, projectCode);
			for (Map<String, Object> map : projectLangues) {
				if(map.containsKey("PROJECT_CODE")){
					map.put("PROJECT_CODE", newProjectCode);
				}
			}
			createSqlToStream(projectLangues,"oec_project_langue", os);
			
			List<Map<String,Object>> projectTeams=selectOecProjectTeam(conn, projectCode);
			for (Map<String, Object> map : projectTeams) {
				if(map.containsKey("PROJECT_CODE")){
					map.put("PROJECT_CODE", newProjectCode);
				}
			}
			createSqlToStream(projectTeams,"oec_project_team", os);
		}
		if ((options & ResourceType.oec_ontology) != 0) {
			System.out.println("export:oec_ontology");
			List<Map<String,Object>> ontologys=selectOecOntology(conn, projectCode);
			for (Map<String, Object> map : ontologys) {
				if(map.containsKey("PROJECT_CODE")){
					map.put("PROJECT_CODE", newProjectCode);
				}
			}
			createSqlToStream(ontologys,"oec_ontology", os);
		}
		if ((options & ResourceType.oec_element) != 0) {
			System.out.println("export:oec_element");
			createSqlToStream(selectOecElement(conn, projectCode),
					"oec_element", os);
			createSqlToStream(selectOecElementResource(conn, projectCode),
					"oec_element_resource", os); //
		}
		if ((options & ResourceType.oec_concept) != 0) {
			System.out.println("export:oec_concept");
			createSqlToStream(selectOecConcept(conn, projectCode),
					"oec_concept", os);
			createSqlToStream(selectOecConceptResource(conn, projectCode),
					"oec_concept_resource", os);
		}
		if ((options & ResourceType.oec_classify) != 0) {
			System.out.println("export:oec_classify");
			createSqlToStream(selectOecClassify(conn, projectCode),
					"oec_classify", os);
			List<Map<String,Object>> dataList=selectOecClassifyResource(conn, projectCode);
			for (Map<String, Object> map : dataList) {
				map.remove("operate_type");
			}
			createSqlToStream(dataList,
					"oec_classify_resource", os);
		}
		if ((options & ResourceType.oec_sclassify) != 0) {
			System.out.println("export:oec_sclassify");
			createSqlToStream(selectOecSclassify(conn, projectCode),
					"oec_sclassify", os);
			createSqlToStream(selectOecSclassifyModel(conn, projectCode),
					"oec_sclassify_model", os);
		}
		if ((options & ResourceType.oec_extract) != 0) {
			System.out.println("export:oec_extract");
			createSqlToStream(selectOecExtract(conn, projectCode),
					"oec_extract", os);
			List<Map<String,Object>> extractResources=selectOecExtractResource(conn, projectCode);
			for (Map<String, Object> map : extractResources) {
				map.remove("ID");
			}
			createSqlToStream(extractResources,"oec_extract_resource", os); //
		}
		if ((options & ResourceType.oec_multi_tuple) != 0) {
			System.out.println("export:oec_multi_tuple");
			createSqlToStream(selectOecMultiTuple(conn, projectCode),
					"oec_multi_tuple", os);
			createSqlToStream(selectOecTuple(conn, projectCode), "oec_tuple",
					os);
			createSqlToStream(selectOecTupleMember(conn, projectCode),
					"oec_tuple_member", os);
			createSqlToStream(selectOecTupleMemberRelate(conn, projectCode),
					"oec_tuple_member_relate", os);
		}
		String sql = "select * from oec_sclassify_corpus where clas_id in ("
				+ "select clas_id from oec_sclassify where onto_id in("
				+ "select onto_id from oec_ontology where PROJECT_CODE='"
				+ projectCode + "'" + ")" + ")";
		System.out.println("oec_sclassify_corpus��֧�ֵ���:�����е���{" + sql + "}");
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException, IOException {
		
		//��ҵ�о���PR10017
		String projectCode = "PR10024";
		int options = ResourceType.oec_all;
		new ProjectDataExportMain431().exportDataToSQL("127.0.0.1:3306",
				"dinfooec_poc", "root", "root", projectCode,projectCode, options,
				new OutputStreamWriter(new FileOutputStream("export/classify.sql"),
						"UTF-8"));
		
		//ɾ��
		/*Connection conn = getConnectionFromMysql("localhost:3306",
				"oecnlp", "root", "root");
		conn.setAutoCommit(false);
		try{
			new ProjectDataExportMain().deleteOecProjectCode(conn, projectCode);
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			conn.rollback();
		}*/

	}
}
