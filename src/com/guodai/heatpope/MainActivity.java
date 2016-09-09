package com.guodai.heatpope;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guodai.activity.Danger;
import com.guodai.activity.EditHeatsite;
import com.guodai.activity.EditHeatsource;
import com.guodai.activity.EditMarker;
import com.guodai.activity.EditMonitor;
import com.guodai.activity.EditValve;
import com.guodai.activity.Patrol;
import com.guodai.activity.SearchHeatsite;
import com.guodai.activity.User;
import com.guodai.bean.HeatLine;
import com.guodai.bean.Heatsite;
import com.guodai.satellite.view.SatelliteMenu;
import com.guodai.satellite.view.SatelliteMenu.SateliteClickedListener;
import com.guodai.satellite.view.SatelliteMenuItem;
import com.guodai.utils.GeneralUtils;
import com.guodai.utils.GetDataThread;


public class MainActivity extends Activity implements OnLongClickListener {


	private String[] params = { "a=0", "a=1002", "a=1001", "a=1003", "a=1007","a=1005",
			"a=1006", "a=1011", "a=1009", "a=1010" };
	private String[] type = {"热站" , "表4初始化", "表2初始化", "公司", "管线","监测点", "监测点数值",
			"热源", "公司数", "监测点初始化数据" };
	private MapView mMapView;
	private View popLocView;
	private PopupWindow mPopWindow;
	private ToggleButton toggle;
	private RadioGroup group;
	//定义一堆点图标
	private BitmapDescriptor pointIcon;
	private BitmapDescriptor valveIcon;
	private BitmapDescriptor heatsiteIcon;
	private BitmapDescriptor heatSourceIcon;
	private BitmapDescriptor monitorIcon;
	//定义标记参数
	private MarkerOptions markerOptions;
	private Marker marker;
	private LatLng markerLatLng;
	private SharedPreferences sp;
	private Editor editor;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner(); // 位置监听
	private LocationMode mCurrentMode = null; // 定位模式
	BitmapDescriptor mCurrentMarker = null; // 定位图标,null表示默认图标，也可以自定义加载
	private BaiduMap mBaiduMap; // 图层对象
	boolean isFirstLoc = true; // 是否首次定位
	private List<LatLng> disPoints = new ArrayList<LatLng>(2);
	private InfoWindow HSDetail;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		
		
//		 SPtext代表写入的文件名,文件存储位置为：/data/data/packageName/shared_prefs/data.xml
		sp = getSharedPreferences("data", Context.MODE_PRIVATE);
		editor = sp.edit();
		
		markerOptions = new MarkerOptions().perspective(false)
				.anchor(0.5f, 0.5f).zIndex(7)
				.animateType(MarkerAnimateType.grow);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
//		//初始化数据
//		getData(params, type);
		// 初始化geometry标记
		initGeometry();
		
		// 加载营口市地图
		GeneralUtils.toAppointedMap(mBaiduMap, 40.671, 122.233391, 15);

		// 初始化地位弹窗
		popLocView = getLayoutInflater().inflate(R.layout.location, null);
		toggle = (ToggleButton) popLocView.findViewById(R.id.loc_open);
		group = (RadioGroup) popLocView.findViewById(R.id.loc_radioGroup);

		// 加载左下角菜单
		SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);

		List<com.guodai.satellite.view.SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(6, R.drawable.warning));
		items.add(new SatelliteMenuItem(5, R.drawable.mesure));
		items.add(new SatelliteMenuItem(4, R.drawable.checkway));
		items.add(new SatelliteMenuItem(3, R.drawable.cloud));
		items.add(new SatelliteMenuItem(2, R.drawable.hes));
		items.add(new SatelliteMenuItem(1, R.drawable.login));
		// items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
		menu.addItems(items);

		menu.setOnItemClickedListener(new SateliteClickedListener() {

			public void eventOccured(int id) {

				switch (id) {
				
				case 1://进入用户界面，显示用户的头像以及需要巡检的任务等
					Intent intent3=new Intent(getApplicationContext(), User.class);
					startActivity(intent3);
				case 3://更新数据
					for (int i = 0; i < params.length; i++) {
						GetDataThread getDataThread = new GetDataThread();
						getDataThread.setParam(params[i]);
						getDataThread.setInfo(type[i]);
						getDataThread.setEditor(editor);
						getDataThread.start();
					}
					Toast.makeText(MainActivity.this, "数据更新完毕！", 0).show();
					break;
				case 2://进入热站属性查询页面
					Intent intent=new Intent(getApplicationContext(),SearchHeatsite.class );
					startActivityForResult(intent, 1993);
					break;
					
				case 4://巡检
					Intent intent2=new Intent(getApplicationContext(), Patrol.class);
					intent2.putExtra("show", false);
					startActivity(intent2);
				case 5://测距
					getDistance();
					break;
					
				case 6:
					Intent intent4=new Intent(getApplicationContext(), Danger.class);
					startActivity(intent4);
				default:
					break;
				}
			}
		});
	}
/**
 *@author semxy
 *@日期：2016年4月13日
 *@注释：弹出图层的气泡菜单
*/
	public void showPopMenu(View view) {
		PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
		popupMenu.getMenuInflater().inflate(R.menu.layer, popupMenu.getMenu());
		popupMenu.show();
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.map:
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					break;
				case R.id.pipe:
					showPipeLayer();
					break;
				case R.id.heat:
					mBaiduMap.setBaiduHeatMapEnabled(true);
					break;
				case R.id.satellite:
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					break;

				}
				GeneralUtils.toAppointedMap(mBaiduMap, 40.671, 122.233391, 15);
				return false;
			}
		});
	}

	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：添加地物
	 */
	public void addGeometry(View view) {
		PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
		popupMenu.getMenuInflater().inflate(R.menu.addgeometry,
				popupMenu.getMenu());
		popupMenu.show();

		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {

				switch (item.getItemId()) {
				case R.id.marker:
					markerOptions.icon(pointIcon);
					initListener();
					break;

				case R.id.valve:
					markerOptions.icon(valveIcon);
					initListener();
					break;
					
				case R.id.heatsite:
					markerOptions.icon(heatsiteIcon);
					initListener();
					break;
				case R.id.heatsource:
					markerOptions.icon(heatSourceIcon);
					initListener();
					break;
				
				case R.id.monitor:
					markerOptions.icon(monitorIcon);
					initListener();
					break;

				case R.id.clear:
					mBaiduMap.clear();
					break;

				}
				return false;
			}
		});
	}

	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：弹出定位菜单
	 */
	public void showLocMenu(View view) {

		if (mCurrentMode != null) {
			toggle.setChecked(true);
		}
		// Focusable 为True，PopupWindow的点击事件才会相应
		mPopWindow = new PopupWindow(popLocView, 250, 550, true);
		// 必须在代码中设置一下背景色，点击外面不会隐藏此弹窗
		mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// Focusable 为False时，不执行则点击外面不会隐藏此弹窗
		// mPopWindow.setOutsideTouchable(true);
		mPopWindow.showAsDropDown(view);

		popLocViewInit();

	}

	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：定位按钮的开启和关闭功能
	 */
	public void popLocViewInit() {

		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					group.setVisibility(View.VISIBLE);
					locInits();
				} else {
					group.setVisibility(View.GONE);
					// 退出时销毁定位
					mLocClient.stop();
					// 关闭定位图层
					mBaiduMap.setMyLocationEnabled(false);
				}
			}
		});
		// 初始化RadioGroup

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.loc_radio_normal) {
					mCurrentMode = LocationMode.NORMAL;
				} else if (checkedId == R.id.loc_radio_follow) {
					mCurrentMode = LocationMode.FOLLOWING;
				} else {
					mCurrentMode = LocationMode.COMPASS;
				}
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfiguration(
								mCurrentMode, true, null));
			}
		});
	}

	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：定位前的初始化工作
	 */
	public void locInits() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
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

	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：定位SDK监听函数,内部类
	 */
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

	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		// 回收位图
		pointIcon.recycle();
		valveIcon.recycle();
		heatsiteIcon.recycle();
		heatSourceIcon.recycle();
		monitorIcon.recycle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	
	

	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：初始化标记，监测点，热站，热源，阀门图标
	 */
	//初始化图标
	public void initGeometry() {
		pointIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_point);
		valveIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_valve);
		heatsiteIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.heslittile);
		heatSourceIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_heatsource);
		monitorIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_monitor);
	}

	/**
	 *@author semxy
	 *@日期：2016年4月11日
	 *@注释：屏幕上点击后，绘制线段，并显示距离
	 */
	public void getDistance(){
		 OnMapClickListener distanceOMC = new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				OverlayOptions ooDot = new DotOptions().center(arg0).color(0xFF0000FF);
				mBaiduMap.addOverlay(ooDot);
				disPoints.add(arg0);
				if (disPoints.size()==2) {
					//绘制线段
					OverlayOptions ooPolyline = new PolylineOptions().width(10)
							.color(0xAAFF0000).points(disPoints);
					mBaiduMap.addOverlay(ooPolyline);
					//计算距离
					double distance = DistanceUtil.getDistance(disPoints.get(0), disPoints.get(1));
					String text="距离："+String.format("%.2f", distance)+"米";
					//绘制文本框
					OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
							.fontSize(24).fontColor(0xFFFF00FF).text(text).position(arg0);
					mBaiduMap.addOverlay(ooText);
					disPoints.clear();
				}
			}
		};
		mBaiduMap.setOnMapClickListener(distanceOMC);
	}


	/**
	 *@author semxy
	 *@日期：2016年4月11日
	 *@注释：为地图设置监听，以实现添加geometry，并进行编辑的功能
	 */
	public void initListener() {
		// 设置地图的长按监听，加载绘制点或者线
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				markerOptions = markerOptions.position(point);
				marker = (Marker) (mBaiduMap.addOverlay(markerOptions));
			}
		});

		//点击marker后跳转到相应的编辑的acticity
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				if (marker.getIcon() == pointIcon) {
					toEidtActivity(marker, new EditMarker());
				}
				if (marker.getIcon() == valveIcon) {
					toEidtActivity(marker, new EditValve());
				}
				if (marker.getIcon() == heatsiteIcon) {
					toEidtActivity(marker, new EditHeatsite());
				}
				if (marker.getIcon() == heatSourceIcon) {
					toEidtActivity(marker, new EditHeatsource());
				}
				if (marker.getIcon() == monitorIcon) {
					toEidtActivity(marker, new EditMonitor());
				}
				return false;
			}
		});
	}

	/**
	 *@author semxy
	 *@日期：2016年4月11日
	 *@注释：点击热战marker后，显示详细信息
	 */
	public void showMarkerDetail(){
		final View window = getLayoutInflater().inflate(R.layout.heatsiteinfowindow, null);
		final TextView  name = (TextView) window.findViewById(R.id.win_name);
		final TextView  id = (TextView) window.findViewById(R.id.win_id);
		final TextView  company = (TextView) window.findViewById(R.id.win_type);
		final TextView  loc = (TextView) window.findViewById(R.id.win_loc);
		
		OnMarkerClickListener showMD = new OnMarkerClickListener() {
			 
			@Override
			public boolean onMarkerClick(Marker marker) {
				
				HSDetail=new InfoWindow(window, marker.getPosition(), -50);
				Bundle bundle = marker.getExtraInfo();
				Heatsite heatsite = (Heatsite) bundle.getSerializable("data");
				Log.v("err", heatsite.toString());
				name.setText(heatsite.getHES_NAME());
				id.setText(heatsite.getHES_ID());
				company.setText(heatsite.getHES_TYPE());
				Log.v("err", heatsite.getHES_X());
				Log.v("err", heatsite.getHES_Y());
				loc.setText("("+heatsite.getHES_X().substring(0, 5)+","+heatsite.getHES_Y().substring(0, 4)+")");
				mBaiduMap.showInfoWindow(HSDetail);
				return false;
			}
		};
		mBaiduMap.setOnMarkerClickListener(showMD);
	}
	
	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：根据不同的marker，跳转到不同的activity编辑页面
	 */
	public void toEidtActivity(Marker marker, Activity target) {
		double lat = marker.getPosition().latitude;
		double lon = marker.getPosition().longitude;
		Intent intent = new Intent(MainActivity.this, target.getClass());
		//回传经纬度
		intent.putExtra("lat", lat);//纬度
		intent.putExtra("lon", lon);//经度
		startActivity(intent);

	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	
	/**
	 *@author semxy
	 *@日期：2016年4月13日
	 *@注释：从SP文件中取出管线数据，并绘制图层
	 */
	public void showPipeLayer(){
		String value = sp.getString("管线", "SP文件中无此值，请联系管理员！");
		Log.v("value", value);
		if (value.equals("SP文件中无此值，请联系管理员！")) {
			Toast.makeText(getApplicationContext(), "管线数据错误，无法加载图层！", 1).show();
			return;
		}
		List<HeatLine> data  = null;//[{id:1,name},{}]
		Gson gson = new Gson();
		data = gson.fromJson(value, new TypeToken<List<HeatLine>>(){}.getType());
		
		Log.v("TAG", data.size()+"__"+data.get(5));
		
		List<HeatLine> uselessData=new ArrayList<HeatLine>();
		//剔除脏数据
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getL_X1().equals("")||data.get(i).getL_X2().equals("")||data.get(i).getL_Y1().equals("")||data.get(i).getL_Y2().equals("")) {
				uselessData.add(data.get(i));
			}
		}
		Log.v("TAG", uselessData.size()+"");
		data.removeAll(uselessData);
		
		
		Log.v("TAG", "移除后"+data.size());
		
		for (int i = 0; i < data.size(); i++) {
			HeatLine heatLine=data.get(i);
			
			LatLng p1 = new LatLng(Double.parseDouble(heatLine.getL_Y1()), Double.parseDouble(heatLine.getL_X1()));
			LatLng p2 = new LatLng(Double.parseDouble(heatLine.getL_Y2()), Double.parseDouble(heatLine.getL_X2()));
			Log.v("line", heatLine.toString());
			 List<LatLng> points = new ArrayList<LatLng>();
			  points.add(p1);
		        points.add(p2);
		        OverlayOptions ooPolyline = new PolylineOptions().width(10)
		                .color(0xAAFF0000).points(points);
		       mBaiduMap.addOverlay(ooPolyline);
		}

		Toast.makeText(getApplicationContext(), "图层加载完毕", 1).show();
	}
	
	/**
	 *@author semxy
	 *@日期：2016年4月12日
	 *@注释：将从热站查询页面传入的热站在地图上显示出来
    */

	public void showHeatsite(Heatsite heatsite){
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.heslittile);
		MarkerOptions mOptions=new MarkerOptions();
		mOptions.icon(icon);
		if (heatsite.getHES_X().equals("")||heatsite.getHES_Y().equals("")) {
			return;
		}
		Double Y = Double.valueOf(heatsite.getHES_Y());
		Double X = Double.valueOf(heatsite.getHES_X());
		LatLng pLatLng=new LatLng(Y, X);
		mOptions.position(pLatLng);
		
		 Marker marker = (Marker) (mBaiduMap.addOverlay(mOptions));
		 Bundle bundle=new Bundle();
		 bundle.putSerializable("data", heatsite);
		 marker.setExtraInfo(bundle);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode==1993&&resultCode==111) {
			Heatsite selected = (Heatsite)data.getSerializableExtra("selected");
			showHeatsite(selected);
			showMarkerDetail();
			GeneralUtils.toAppointedMap(mBaiduMap, Double.valueOf(selected.getHES_Y()), Double.valueOf(selected.getHES_X()), 15);
		}
		if (requestCode==1993&&resultCode==112) {
			
			if (data!=null) {
				List<Heatsite> toShow=(List<Heatsite>)data.getSerializableExtra("data");
				for (Iterator iterator = toShow.iterator(); iterator.hasNext();) {
					Heatsite heatsite = (Heatsite) iterator.next();
					showHeatsite(heatsite);
				}
				showMarkerDetail();
			}
		}
	}
	
	
	
}
