package com.guodai.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guodai.bean.HeatLine;
import com.guodai.bean.User;
import com.guodai.heatpope.MainActivity;
import com.guodai.heatpope.MyApplication;
import com.guodai.heatpope.R;
import com.guodai.utils.GeneralUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private EditText username;
	private EditText password;
	private SharedPreferences sp ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setTitle("登录");
		username=(EditText) findViewById(R.id.username);
		password=(EditText) findViewById(R.id.password);
		
		File spFile=new File("/data/data/com.guodai.heatpope/shared_prefs/data.xml");
		if (!spFile.exists()) {
			copyAssetsFileToSP("data.xml", spFile);
			Log.e("copy", "success");
		}
		
		
		
	}
	
	public void callForHelp(View view){
		Intent intent=new Intent();
		String phoneNum="3555500";
	    intent.setAction("android.intent.action.CALL");
	    intent.setData(Uri.parse("tel:"+phoneNum));
	    startActivity(intent);
	}
	
		public void login(View view){
			
						String un = username.getText().toString();
						String pwd = password.getText().toString();
						
						sp = getSharedPreferences("data", Context.MODE_PRIVATE);
						MyApplication application = (MyApplication) getApplication();
						
						Gson gson=new Gson();
						
						for (int i =0 ; i <application.getUsers().size() ; i++) {
							User user=application.getUsers().get(i);
								if (!un.equals(user.getUsername())) {
								continue;
								}
							
								if (!pwd.equals(user.getPassword())) {
									Toast.makeText(Login.this, "密码错误！", Toast.LENGTH_SHORT).show();
									return;
								}else {
									GeneralUtils.init();
									//取出巡检的任务和历史回放进行判断
									String task = sp.getString("task", "");
									String task1 = sp.getString("task1", "");
									String history = sp.getString("history", "");
									String history1 = sp.getString("history1", "");
									
								Map<String, List<HeatLine>> taskMap=new HashMap<String, List<HeatLine>>();//任务路线
								 Map<String, List<LatLng>> historyMap=new HashMap<String, List<LatLng>>();//历史路线
									
								 initUser1(gson, task1, taskMap, "task1");
								 initUser1(gson, task, taskMap, "task2");
								 initUser2(gson, history1, historyMap, "history1");
								 initUser2(gson, history, historyMap, "history2");
									
								 user.setHistoryMap(historyMap);
								 user.setTaskMap(taskMap);
									
									application.setCurrentUser(user);
									Intent intent=new Intent(Login.this, MainActivity.class);
									startActivity(intent);
									return;
								}
							}
						Toast.makeText(Login.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
						Log.v("Tag", "程序已经执行完毕");
				}
			
		
		//解析JSON并填充用户的任务路线
		public void initUser1(Gson gson,String task,Map<String, List<HeatLine>> taskMap,String name){
			if (!task.equals("")) {
				List<HeatLine> data  = null;//[{id:1,name},{}]
				data = gson.fromJson(task, new TypeToken<List<HeatLine>>(){}.getType());
				taskMap.put(name, data);
			}
		}
		
		//解析JSON并填充用户的历史轨迹
		public void initUser2(Gson gson,String task,Map<String, List<LatLng>> historyMap,String name){
			if (!task.equals("")) {
				List<LatLng> data  = null;//[{id:1,name},{}]
				data = gson.fromJson(task, new TypeToken<List<LatLng>>(){}.getType());
				historyMap.put(name, data);
			}
		}
		
		/**
		 * 从工程资源里面复制相关文件到存储卡上
		 */
		public  void copyAssetsFileToSP(String filename, File des) {
			InputStream inputFile = null;
			OutputStream outputFile = null;
			try {
				
					inputFile =getAssets().open(filename);
					outputFile = new FileOutputStream(des);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inputFile.read(buffer)) > 0) {
						outputFile.write(buffer, 0, length);
					}
					outputFile.flush();
					outputFile.close();
					inputFile.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

