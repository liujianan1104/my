package com.dinfo.oec.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.dinfo.oec.tools.PrimaryKeyGenerator;


public class FieldResTransfer {
	public static String DB_TYPE="oracle";
	
	private Connection oldConn = null;

	private Connection newConn = null;
	
	public FieldResTransfer() throws SQLException, ClassNotFoundException {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<OecField> queryListOecField(Connection conn,
			String sql, Object... params) throws SQLException {
		return (List<OecField>) new QueryRunner().query(conn, sql,
				new BeanListHandler(OecField.class), params);
	}

	public static List<OecField> queryChildOecField(Connection conn,
			String parentId) throws SQLException {
		return queryListOecField(conn,
				"select * from oec_field where field_parent_id=?", parentId);
	}

	public static List<OecField> queryRootOecField(Connection conn)
			throws SQLException {
		return queryListOecField(conn,
				"select * from oec_field where field_level=?", 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<OecFieldResource> queryOecFieldResource(Connection conn,
			String conceptId) throws SQLException {
		return (List<OecFieldResource>) new QueryRunner().query(conn,
				"select * from oec_field_resource where field_id=?",
				new BeanListHandler(OecFieldResource.class), conceptId);
	}

	// 查询概念资源
	private List<OecField> queryOecFields(Connection conn) throws SQLException {
		List<OecField> roots = queryRootOecField(conn);
		for (OecField oecField : roots) {
			queryAndFillOecField(conn, oecField);
		}
		return roots;
	}

	// 递归查询子节点和值数据
	private void queryAndFillOecField(Connection conn, OecField oecField)
			throws SQLException {
		// 查询概念资源值
		List<OecFieldResource> oecResConceptResources = queryOecFieldResource(
				conn, oecField.getField_id());
		oecField.setResources(oecResConceptResources);
		// 查询子节点
		List<OecField> childs = queryChildOecField(conn, oecField.getField_id());
		oecField.setChildren(childs);
		for (OecField child : childs) {
			queryAndFillOecField(conn, child);
		}
	}

	// 转换概念资源
	private List<FieldResTree> transToFieldResTrees(List<OecField> oecFields) {
		List<FieldResTree> fieldResTrees = new ArrayList<FieldResTree>();
		for (OecField oecField : oecFields) {
			fieldResTrees.add(transToFieldResTree(oecField));
		}
		return fieldResTrees;
	}

	private FieldResTree transToFieldResTree(OecField oecField) {
		if (oecField.getChildren() == null
				|| oecField.getChildren().size() == 0) {// 子节点
			// 节点信息
			FieldResTree fieldResTree = new FieldResTree(null, null,
					oecField.getField_name(), null, null, null, null, null,
					oecField.getDescription(), null, null, null, null, null);
			fieldResTree.setChildren(new ArrayList<FieldResTree>(0));
			// 表达式
			List<OecFieldResource> resources = oecField.getResources();
			List<FieldResValue> fieldResValues = new ArrayList<FieldResValue>();
			if (resources != null && resources.size() > 0) {
				for (OecFieldResource oecFieldResource : resources) {
					FieldResValue fieldResValue = new FieldResValue(
							oecFieldResource.getField_resource(),
							oecFieldResource.getType(),
							oecFieldResource.getValid_flg(), null, null, null);
					fieldResValues.add(fieldResValue);
				}
			}
			fieldResTree.setValues(fieldResValues);
			return fieldResTree;
		} else {
			// 处理子节点
			List<FieldResTree> newChilds = new ArrayList<FieldResTree>();
			for (OecField child : oecField.getChildren()) {
				FieldResTree cr = transToFieldResTree(child);
				newChilds.add(cr);
			}
			// 节点信息
			FieldResTree fieldResTree = new FieldResTree(null, null,
					oecField.getField_name(), null, null, null, null, null,
					oecField.getDescription(), null, null, null, null, null);
			fieldResTree.setChildren(newChilds);
			// 表达式
			List<OecFieldResource> resources = oecField.getResources();
			List<FieldResValue> fieldResValues = new ArrayList<FieldResValue>();
			if (resources != null && resources.size() > 0) {
				for (OecFieldResource oecFieldResource : resources) {
					FieldResValue fieldResValue = new FieldResValue(
							oecFieldResource.getField_resource(),
							oecFieldResource.getType(),
							oecFieldResource.getValid_flg(), null, null, null);
					fieldResValues.add(fieldResValue);
				}
			}
			fieldResTree.setValues(fieldResValues);
			return fieldResTree;
		}
	}

	// 编号重新生成
	private void reBuiltId(List<FieldResTree> roots){
		for (int i = 0; i < roots.size(); i++) {
			FieldResTree fieldResTree=roots.get(i);
			fieldResTree.setSort(i+1);
			fieldResTree.setUser_id(0L);
			fieldResTree.setLangue_id(1L);
			fieldResTree.setParent_id(-1L);
			built(fieldResTree,0);
		}
	}
	
	private void built(FieldResTree fieldResTree,int level){
		//节点
		fieldResTree.setId(PrimaryKeyGenerator.getKeya());
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(level==0){//根节点
			fieldResTree.setId_path(fieldResTree.getId()+"");
			fieldResTree.setLevel(level);
			fieldResTree.setLangue_id(1L);
			fieldResTree.setTop_tree_id(fieldResTree.getId());
		}else{
			fieldResTree.setId_path(fieldResTree.getId_path()+"->"+fieldResTree.getId());
			fieldResTree.setLevel(level);
			fieldResTree.setLangue_id(1L);
		}
		//表达式
		List<FieldResValue> values = fieldResTree.getValues();
		for (FieldResValue fieldResValue : values) {
			fieldResValue.setField_id(fieldResTree.getId());
		}
		//子节点
		List<FieldResTree> childrens = fieldResTree.getChildren();
		for(int i=0;i<childrens.size();i++){
			FieldResTree child=childrens.get(i);
			child.setId_path(fieldResTree.getId_path());
			child.setLangue_id(1L);
			child.setParent_id(fieldResTree.getId());
			child.setSort(i+1);
			child.setTop_tree_id(fieldResTree.getTop_tree_id());
			child.setUser_id(0L);
			built(child,level+1);
		}
	}

	// 插入新库
	private void intoDB(Connection conn,List<FieldResTree> roots) throws SQLException{
		int i=1;
		for (FieldResTree fieldResTree : roots) {
			insert(conn,fieldResTree);
			System.out.println(roots.size()+"===="+i++);
		}
	}
	
	private void insert(Connection conn,FieldResTree fieldResTree) throws SQLException{
		fieldResTree.insert(conn);
		if(fieldResTree.getChildren()!=null && fieldResTree.getChildren().size()>0){
			for (FieldResTree child : fieldResTree.getChildren()) {
				insert(conn,child);
			}
		}
	}
		
	
	public void oecFieldTransfer() throws SQLException {
		// 查询数据
		List<OecField> roots = queryOecFields(oldConn);
		System.out.println("查询完成");
		// 数据转换
		List<FieldResTree> fieldResTrees = transToFieldResTrees(roots);
		System.out.println("转换完成");
		// 编号、路径、等级、排序编号重建
		reBuiltId(fieldResTrees);
		System.out.println("编号生成完成");
		// 插入新库
		intoDB(newConn,fieldResTrees);
		System.out.println("over");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new FieldResTransfer().oecFieldTransfer();
	}
	
}
