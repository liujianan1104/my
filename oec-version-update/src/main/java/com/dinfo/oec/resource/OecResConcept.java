package com.dinfo.oec.resource;

import java.util.List;

public class OecResConcept {

	private String concept_id;

	private String concept_name;

	private Integer concept_level;

	private String concept_parent_id;

	private Integer langueId;

	private Integer concept_sort;

	private String description;

	private List<OecResConcept> children;
	
	private List<OecResConceptResource> resources;
	
	public String getConcept_id() {
		return concept_id;
	}

	public void setConcept_id(String concept_id) {
		this.concept_id = concept_id;
	}

	public String getConcept_name() {
		return concept_name;
	}

	public void setConcept_name(String concept_name) {
		this.concept_name = concept_name;
	}

	public Integer getConcept_level() {
		return concept_level;
	}

	public void setConcept_level(Integer concept_level) {
		this.concept_level = concept_level;
	}

	public String getConcept_parent_id() {
		return concept_parent_id;
	}

	public void setConcept_parent_id(String concept_parent_id) {
		this.concept_parent_id = concept_parent_id;
	}

	public Integer getLangueId() {
		return langueId;
	}

	public void setLangueId(Integer langueId) {
		this.langueId = langueId;
	}

	public Integer getConcept_sort() {
		return concept_sort;
	}

	public void setConcept_sort(Integer concept_sort) {
		this.concept_sort = concept_sort;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<OecResConcept> getChildren() {
		return children;
	}

	public void setChildren(List<OecResConcept> children) {
		this.children = children;
	}

	public List<OecResConceptResource> getResources() {
		return resources;
	}

	public void setResources(List<OecResConceptResource> resources) {
		this.resources = resources;
	}

	@Override
	public String toString() {
		return "OecResConcept [concept_id=" + concept_id + ", concepzt_name="
				+ concept_name + ", concept_level=" + concept_level
				+ ", concept_parent_id=" + concept_parent_id + ", langueId="
				+ langueId + ", concept_sort=" + concept_sort
				+ ", description=" + description + "]";
	}

}
