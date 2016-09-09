package com.guodai.activity;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.guodai.adapter.ManagerAdapter;
import com.guodai.bean.Charge;
import com.guodai.bean.Manager;
import com.guodai.heatpope.R;
import com.guodai.utils.DrivingRouteOverlay;
import com.guodai.utils.GeneralUtils;
import com.guodai.utils.OverlayManager;

public class Danger extends Activity implements OnGetRoutePlanResultListener{
	
	private static final int NOTIFICATION_ID_1 = 0;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private ListView team;
	private RoutePlanSearch mSearch = null;
	private BitmapDescriptor company=BitmapDescriptorFactory.fromResource(R.drawable.company);;
	private BitmapDescriptor dangerSource=BitmapDescriptorFactory.fromResource(R.drawable.danger);;
	private MarkerOptions companyMarkerOptions;
	private MarkerOptions dangerMarkerOptions;
	private InfoWindow mInfoWindow;
	private ManagerAdapter mAdapter;
	private OnClickListener call;
	private Marker dangerMarker;
	//用于显示通知
	private Bitmap icon;
	private NotificationManager manager;
	private PendingIntent pIntent;
	
	//路径规划相关
	private  DrivingRouteLine route = null;
	private   OverlayManager routeOverlay = null;
	private  DrivingRouteLine shortestRoute=null;
	private com.guodai.bean.Danger danger;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.danger);
		mMapView  = (MapView) findViewById(R.id.danger_bmapView);
		mBaiduMap = mMapView.getMap();
		team = (ListView) findViewById(R.id.danger_team);
		
		
		//加载公司marker
		companyMarkerOptions = new MarkerOptions().perspective(false)
				.anchor(0.5f, 0.5f).zIndex(6).icon(company)
				.animateType(MarkerAnimateType.grow);
		
		dangerMarkerOptions = new MarkerOptions().perspective(false)
				.anchor(0.5f, 0.5f).zIndex(6).icon(dangerSource)
				.animateType(MarkerAnimateType.grow);
		
		// 加载营口市地图
		GeneralUtils.toAppointedMap(mBaiduMap, 40.671, 122.233391, 15);
		
		initCharges();
		initDanger(0);
		
		call=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str = (String) v.getTag();
				call(str);
			}
		};
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if (marker.getIcon()==company) {
					//填充每个公司的救援队信息列表
					Bundle bundle = marker.getExtraInfo();
					Charge charge = (Charge) bundle.get("data");
					List<Manager> managers = charge.getCharge();
					mAdapter=new ManagerAdapter(Danger.this, managers,call);
					team.setAdapter(mAdapter);
					
				}
				if (marker.getIcon()==dangerSource) {
					//弹出事故简要信息
					Button button = new Button(getApplicationContext());
					button.setBackgroundResource(R.drawable.popup);
					button.setText(GeneralUtils.dangers.get(0).getInfo());
					button.setTextColor(Color.rgb(0, 0, 0));
					mInfoWindow = new InfoWindow(button, marker.getPosition(), -47);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				return false;
			}
		});
		 
	     
	   
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater=getMenuInflater();
		menuInflater.inflate(R.menu.danger, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.danger_clear:
			mBaiduMap.hideInfoWindow();
			return true;
			
		case R.id.analysis:
			for (Iterator iterator = GeneralUtils.charges.iterator(); iterator.hasNext();) {
				Charge charge = (Charge) iterator.next();
				shortestRoute(charge);
			}
			
			drawFinalResult();
			return true;
			
		case R.id.dangerArea:
			dangerMarker.remove();
			initDanger(1);
			return true;
			
		case R.id.danger_note:
			showNote();
			return true;
		default: return super.onOptionsItemSelected(item);
		}
		
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            Toast.makeText(Danger.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	            result.getSuggestAddrInfo();
	            return;
	        }
	        //如果查到结果，分别计算
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	        		route = result.getRouteLines().get(0);
	 	           int distance = route.getDistance();
	            if (shortestRoute==null) {
					shortestRoute=(DrivingRouteLine) route;
					return;
				}
	            if (distance<shortestRoute.getDistance()) {
	            	shortestRoute=(DrivingRouteLine) route;
				}
	        }
	}
	
	public void shortestRoute(Charge charge){
		//判断是否有维修队可以处理
		if (!charge.isStatus()) {
			return;
		}
		 mSearch = RoutePlanSearch.newInstance();
	      mSearch.setOnGetRoutePlanResultListener(this);
		LatLng startLatlng=charge.getLoc();
		LatLng endLatLng=danger.getLoc();
	    PlanNode end=PlanNode.withLocation(endLatLng);
	    PlanNode start=PlanNode.withLocation(startLatlng); 
	      mSearch.drivingSearch((new DrivingRoutePlanOption()).from(start).to(end));
		
	}
	
	public void drawFinalResult(){
		//将最短的路径画出
				DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
		        routeOverlay = overlay;
		        overlay.setData(shortestRoute);
		        overlay.addToMap();
		        overlay.zoomToSpan();
		        
	}

	//定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
                return null;//BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
                return null ;//BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }
    
	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		
	}

	//初始化每个修理站
	public void initCharges(){
		for (Iterator iterator = GeneralUtils.charges.iterator(); iterator.hasNext();) {
			Charge charge = (Charge) iterator.next();
			companyMarkerOptions.position(charge.getLoc());
			Marker marker = (Marker) mBaiduMap.addOverlay(companyMarkerOptions);
			Bundle bundle=new Bundle();
			 bundle.putSerializable("data", charge);
			 marker.setExtraInfo(bundle);
		}
			
	}
	
	//画出危险地点
	public void initDanger(int i){
		 danger = GeneralUtils.dangers.get(i);
		 dangerMarkerOptions.position(danger.getLoc());
		 dangerMarker = (Marker) mBaiduMap.addOverlay(dangerMarkerOptions);
	}
	
	//呼叫检修组负责人
	public void call(String tellNum){
		Intent intent=new Intent();
	    intent.setAction("android.intent.action.CALL");
	    intent.setData(Uri.parse("tel:"+tellNum));
	    startActivity(intent);
	}
	
	//模拟显示通知
	public void showNote(){
		icon=BitmapFactory.decodeResource(getResources(),
                R.drawable.dangericon);
		
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent intent=new Intent(this, Danger.class);
		
		pIntent = PendingIntent.getActivity(this, 0,  
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		 Notification notification = new NotificationCompat.Builder(this)
         .setLargeIcon(icon).setSmallIcon(R.drawable.hes)
         .setTicker("showNormal").setContentInfo(danger.getType())
         .setContentTitle("紧急情况").setContentText(danger.getInfo())
        .setNumber(danger.getId())
         .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)//把所有属性设置为系统默认，比如震动和铃声
         .setContentIntent(pIntent)
         .build();
		 manager.notify(NOTIFICATION_ID_1, notification);
	}
	
}
