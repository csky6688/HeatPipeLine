package com.guodai.bean;

//处理小组组长类
public class GroupLeader {
	
	private String name;//姓名
	private String workId;//工号
	private String workGroup;//单位
	private String tel;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public String getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}
	public String getTel() {
		return tel;
	}
	public GroupLeader(String name, String workId, String workGroup, String tel) {
		super();
		this.name = name;
		this.workId = workId;
		this.workGroup = workGroup;
		this.tel = tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	

}
