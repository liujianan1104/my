package com.dinfo.oec.resource;

import java.sql.Timestamp;

public class FieldCateWord {
	
	private Long id;
	
	private String word;
	
	private Integer valid_flg;
	
	private Timestamp ctime;
	
	private Timestamp utime;
	
	private Long cate_id;

	public FieldCateWord(Long id, String word, Integer valid_flg,
			Timestamp ctime, Timestamp utime, Long cate_id) {
		super();
		this.id = id;
		this.word = word;
		this.valid_flg = valid_flg;
		this.ctime = ctime;
		this.utime = utime;
		this.cate_id = cate_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getValid_flg() {
		return valid_flg;
	}

	public void setValid_flg(Integer valid_flg) {
		this.valid_flg = valid_flg;
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

	public Long getCate_id() {
		return cate_id;
	}

	public void setCate_id(Long cate_id) {
		this.cate_id = cate_id;
	}
	
}
