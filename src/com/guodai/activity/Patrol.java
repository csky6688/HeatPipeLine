package com.guodai.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.guodai.bean.HeatLine;
import com.guodai.bean.User;
import com.guodai.heatpope.MyApplication;
import com.guodai.heatpope.R;
import com.guodai.utils.GeneralUtils;

public class Patrol extends Activity {
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private DatePickerDialog dateDialog;
	private int year,monthOfYear,dayOfMonth;
	private TextView textView;
	private Date date;
	private User user;
	private MyApplication application;
	LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner(); // 位置监听
	private LocationMode mCurrentMode = LocationMode.COMPASS; // 定位模式
	BitmapDescriptor mCurrentMarker = null;
	private boolean isFirstLoc=true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol);
		mMapView  = (MapView) findViewById(R.id.patrol_bmapView);
		mBaiduMap = mMapView.getMap();
		
		// 加载营口市地图
		GeneralUtils.toAppointedMap(mBaiduMap, 40.671, 122.233391, 15);
		//初始化日历对话框的日期
		Calendar calendar=Calendar.getInstance();
		
		year=calendar.get(calendar.YEAR);
		monthOfYear=calendar.get(calendar.MONTH)+1;
		dayOfMonth=calendar.get(calendar.DAY_OF_MONTH);
		
		application=(MyApplication) getApplication();
		user=application.getCurrentUser();
		//判断启动来源
		Intent intent = getIntent();
		if (intent.getBooleanExtra("show", false)) {
			drawTaskLine(user.getTaskMap().get("task1"));
		}
		
	}
	
	
	//绘制actionbar的菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater=getMenuInflater();
		menuInflater.inflate(R.menu.patrol, menu);
		textView = (TextView) menu.findItem(R.id.dateString).getActionView();
		textView.setEnabled(false);
		return super.onCreateOptionsMenu(menu);
	}
	
	//为actionbar上的按钮添加事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyApplication application = (MyApplication) getApplication();
		user=application.getCurrentUser();
		
		date=new Date(year-1900, monthOfYear, dayOfMonth);
		String today = (String) DateFormat.format("yyyy-MM-dd", date);
		
		switch (item.getItemId()) {
		case R.id.date://弹出日历对话框
			dateDialog=new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog_MinWidth, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					year=view.getYear();
					monthOfYear=view.getMonth();
					dayOfMonth=view.getDayOfMonth();
					date=new Date(year-1900, monthOfYear, dayOfMonth);//必须减去1900
					textView.setEnabled(true);
				
					String format = (String) DateFormat.format("yyyy-MM-dd", date);
					textView.setText(format);
				}
			},  year, monthOfYear-1, dayOfMonth);
			dateDialog.show();
			return true;
		case R.id.task:
			Map<String, List<HeatLine>> taskMap = user.getTaskMap();
			
			if (textView.isEnabled()) {
				String text = (String) textView.getText();
				if (text.equals(today)) {
					List<HeatLine> list = taskMap.get("task1");
					drawTaskLine(list);
				}else {
					List<HeatLine> list = taskMap.get("task2");
					drawTaskLine(list);
				}
				return true;
			}
			return false;
			
		case R.id.review:
			Map<String, List<LatLng>> historyMap = user.getHistoryMap();
			if (textView.isEnabled()) {
				String text = (String) textView.getText();
				if (text.equals(today)) {
					List<LatLng> list = historyMap.get("history1");
					drawHistoryLine(list);
				}else {
					List<LatLng> list = historyMap.get("history2");
					drawHistoryLine(list);
				}
				return true;
			}
			return false;
			
		case R.id.clear:
			mBaiduMap.clear();
			return true;
		default:return super.onOptionsItemSelected(item);
			
		}
		 
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
	}
	
	//绘制任务路线
	public void drawTaskLine(List<HeatLine> list){
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
	
	//绘制轨迹回放
	public void drawHistoryLine(List<LatLng> list){
		 OverlayOptions ooPolyline = new PolylineOptions().width(10)
	                .color(0xAA00FF00).points(list);
		
	     Polyline line = (Polyline) mBaiduMap.addOverlay(ooPolyline);
	     line.setDottedLine(true);
	    
	}
	
	public void initLoc(View view){
		mBaiduMap.setMyLocationEnabled(true);
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.worker);
		mBaiduMap
				.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, true, mCurrentMarker));
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	
	//定位函数监听类
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

}
