package com.dinfo.oec.resource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

public class ConceptResTree {

	private Long id;

	private String id_path;

	private String name;

	private Long parent_id;

	private Integer level;

	private Integer sort;

	private Long langue_id;

	private Long top_tree_Id;

	private String note;

	private Long userId;
	
	private List<ConceptResTree> childrens;
	
	private List<ConceptResValue> values;

	public Long getId() {
		return id;
	}

	public ConceptResTree(){
		
	}
	
	public ConceptResTree(Long id, String id_path, String name, Long parent_id,
			Integer level, Integer sort, Long langue_id, Long top_tree_Id,
			String note, Long userId, List<ConceptResTree> childrens,
			List<ConceptResValue> values) {
		super();
		this.id = id;
		this.id_path = id_path;
		this.name = name;
		this.parent_id = parent_id;
		this.level = level;
		this.sort = sort;
		this.langue_id = langue_id;
		this.top_tree_Id = top_tree_Id;
		this.note = note;
		this.userId = userId;
		this.childrens = childrens;
		this.values = values;
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

	public Long getTop_tree_Id() {
		return top_tree_Id;
	}

	public void setTop_tree_Id(Long top_tree_Id) {
		this.top_tree_Id = top_tree_Id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<ConceptResTree> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<ConceptResTree> childrens) {
		this.childrens = childrens;
	}

	public List<ConceptResValue> getValues() {
		return values;
	}

	public void setValues(List<ConceptResValue> values) {
		this.values = values;
	}
	
	@Override
	public String toString() {
		return "ConceptResTree [id=" + id + ", id_path=" + id_path + ", name="
				+ name + ", parent_id=" + parent_id + ", level=" + level
				+ ", sort=" + sort + ", langue_id=" + langue_id
				+ ", top_tree_Id=" + top_tree_Id + ", note=" + note
				+ ", userId=" + userId + ", childrens=" + childrens.size()
				+ ", values=" + values.size() + "]";
	}
	
	public void insert(Connection conn) throws SQLException{
		String sql="";
		if(ConResTransfer.DB_TYPE.equalsIgnoreCase("oracle")){
			sql="insert into concept_res_tree (id,id_path,name,parent_id,\"LEVEL\",sort,langue_id,top_tree_id,note,ctime,utime,user_id)values(?,?,?,?,?,?,?,?,?,sysdate,sysdate,?)";
		}else{
			sql="insert into concept_res_tree (id,id_path,name,parent_id,level,sort,langue_id,top_tree_id,note,ctime,utime,user_id)values(?,?,?,?,?,?,?,?,?,now(),now(),?)";
		}
		List<Object> params=new ArrayList<Object>();
		params.add(this.id);
		params.add(this.id_path);
		params.add(this.name==null?" ":this.name);
		params.add(this.parent_id);
		params.add(this.level);
		params.add(this.sort);
		params.add(this.langue_id);
		params.add(this.top_tree_Id);
		params.add(this.note==null?"":this.note);
		params.add(this.userId);
		new QueryRunner().update(conn, sql, params.toArray());
		if(this.values!=null && this.values.size()>0){
			Object [][]params1=new Object[this.values.size()][4];
			if(ConResTransfer.DB_TYPE.equalsIgnoreCase("oracle")){
				sql="insert into concept_res_value(value,type,valid_flg,ctime,utime,concept_id)values(?,?,?,sysdate,sysdate,?)";
			}else{
				sql="insert into concept_res_value(value,type,valid_flg,ctime,utime,concept_id)values(?,?,?,now(),now(),?)";
			}
			for (int i = 0; i < this.values.size(); i++) {
				ConceptResValue conceptResValue = this.values.get(i);
				params1[i][0]=conceptResValue.getValue();
				params1[i][1]=conceptResValue.getType();
				params1[i][2]=conceptResValue.getValid_Flg();
				params1[i][3]=conceptResValue.getConcept_id();
			}
			new QueryRunner().batch(conn,sql, params1);
		}
	}
}
