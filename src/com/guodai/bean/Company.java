package com.guodai.bean;

//初始化热站数据时，需要的公司名
public class Company {
	
	private String HS_ID;	
	private String HS_NAME;
	private String HS_NOTE;
	private String HS_PHOTOPATH;
	public String getHS_ID() {
		return HS_ID;
	}
	public void setHS_ID(String hS_ID) {
		HS_ID = hS_ID;
	}
	public String getHS_NAME() {
		return HS_NAME;
	}
	public void setHS_NAME(String hS_NAME) {
		HS_NAME = hS_NAME;
	}
	public String getHS_NOTE() {
		return HS_NOTE;
	}
	@Override
	public String toString() {
		return "Company [HS_ID=" + HS_ID + ", HS_NAME=" + HS_NAME
				+ ", HS_NOTE=" + HS_NOTE + ", HS_PHOTOPATH=" + HS_PHOTOPATH
				+ "]";
	}
	public Company() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Company(String hS_ID, String hS_NAME, String hS_NOTE,
			String hS_PHOTOPATH) {
		super();
		HS_ID = hS_ID;
		HS_NAME = hS_NAME;
		HS_NOTE = hS_NOTE;
		HS_PHOTOPATH = hS_PHOTOPATH;
	}
	public void setHS_NOTE(String hS_NOTE) {
		HS_NOTE = hS_NOTE;
	}
	public String getHS_PHOTOPATH() {
		return HS_PHOTOPATH;
	}
	public void setHS_PHOTOPATH(String hS_PHOTOPATH) {
		HS_PHOTOPATH = hS_PHOTOPATH;
	}


}
