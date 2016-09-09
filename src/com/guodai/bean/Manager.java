package com.guodai.bean;

import java.util.List;

//事故处理小组类
public class Manager {
	
	private String group;
	private String id;
	private List<String> team;//组员姓名
	private GroupLeader leader;
	private boolean status;//状态
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getTeam() {
		return team;
	}
	public void setTeam(List<String> team) {
		this.team = team;
	}
	public GroupLeader getLeader() {
		return leader;
	}
	public void setLeader(GroupLeader leader) {
		this.leader = leader;
	}
	public Manager() {
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Manager(String group, String id, List<String> team, GroupLeader leader,
			boolean status) {
		super();
		this.group = group;
		this.id = id;
		this.team = team;
		this.leader = leader;
		this.status = status;
	}
	
	
	

}
