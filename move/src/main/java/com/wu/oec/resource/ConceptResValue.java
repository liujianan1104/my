package com.wu.oec.resource;

import java.sql.Timestamp;

public class ConceptResValue {
	
	private Long id;
	
	private String value;
	
	private Integer type;
	
	private Integer valid_Flg;
	
	private Timestamp ctime;
	
	private Timestamp utime;
	
	private Long concept_id;

	public ConceptResValue(Long id, String value, Integer type,
			Integer valid_Flg, Timestamp ctime, Timestamp utime, Long concept_id) {
		super();
		this.id = id;
		this.value = value;
		this.type = type;
		this.valid_Flg = valid_Flg;
		this.ctime = ctime;
		this.utime = utime;
		this.concept_id = concept_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getValid_Flg() {
		return valid_Flg;
	}

	public void setValid_Flg(Integer valid_Flg) {
		this.valid_Flg = valid_Flg;
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

	public Long getConcept_id() {
		return concept_id;
	}

	public void setConcept_id(Long concept_id) {
		this.concept_id = concept_id;
	}

	@Override
	public String toString() {
		return "ConceptResValue [id=" + id + ", value=" + value + ", type="
				+ type + ", valid_Flg=" + valid_Flg + ", ctime=" + ctime
				+ ", utime=" + utime + ", concept_id=" + concept_id + "]";
	}
	
}
