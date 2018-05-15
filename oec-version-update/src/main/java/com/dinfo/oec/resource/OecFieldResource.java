package com.dinfo.oec.resource;

public class OecFieldResource {
	
	private String field_resource;
	
	private Integer type;
	
	private Integer valid_flg;

	public String getField_resource() {
		return field_resource;
	}

	public void setField_resource(String field_resource) {
		this.field_resource = field_resource;
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

	@Override
	public String toString() {
		return "OecFieldResource [field_resource=" + field_resource + ", type="
				+ type + ", valid_flg=" + valid_flg + "]";
	}
	
}
