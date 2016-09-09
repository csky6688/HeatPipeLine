package com.guodai.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.guodai.bean.Charge;
import com.guodai.bean.Danger;
import com.guodai.bean.GroupLeader;
import com.guodai.bean.Manager;

public class GeneralUtils {
	
	public static List<Charge> charges=new ArrayList<Charge>();
	
	public static List<Danger> dangers=new ArrayList<Danger>();
	
	
	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：加载以指定经纬度为中心点的地图
	 */
	public static void toAppointedMap(BaiduMap map, double lat, double lon, float zoom) {
		// 自定义一个经纬度
		LatLng latLng = new LatLng(lat, lon);
		// 自定义地图状态
		MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(zoom)
				.build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mapStatus);
		// 更新地图状态，加载位置。
		map.animateMapStatus(u);
	}
	
	public static void init(){
		
		List<String> group11=new ArrayList<String>();
		group11.add("张强");
		group11.add("杨帅伟");
		
		List<String> group12=new ArrayList<String>();
		group12.add("姜恒远");
		group12.add("王凯");
		
		List<String> group21=new ArrayList<String>();
		group21.add("李军");
		group21.add("杨帅伟");
		
		List<String> group22=new ArrayList<String>();
		group22.add("邓志勇");
		group22.add("丁钊兴");
		
		List<String> group31=new ArrayList<String>();
		group31.add("董明杰");
		group31.add("杜晓刚");
		
		List<String> group41=new ArrayList<String>();
		group41.add("樊赐贵");
		group41.add("范海峰");
		
		
		List<Manager> managers1=new ArrayList<Manager>();
		Manager manager11=new Manager("一公司第一检修组", "F001", group11, new GroupLeader("王兵", "10017", "第一检修组", "13713563378"),false);
		Manager manager12=new Manager("一公司第二检修组", "F002", group12, new GroupLeader("张晓伟", "10023", "第二检修组", "13939835554"),true);
		managers1.add(manager11);
		managers1.add(manager12);
		
		List<Manager> managers2=new ArrayList<Manager>();
		Manager manager21=new Manager("二公司第一检修组", "F003", group21, new GroupLeader("陈飞", "20014", "第一检修组", "13933569412"),true);
		Manager manager22=new Manager("二公司第二检修组", "F004", group22, new GroupLeader("周崇建", "20035", "第二检修组", "15735628114"),false);
		managers2.add(manager21);
		managers2.add(manager22);
		
		List<Manager> managers3=new ArrayList<Manager>();
		Manager manager31=new Manager("三公司第一检修组", "F005", group31, new GroupLeader("李继伟", "30008", "第一检修组", "15938346691"),false);
		managers3.add(manager31);
		
		List<Manager> managers4=new ArrayList<Manager>();
		Manager manager41=new Manager("四公司第二检修组", "F006", group41, new GroupLeader("宋康", "40012", "第一检修组", "13233656157"),true);
		managers4.add(manager41);
		
		Charge charge3=new Charge("三公司", new LatLng(40.67518642578062, 122.27100396147878), managers1);
		Charge charge2=new Charge("二公司", new LatLng(40.67223165082538, 122.23296072313121), managers2);
		Charge charge1=new Charge("一公司", new LatLng(40.663811186361, 122.22786733088964), managers3);
		Charge charge4=new Charge("四公司", new LatLng(40.66872952443561, 122.25045971446558), managers4);
		
		setChargeStatus(charge1);
		setChargeStatus(charge2);
		setChargeStatus(charge3);
		setChargeStatus(charge4);
		
		charges.add(charge1);
		charges.add(charge2);
		charges.add(charge3);
		charges.add(charge4);
		
		//初始化模拟危险信息
		Danger danger1=new Danger("燃气泄漏，已进行区域关阀", 001, "泄漏", new LatLng(40.627195121393821,122.24780971321114));
		Danger danger2=new Danger("管道气压异常，已停气", 002, "异常", new LatLng(40.669358826432855,122.2376319117831));
		
		dangers.add(danger1);
		dangers.add(danger2);
		
	}
	
	//计算每个公司的出勤状态
	public static void setChargeStatus(Charge charge){
		List<Manager> managers = charge.getCharge();
		boolean status=false;
		for (Iterator iterator = managers.iterator(); iterator.hasNext();) {
			Manager manager = (Manager) iterator.next();
			status=manager.isStatus()|status;
		}
		charge.setStatus(status);
	}
	

	
}
