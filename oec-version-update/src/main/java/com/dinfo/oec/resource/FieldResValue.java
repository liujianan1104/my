package com.dinfo.oec.resource;

import java.sql.Timestamp;

public class FieldResValue {
	
	private String value;
	
	private Integer type;
	
	private Integer valid_flg;
	
	private Long field_id;
	
	private Timestamp ctime;
	
	private Timestamp utime;

	public FieldResValue(String value, Integer type, Integer valid_flg,
			Long field_id, Timestamp ctime, Timestamp utime) {
		super();
		this.value = value;
		this.type = type;
		this.valid_flg = valid_flg;
		this.field_id = field_id;
		this.ctime = ctime;
		this.utime = utime;
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

	public Integer getValid_flg() {
		return valid_flg;
	}

	public void setValid_flg(Integer valid_flg) {
		this.valid_flg = valid_flg;
	}

	public Long getField_id() {
		return field_id;
	}

	public void setField_id(Long field_id) {
		this.field_id = field_id;
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
}
