package com.guodai.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Heatsite implements Serializable{

	private static final long serialVersionUID = 1L;
	private String HES_ID;
	private String HS_ID;

	private String HES_NAME;
	private String HES_TYPE;
	private String HES_X;
	private String HES_Y;
	private String HES_MONITORID;

	private String HES_PHOTOPATH_3D;
	private String HES_PHOTOPATH_2D;
	public String getHES_ID() {
		return HES_ID;
	}
	public void setHES_ID(String hES_ID) {
		HES_ID = hES_ID;
	}
	public String getHS_ID() {
		return HS_ID;
	}
	@Override
	public String toString() {
		return "Heatsite [HES_ID=" + HES_ID + ", HS_ID=" + HS_ID
				+ ", HES_NAME=" + HES_NAME + ", HES_TYPE=" + HES_TYPE
				+ ", HES_X=" + HES_X + ", HES_Y=" + HES_Y + ", HES_MONITORID="
				+ HES_MONITORID + ", HES_PHOTOPATH_3D=" + HES_PHOTOPATH_3D
				+ ", HES_PHOTOPATH_2D=" + HES_PHOTOPATH_2D + ", DELEFLAG="
				+ DELEFLAG + "]";
	}
	public Heatsite() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Heatsite(String hES_ID, String hS_ID, String hES_NAME, String hES_TYPE,
			String hES_X, String hES_Y, String hES_MONITORID,
			String hES_PHOTOPATH_3D, String hES_PHOTOPATH_2D, String dELEFLAG) {
		super();
		HES_ID = hES_ID;
		HS_ID = hS_ID;
		HES_NAME = hES_NAME;
		HES_TYPE = hES_TYPE;
		HES_X = hES_X;
		HES_Y = hES_Y;
		HES_MONITORID = hES_MONITORID;
		HES_PHOTOPATH_3D = hES_PHOTOPATH_3D;
		HES_PHOTOPATH_2D = hES_PHOTOPATH_2D;
		DELEFLAG = dELEFLAG;
	}
	public void setHS_ID(String hS_ID) {
		HS_ID = hS_ID;
	}
	public String getHES_NAME() {
		return HES_NAME;
	}
	public void setHES_NAME(String hES_NAME) {
		HES_NAME = hES_NAME;
	}
	public String getHES_TYPE() {
		return HES_TYPE;
	}
	public void setHES_TYPE(String hES_TYPE) {
		HES_TYPE = hES_TYPE;
	}
	public String getHES_X() {
		return HES_X;
	}
	public void setHES_X(String hES_X) {
		HES_X = hES_X;
	}
	public String getHES_Y() {
		return HES_Y;
	}
	public void setHES_Y(String hES_Y) {
		HES_Y = hES_Y;
	}
	public String getHES_MONITORID() {
		return HES_MONITORID;
	}
	public void setHES_MONITORID(String hES_MONITORID) {
		HES_MONITORID = hES_MONITORID;
	}
	public String getHES_PHOTOPATH_3D() {
		return HES_PHOTOPATH_3D;
	}
	public void setHES_PHOTOPATH_3D(String hES_PHOTOPATH_3D) {
		HES_PHOTOPATH_3D = hES_PHOTOPATH_3D;
	}
	public String getHES_PHOTOPATH_2D() {
		return HES_PHOTOPATH_2D;
	}
	public void setHES_PHOTOPATH_2D(String hES_PHOTOPATH_2D) {
		HES_PHOTOPATH_2D = hES_PHOTOPATH_2D;
	}
	public String getDELEFLAG() {
		return DELEFLAG;
	}
	public void setDELEFLAG(String dELEFLAG) {
		DELEFLAG = dELEFLAG; 
	}
	private String DELEFLAG;
	
}
