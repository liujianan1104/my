package com.wu.oec.resource;

import com.wu.oec.utils.PrimaryKeyGenerator;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class FieldCateResTransfer {

	private Connection oldConn = null;

	private Connection newConn = null;

	public FieldCateResTransfer() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		oldConn = DriverManager
				.getConnection(
						"jdbc:mysql://127.0.0.1:3306/semantic_local?useUnicode=true&characterEncoding=utf8",
						"root", "root");
		newConn = DriverManager
				.getConnection(
						"jdbc:mysql://127.0.0.1:3306/semantic_local?useUnicode=true&characterEncoding=utf8",
						"root", "root");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<OecFieldCate> queryListOecFieldCate(Connection conn,
			String sql, Object... params) throws SQLException {
		return (List<OecFieldCate>) new QueryRunner().query(conn, sql,
				new BeanListHandler(OecFieldCate.class), params);
	}

	public static List<OecFieldCate> queryChildOecFieldCate(Connection conn,
			String parentId) throws SQLException {
		return queryListOecFieldCate(conn,
				"select * from oec_field_cate where cate_parent_id=?", parentId);
	}

	public static List<OecFieldCate> queryRootOecFieldCate(Connection conn)
			throws SQLException {
		return queryListOecFieldCate(conn,
				"select * from oec_field_cate where cate_level=?", 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<OecFieldWord> queryOecFieldWord(Connection conn,
			String cateId) throws SQLException {
		return (List<OecFieldWord>) new QueryRunner().query(conn,
				"select * from oec_field_word where field_cate_id=?",
				new BeanListHandler(OecFieldWord.class), cateId);
	}

	// 查询概念资源
	private List<OecFieldCate> queryOecFieldCates(Connection conn)
			throws SQLException {
		List<OecFieldCate> roots = queryRootOecFieldCate(conn);
		for (OecFieldCate oecField : roots) {
			queryAndFillOecFieldCate(conn, oecField);
		}
		return roots;
	}

	// 递归查询子节点和值数据
	private void queryAndFillOecFieldCate(Connection conn,
			OecFieldCate oecFieldCate) throws SQLException {
		// 查询概念资源值
		List<OecFieldWord> oecFieldWords = queryOecFieldWord(conn,
				oecFieldCate.getCate_id());
		oecFieldCate.setWords(oecFieldWords);
		// 查询子节点
		List<OecFieldCate> childs = queryChildOecFieldCate(conn,
				oecFieldCate.getCate_id());
		oecFieldCate.setChildren(childs);
		for (OecFieldCate child : childs) {
			queryAndFillOecFieldCate(conn, child);
		}
	}

	// 转换概念资源
	private List<FieldCateTree> transToFieldCateTrees(
			List<OecFieldCate> oecFieldCates) {
		List<FieldCateTree> fieldCateTrees = new ArrayList<FieldCateTree>();
		for (OecFieldCate oecFieldCate : oecFieldCates) {
			fieldCateTrees.add(transToFieldCateTree(oecFieldCate));
		}
		return fieldCateTrees;
	}

	private FieldCateTree transToFieldCateTree(OecFieldCate oecFieldCate) {
		if (oecFieldCate.getChildren() == null
				|| oecFieldCate.getChildren().size() == 0) {// 子节点
			// 节点信息
			FieldCateTree fieldCateTree = new FieldCateTree(null,
					oecFieldCate.getCate_name(), null, null, null, null,
					oecFieldCate.getValid_flg(), null,
					oecFieldCate.getDescription(), null, null, null, null);
			fieldCateTree.setChildren(new ArrayList<FieldCateTree>(0));
			// 表达式
			List<OecFieldWord> words = oecFieldCate.getWords();
			List<FieldCateWord> fieldCateWords = new ArrayList<FieldCateWord>();
			if (words != null && words.size() > 0) {
				for (OecFieldWord oecFieldWord : words) {
					FieldCateWord fieldCateWord = new FieldCateWord(null,
							oecFieldWord.getWord(),
							oecFieldWord.getValid_flg(), null, null, null);
					fieldCateWords.add(fieldCateWord);
				}
			}
			fieldCateTree.setWords(fieldCateWords);
			return fieldCateTree;
		} else {
			// 处理子节点
			List<FieldCateTree> newChilds = new ArrayList<FieldCateTree>();
			for (OecFieldCate child : oecFieldCate.getChildren()) {
				FieldCateTree cr = transToFieldCateTree(child);
				newChilds.add(cr);
			}
			// 节点信息
			FieldCateTree fieldCateTree = new FieldCateTree(null,
					oecFieldCate.getCate_name(), null, null, null, null,
					oecFieldCate.getValid_flg(), null,
					oecFieldCate.getDescription(), null, null, null, null);
			fieldCateTree.setChildren(newChilds);
			// 表达式
			List<OecFieldWord> words = oecFieldCate.getWords();
			List<FieldCateWord> fieldCateWords = new ArrayList<FieldCateWord>();
			if (words != null && words.size() > 0) {
				for (OecFieldWord oecFieldWord : words) {
					FieldCateWord fieldCateWord = new FieldCateWord(null,
							oecFieldWord.getWord(),
							oecFieldWord.getValid_flg(), null, null, null);
					fieldCateWords.add(fieldCateWord);
				}
			}
			fieldCateTree.setWords(fieldCateWords);
			return fieldCateTree;
		}
	}

	// 编号重新生成
	private void reBuiltId(List<FieldCateTree> roots) {
		for (int i = 0; i < roots.size(); i++) {
			FieldCateTree fieldCateTree = roots.get(i);
			fieldCateTree.setSort(i + 1);
			fieldCateTree.setUserId(0L);
			fieldCateTree.setParent_id(-1L);
			built(fieldCateTree, 0);
		}
	}

	private void built(FieldCateTree fieldCateTree, int level) {
		// 节点
		fieldCateTree.setId(PrimaryKeyGenerator.getKeya());
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (level == 0) {// 根节点
			fieldCateTree.setLevel(level);
			fieldCateTree.setTop_tree_id(fieldCateTree.getId());
		} else {
			fieldCateTree.setLevel(level);
		}
		// 表达式
		List<FieldCateWord> words = fieldCateTree.getWords();
		for (FieldCateWord fieldCateWord : words) {
			fieldCateWord.setCate_id(fieldCateTree.getId());
		}
		List<FieldCateTree> childrens = fieldCateTree.getChildren();
		for (int i = 0; i < childrens.size(); i++) {
			FieldCateTree child = childrens.get(i);
			child.setParent_id(fieldCateTree.getId());
			child.setSort(i + 1);
			child.setTop_tree_id(fieldCateTree.getTop_tree_id());
			child.setUserId(0L);
			built(child, level + 1);
		}

	}

	// 插入新库
	private void intoDB(Connection conn, List<FieldCateTree> roots)
			throws SQLException {
		int i = 1;
		for (FieldCateTree fieldResTree : roots) {
			insert(conn, fieldResTree);
			System.out.println(roots.size() + "====" + i++);
		}
	}

	private void insert(Connection conn, FieldCateTree fieldCateTree)
			throws SQLException {
		fieldCateTree.insert(conn);
		if (fieldCateTree.getChildren() != null
				&& fieldCateTree.getChildren().size() > 0) {
			for (FieldCateTree child : fieldCateTree.getChildren()) {
				insert(conn, child);
			}
		}
	}

	public void oecFieldCateTransfer() throws SQLException {
		// 查询数据
		List<OecFieldCate> roots = queryOecFieldCates(oldConn);
		System.out.println("查询完成");
		// 数据转换
		List<FieldCateTree> fieldCateTrees = transToFieldCateTrees(roots);
		System.out.println("转换完成");
		// 编号、路径、等级、排序编号重建
		reBuiltId(fieldCateTrees);
		System.out.println("编号生成完成");
		// 插入新库
		intoDB(newConn, fieldCateTrees);
		System.out.println("over");
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
//		new FieldCateResTransfer().oecFieldCateTransfer();
	}

}
