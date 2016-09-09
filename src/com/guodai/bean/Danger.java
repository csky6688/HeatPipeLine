package com.guodai.bean;

import com.baidu.mapapi.model.LatLng;

public class Danger {
	
	private String info;
	private int id;
	private String type;
	private LatLng loc;
	
	@Override
	public String toString() {
		return "Danger [info=" + info + ", id=" + id + ", type=" + type
				+ ", loc=" + loc + "]";
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getId() {
		return id;
	}
	public Danger(String info, int id, String type, LatLng loc) {
		super();
		this.info = info;
		this.id = id;
		this.type = type;
		this.loc = loc;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LatLng getLoc() {
		return loc;
	}
	public void setLoc(LatLng loc) {
		this.loc = loc;
	}
	
	

}
