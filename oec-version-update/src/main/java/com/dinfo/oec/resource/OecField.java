package com.dinfo.oec.resource;

import java.util.List;

public class OecField {
	
	private String field_id;
	
	private String field_name;

	private String description;

	private List<OecField> children;

	private List<OecFieldResource> resources;

	public String getField_id() {
		return field_id;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<OecField> getChildren() {
		return children;
	}

	public void setChildren(List<OecField> children) {
		this.children = children;
	}

	public List<OecFieldResource> getResources() {
		return resources;
	}

	public void setResources(List<OecFieldResource> resources) {
		this.resources = resources;
	}

}
