package com.guodai.bean;

public class HeatLine {
	private String L_CALIBER;
	private String L_MATERIAL;
	private String L_PHOTOPATH;
	private int L_ID;
	private String L_X1;
	private String L_Y1;
	private String L_X2;
	private  String L_Y2;
	private String DELEFLAG;
	
	public String getL_CALIBER() {
		return L_CALIBER;
	}
	public void setL_CALIBER(String l_CALIBER) {
		L_CALIBER = l_CALIBER;
	}
	public String getL_MATERIAL() {
		return L_MATERIAL;
	}
	public void setL_MATERIAL(String l_MATERIAL) {
		L_MATERIAL = l_MATERIAL;
	}
	public String getL_PHOTOPATH() {
		return L_PHOTOPATH;
	}
	public void setL_PHOTOPATH(String l_PHOTOPATH) {
		L_PHOTOPATH = l_PHOTOPATH;
	}
	public int getL_ID() {
		return L_ID;
	}
	public void setL_ID(int l_ID) {
		L_ID = l_ID;
	}
	public String getL_X1() {
		return L_X1;
	}
	public void setL_X1(String l_X1) {
		L_X1 = l_X1;
	}
	public String getL_Y1() {
		return L_Y1;
	}
	public void setL_Y1(String l_Y1) {
		L_Y1 = l_Y1;
	}
	public String getL_X2() {
		return L_X2;
	}
	public void setL_X2(String l_X2) {
		L_X2 = l_X2;
	}
	public String getL_Y2() {
		return L_Y2;
	}
	public HeatLine() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "HeatLine [L_CALIBER=" + L_CALIBER + ", L_MATERIAL="
				+ L_MATERIAL + ", L_PHOTOPATH=" + L_PHOTOPATH + ", L_ID="
				+ L_ID + ", L_X1=" + L_X1 + ", L_Y1=" + L_Y1 + ", L_X2=" + L_X2
				+ ", L_Y2=" + L_Y2 + ", DELEFLAG=" + DELEFLAG + "]";
	}
	public HeatLine(String l_CALIBER, String l_MATERIAL, String l_PHOTOPATH,
			int l_ID, String l_X1, String l_Y1, String l_X2, String l_Y2,
			String dELEFLAG) {
		super();
		L_CALIBER = l_CALIBER;
		L_MATERIAL = l_MATERIAL;
		L_PHOTOPATH = l_PHOTOPATH;
		L_ID = l_ID;
		L_X1 = l_X1;
		L_Y1 = l_Y1;
		L_X2 = l_X2;
		L_Y2 = l_Y2;
		DELEFLAG = dELEFLAG;
	}
	public void setL_Y2(String l_Y2) {
		L_Y2 = l_Y2;
	}
	public String getDELEFLAG() {
		return DELEFLAG;
	}
	public void setDELEFLAG(String dELEFLAG) {
		DELEFLAG = dELEFLAG;
	}
	
	

}
