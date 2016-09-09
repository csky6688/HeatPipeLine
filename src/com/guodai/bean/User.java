package com.guodai.bean;

import java.util.List;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;


public class User {
	
	private String username;
	private String password;
	private String name;//姓名
	private String workId;//工号
	private String workGroup;//单位
	private Map<String, List<HeatLine>> taskMap;//任务路线
	private Map<String, List<LatLng>> historyMap;//历史路线
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkId() {
		return workId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	
	
	public Map<String, List<HeatLine>> getTaskMap() {
		return taskMap;
	}
	public void setTaskMap(Map<String, List<HeatLine>> taskMap) {
		this.taskMap = taskMap;
	}
	public Map<String, List<LatLng>> getHistoryMap() {
		return historyMap;
	}
	public void setHistoryMap(Map<String, List<LatLng>> historyMap) {
		this.historyMap = historyMap;
	}
	public User(String username, String password, String name, String workId,
			String workGroup) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.workId = workId;
		this.workGroup = workGroup;
	}
	
	
	
	

}
