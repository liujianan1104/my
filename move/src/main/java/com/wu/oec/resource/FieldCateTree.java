package com.wu.oec.resource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

public class FieldCateTree {
	
	private Long id;
	
	private String name;
	
	
	private Integer level;
	
	private Integer sort;
	
	private Long parent_id;
	
	private Long top_tree_id;

	private Integer valid_flg;
	
	private Long userId;

	private String note;
	
	private Timestamp ctime;
	
	private Timestamp utime;
	
	private List<FieldCateTree> children;
	
	private List<FieldCateWord> words;
	
	public FieldCateTree(Long id, String name, Integer level, Integer sort,
			Long parent_id, Long top_tree_id, Integer valid_flg, Long userId,
			String note, Timestamp ctime, Timestamp utime,
			List<FieldCateTree> children, List<FieldCateWord> words) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.sort = sort;
		this.parent_id = parent_id;
		this.top_tree_id = top_tree_id;
		this.valid_flg = valid_flg;
		this.userId = userId;
		this.note = note;
		this.ctime = ctime;
		this.utime = utime;
		this.children = children;
		this.words = words;
	}



	public Long getTop_tree_id() {
		return top_tree_id;
	}



	public void setTop_tree_id(Long top_tree_id) {
		this.top_tree_id = top_tree_id;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getValid_flg() {
		return valid_flg;
	}

	public void setValid_flg(Integer valid_flg) {
		this.valid_flg = valid_flg;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Timestamp getCtime() {
		return ctime;
	}

	public void setCtime(Timestamp ctime) {
		this.ctime = ctime;
	}

	public Timestamp getUtime() {
		return utime;
	}

	public void setUtime(Timestamp utime) {
		this.utime = utime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<FieldCateTree> getChildren() {
		return children;
	}

	public void setChildren(List<FieldCateTree> children) {
		this.children = children;
	}

	public List<FieldCateWord> getWords() {
		return words;
	}

	public void setWords(List<FieldCateWord> words) {
		this.words = words;
	}


	public void insert(Connection conn) throws SQLException {
		String sql="insert into field_cate_tree (id,name,level,sort,parent_id,top_tree_id,valid_flg,user_id,note,ctime,utime)values(?,?,?,?,?,?,?,?,?,now(),now())";
		List<Object> params=new ArrayList<Object>();
		params.add(this.id);
		params.add(this.name);
		params.add(this.level);
		params.add(this.sort);
		params.add(this.parent_id);
		params.add(this.top_tree_id);
		params.add(this.valid_flg);
		params.add(this.userId);
		params.add(this.note==null?"":this.note);
		new QueryRunner().update(conn, sql, params.toArray());
		if(this.words!=null && this.words.size()>0){
			Object [][]params1=new Object[this.words.size()][3];
			sql="insert field_cate_word(cate_id,word,valid_flg,ctime,utime)values(?,?,?,now(),now())";
			for (int i = 0; i < this.words.size(); i++) {
				FieldCateWord fieldCateWord = this.words.get(i);
				params1[i][0]=fieldCateWord.getCate_id();
				params1[i][1]=fieldCateWord.getWord();
				params1[i][2]=fieldCateWord.getValid_flg();
			}
			new QueryRunner().batch(conn,sql, params1);
		}
	
	}
	
	
}
