package com.guodai.bean;

import java.io.Serializable;
import java.util.List;

import com.baidu.mapapi.model.LatLng;

//负责处理事故的类,即热力公司类
public class Charge implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;//公司名称
	private LatLng loc;//公司坐标
	private List<Manager> charge;//公司的事故处理组
	private boolean status;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LatLng getLoc() {
		return loc;
	}
	public void setLoc(LatLng loc) {
		this.loc = loc;
	}
	public List<Manager> getCharge() {
		return charge;
	}
	public void setCharge(List<Manager> charge) {
		this.charge = charge;
	}
	
	public Charge() {
	}
	
	public Charge(String name, LatLng loc, List<Manager> charge) {
		super();
		this.name = name;
		this.loc = loc;
		this.charge = charge;
	}
	
	

}
