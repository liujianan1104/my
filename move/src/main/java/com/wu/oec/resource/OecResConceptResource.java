package com.wu.oec.resource;

public class OecResConceptResource {
	private String concept_resource;

	private Integer type;

	private Integer valid_flg;

	private String concept_id;

	public String getConcept_resource() {
		return concept_resource;
	}

	public void setConcept_resource(String concept_resource) {
		this.concept_resource = concept_resource;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getValid_flg() {
		return valid_flg;
	}

	public void setValid_flg(Integer valid_flg) {
		this.valid_flg = valid_flg;
	}

	public String getConcept_id() {
		return concept_id;
	}

	public void setConcept_id(String concept_id) {
		this.concept_id = concept_id;
	}

}
