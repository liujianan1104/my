package com.dinfo.oec.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.dinfo.oec.tools.PrimaryKeyGenerator;


public class ConResTransfer {
	
	public static String DB_TYPE="oracle";

	private Connection oldConn = null;

	private Connection newConn = null;

	public ConResTransfer() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		oldConn = DriverManager
				.getConnection(
						"jdbc:mysql://127.0.0.1:3306/123_landinfooec510?useUnicode=true&characterEncoding=utf8",
						"root", "root");
		/*newConn = DriverManager
				.getConnection(
						"jdbc:mysql://127.0.0.1:3306/semantic_test_bak?useUnicode=true&characterEncoding=utf8",
						"root", "root");*/
		newConn = DriverManager
				.getConnection("jdbc:oracle:thin:@192.168.38.131:1521:helowin",
						"oectest", "oectest123");
	}

	// 查询数据
	@SuppressWarnings("unused")
	private List<Map<String, Object>> queryListMap(Connection conn, String sql,
			Object... params) throws SQLException {
		return new QueryRunner().query(conn, sql, new MapListHandler(), params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<OecResConcept> queryListConceptResTrees(
			Connection conn, String sql, Object... params) throws SQLException {
		return (List<OecResConcept>) new QueryRunner().query(conn, sql,
				new BeanListHandler(OecResConcept.class), params);
	}

	public static List<OecResConcept> queryChildOecResConcept(Connection conn,
			String parentId) throws SQLException {
		return queryListConceptResTrees(conn,
				"select * from oec_res_concept where concept_parent_id=?",
				parentId);
	}

	public static List<OecResConcept> queryRootOecResConcept(Connection conn)
			throws SQLException {
		return queryListConceptResTrees(conn,
				"select * from oec_res_concept where concept_level=?", 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<OecResConceptResource> queryOecResConceptResources(
			Connection conn, String conceptId) throws SQLException {
		return (List<OecResConceptResource>) new QueryRunner().query(conn,
				"select * from oec_res_concept_resource where concept_id=?",
				new BeanListHandler(OecResConceptResource.class), conceptId);
	}

	// 插入数据
	@SuppressWarnings("unused")
	private Long insert(Connection conn, String sql, Object... params)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(sql,
				PreparedStatement.RETURN_GENERATED_KEYS);
		new QueryRunner().fillStatement(stmt, params);
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			return rs.getLong(1);
		}
		return null;
	}

	// 概念资源迁移---------------------------------------------------------------
	public void oecConceptResTransfer() throws SQLException {
		// 查询数据
		List<OecResConcept> roots = queryOecResConcepts(oldConn);
		System.out.println("查询完成");
		// 数据转换
		List<ConceptResTree> conceptResTrees = transToConceptResTrees(roots);
		System.out.println("转换完成");
		System.out.println("数量:"+conceptResTrees.size());
		// 编号、路径、等级、排序编号重建
		reBuiltId(conceptResTrees);
		System.out.println("编号生成完成");
		// 插入新库
		intoDB(newConn,conceptResTrees);
		System.out.println("over");
	}

	// 查询概念资源
	private List<OecResConcept> queryOecResConcepts(Connection conn)
			throws SQLException {
		List<OecResConcept> roots = queryRootOecResConcept(conn);
		for (OecResConcept oecResConcept : roots) {
			queryAndFillOecResConcept(conn, oecResConcept);
		}
		return roots;
	}

	// 递归查询子节点和值数据
	private void queryAndFillOecResConcept(Connection conn,
			OecResConcept oecResConcept) throws SQLException {
		// 查询概念资源值
		List<OecResConceptResource> oecResConceptResources = queryOecResConceptResources(
				conn, oecResConcept.getConcept_id());
		oecResConcept.setResources(oecResConceptResources);
		// 查询子节点
		List<OecResConcept> childs = queryChildOecResConcept(conn,
				oecResConcept.getConcept_id());
		oecResConcept.setChildren(childs);
		for (OecResConcept child : childs) {
			queryAndFillOecResConcept(conn, child);
		}
	}

	// 转换概念资源
	private List<ConceptResTree> transToConceptResTrees(
			List<OecResConcept> oecResConcepts) {
		List<ConceptResTree> conceptResTrees = new ArrayList<ConceptResTree>();
		for (OecResConcept oecResConcept : oecResConcepts) {
			conceptResTrees.add(transToConceptResTree(oecResConcept));
		}
		return conceptResTrees;
	}

	private ConceptResTree transToConceptResTree(OecResConcept oecResConcept) {
		if (oecResConcept.getChildren() == null
				|| oecResConcept.getChildren().size() == 0) {// 子节点
			// 节点信息
			ConceptResTree conceptResTree = new ConceptResTree(null, null,
					oecResConcept.getConcept_name(), null, null, null, 1L,
					null, oecResConcept.getDescription(), null, null, null);
			conceptResTree.setChildrens(new ArrayList<ConceptResTree>(0));
			// 表达式
			List<OecResConceptResource> resources = oecResConcept
					.getResources();
			List<ConceptResValue> conceptResValues = new ArrayList<ConceptResValue>();
			if (resources != null && resources.size() > 0) {
				for (OecResConceptResource oecResConceptResource : resources) {
					ConceptResValue conceptResValue = new ConceptResValue(null,
							oecResConceptResource.getConcept_resource(),
							oecResConceptResource.getType(),
							oecResConceptResource.getValid_flg(), null, null,
							null);
					conceptResValues.add(conceptResValue);
				}
			}
			conceptResTree.setValues(conceptResValues);
			return conceptResTree;
		} else {
			//处理子节点
			List<ConceptResTree> newChilds=new ArrayList<ConceptResTree>();
			for(OecResConcept child : oecResConcept.getChildren()){
				ConceptResTree cr = transToConceptResTree(child);
				newChilds.add(cr);
			}
			// 节点信息
			ConceptResTree conceptResTree = new ConceptResTree(null, null,
					oecResConcept.getConcept_name(), null, null, null, 1L,
					null, oecResConcept.getDescription(), null, null, null);
			conceptResTree.setChildrens(newChilds);
			// 表达式
			List<OecResConceptResource> resources = oecResConcept
					.getResources();
			List<ConceptResValue> conceptResValues = new ArrayList<ConceptResValue>();
			if (resources != null && resources.size() > 0) {
				for (OecResConceptResource oecResConceptResource : resources) {
					ConceptResValue conceptResValue = new ConceptResValue(null,
							oecResConceptResource.getConcept_resource(),
							oecResConceptResource.getType(),
							oecResConceptResource.getValid_flg(), null, null,
							null);
					conceptResValues.add(conceptResValue);
				}
			}
			conceptResTree.setValues(conceptResValues);
			return conceptResTree;
		}
	}

	// 编号重新生成
	private void reBuiltId(List<ConceptResTree> roots){
		for (int i = 0; i < roots.size(); i++) {
			ConceptResTree conceptResTree=roots.get(i);
			conceptResTree.setSort(i+1);
			conceptResTree.setUserId(0L);
			conceptResTree.setParent_id(-1L);
			built(conceptResTree,0);
		}
	}
	
	private void built(ConceptResTree conceptResTree,int level){
		//节点
		conceptResTree.setId(PrimaryKeyGenerator.getKeya());
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(level==0){//根节点
			conceptResTree.setId_path(conceptResTree.getId()+"");
			conceptResTree.setLevel(level);
			conceptResTree.setTop_tree_Id(conceptResTree.getId());
		}else{
			conceptResTree.setId_path(conceptResTree.getId_path()+"->"+conceptResTree.getId());
			conceptResTree.setLevel(level);
		}
		//表达式
		List<ConceptResValue> values = conceptResTree.getValues();
		for (ConceptResValue conceptResValue : values) {
			conceptResValue.setConcept_id(conceptResTree.getId());
		}
		//子节点
		List<ConceptResTree> childrens = conceptResTree.getChildrens();
		for(int i=0;i<childrens.size();i++){
			ConceptResTree child=childrens.get(i);
			child.setId_path(conceptResTree.getId_path());
			child.setLangue_id(1L);
			child.setParent_id(conceptResTree.getId());
			child.setSort(i+1);
			child.setTop_tree_Id(conceptResTree.getTop_tree_Id());
			child.setUserId(0L);
			built(child,level+1);
		}
	}
	
	// 插入新库
	private void intoDB(Connection conn,List<ConceptResTree> roots) throws SQLException{
		int i=1;
		for (ConceptResTree conceptResTree : roots) {
			insert(conn,conceptResTree);
			System.out.println(roots.size()+"===="+i++);
		}
	}
	private void insert(Connection conn,ConceptResTree conceptResTree) throws SQLException{
		conceptResTree.insert(conn);
		if(conceptResTree.getChildrens()!=null && conceptResTree.getChildrens().size()>0){
			for (ConceptResTree child : conceptResTree.getChildrens()) {
				insert(conn,child);
			}
		}
	}
	// 领域资源迁移-----------------

	public static void main(String[] args) throws SQLException,
			ClassNotFoundException {
		new ConResTransfer().oecConceptResTransfer();
	}
}
