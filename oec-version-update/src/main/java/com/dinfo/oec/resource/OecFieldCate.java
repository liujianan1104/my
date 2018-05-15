package com.dinfo.oec.resource;

import java.util.List;

public class OecFieldCate {
	
	private String cate_id;
	
	private String cate_name;
	
	private Integer valid_flg;
	
	private String description;
	
	private List<OecFieldCate> children;
	
	private List<OecFieldWord> words;
	
	public String getCate_id() {
		return cate_id;
	}

	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}

	public String getCate_name() {
		return cate_name;
	}

	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}

	public Integer getValid_flg() {
		return valid_flg;
	}

	public void setValid_flg(Integer valid_flg) {
		this.valid_flg = valid_flg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public List<OecFieldCate> getChildren() {
		return children;
	}

	
	public void setChildren(List<OecFieldCate> children) {
		this.children = children;
	}

	
	public List<OecFieldWord> getWords() {
		return words;
	}

	
	public void setWords(List<OecFieldWord> words) {
		this.words = words;
	}
}
