package com.wu.oec.resource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

public class FieldResTree {
	
	private Long id;
	
	private String id_path;
	
	private String name;
	
	private Long parent_id;
	
	private Integer level;
	
	private Integer sort;
	
	private Long langue_id;
	
	private Long top_tree_id;
	
	private String note;
	
	private Timestamp ctime;
	
	private Timestamp utime;
	
	private Long user_id;

	private List<FieldResTree> children;
	
	private List<FieldResValue> values;
	
	public FieldResTree(Long id, String id_path, String name, Long parent_id,
			Integer level, Integer sort, Long langue_id, Long top_tree_id,
			String note, Timestamp ctime, Timestamp utime, Long user_id,
			List<FieldResTree> children, List<FieldResValue> values) {
		super();
		this.id = id;
		this.id_path = id_path;
		this.name = name;
		this.parent_id = parent_id;
		this.level = level;
		this.sort = sort;
		this.langue_id = langue_id;
		this.top_tree_id = top_tree_id;
		this.note = note;
		this.ctime = ctime;
		this.utime = utime;
		this.user_id = user_id;
		this.children = children;
		this.values = values;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getId_path() {
		return id_path;
	}

	public void setId_path(String id_path) {
		this.id_path = id_path;
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

	public Long getLangue_id() {
		return langue_id;
	}

	public void setLangue_id(Long langue_id) {
		this.langue_id = langue_id;
	}

	public Long getTop_tree_id() {
		return top_tree_id;
	}

	public void setTop_tree_id(Long top_tree_id) {
		this.top_tree_id = top_tree_id;
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

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public List<FieldResTree> getChildren() {
		return children;
	}

	public void setChildren(List<FieldResTree> children) {
		this.children = children;
	}

	public List<FieldResValue> getValues() {
		return values;
	}

	public void setValues(List<FieldResValue> values) {
		this.values = values;
	}
	
	static int num=100;

	public void insert(Connection conn) throws SQLException {
		String sql="";
		if(FieldResTransfer.DB_TYPE.equalsIgnoreCase("oracle")){
			sql="insert into field_res_tree (id,id_path,name,parent_id,\"LEVEL\",sort,langue_id,top_tree_id,note,ctime,utime,user_id)values(?,?,?,?,?,?,?,?,?,sysdate,sysdate,?)";
		}else{
			sql="insert into field_res_tree (id,id_path,name,parent_id,level,sort,langue_id,top_tree_id,note,ctime,utime,user_id)values(?,?,?,?,?,?,?,?,?,now(),now(),?)";
		}
		List<Object> params=new ArrayList<Object>();
		params.add(this.id);
		params.add(this.id_path);
		params.add(this.name);
		params.add(this.parent_id);
		params.add(this.level);
		params.add(this.sort);
		params.add(this.langue_id);
		params.add(this.top_tree_id);
		params.add(this.note==null?"":this.note);
		params.add(this.user_id);
		new QueryRunner().update(conn, sql, params.toArray());
		if(this.values!=null && this.values.size()>0){
			Object [][]params1=new Object[this.values.size()][4];
			if(FieldResTransfer.DB_TYPE.equalsIgnoreCase("oracle")){
				sql="insert into field_res_value(value,type,valid_flg,ctime,utime,field_id)values(?,?,?,sysdate,sysdate,?)";
			}else{
				sql="insert into field_res_value(value,type,valid_flg,ctime,utime,field_id)values(?,?,?,NOW(),NOW(),?)";
			}
			for (int i = 0; i < this.values.size(); i++) {
				FieldResValue fieldResValue = this.values.get(i);
				params1[i][0]=fieldResValue.getValue();
				params1[i][1]=fieldResValue.getType();
				params1[i][2]=fieldResValue.getValid_flg();
				params1[i][3]=fieldResValue.getField_id();
			}
			new QueryRunner().batch(conn,sql, params1);
		}
	
	}
	
}
