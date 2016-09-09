package com.guodai.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.guodai.bean.HeatLine;
import com.guodai.heatpope.MyApplication;
import com.guodai.heatpope.R;
import com.guodai.utils.GeneralUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class User extends Activity {
	
	private TextView name;
	private TextView id;
	private TextView group;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private com.guodai.bean.User user;
	private MyApplication application;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);
		setTitle("用户");
		name = (TextView) findViewById(R.id.tv_user_name);
		id = (TextView) findViewById(R.id.tv_user_id);
		group = (TextView) findViewById(R.id.tv_user_group);
		
		mMapView=(MapView) findViewById(R.id.us_bmapView);
		mBaiduMap=mMapView.getMap();
		
		GeneralUtils.toAppointedMap(mBaiduMap, 40.671, 122.233391, 15);
		
		application=(MyApplication) getApplication();
		 user=application.getCurrentUser();
		 
		 name.setText(user.getName());
		 id.setText(user.getWorkId());
		 group.setText(user.getWorkGroup());
	}
	
	//转支巡检界面
	public void toPatrol(View view){
		Intent intent=new Intent(this, Patrol.class);
		intent.putExtra("show", true);
		startActivity(intent);
	}
	
	//显示今天的巡检任务
	public void showTaskOfTaday(View view){
		
		 List<HeatLine> list = user.getTaskMap().get("task1");
		for (int i = 0; i <list.size(); i++) {
			HeatLine heatLine = list.get(i);
			LatLng p1 = new LatLng(Double.parseDouble(heatLine.getL_Y1()), Double.parseDouble(heatLine.getL_X1()));
			LatLng p2 = new LatLng(Double.parseDouble(heatLine.getL_Y2()), Double.parseDouble(heatLine.getL_X2()));
			 List<LatLng> points = new ArrayList<LatLng>();
			  points.add(p1);
		        points.add(p2);
		        OverlayOptions ooPolyline = new PolylineOptions().width(10)
		                .color(0xAAFF0000).points(points);
		       mBaiduMap.addOverlay(ooPolyline);
		}	
	}
}
