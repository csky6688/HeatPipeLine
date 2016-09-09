package com.guodai.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.guodai.heatpope.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;



//此service用于后台更新数据
public class DownloadDataService extends Service {
	

	private String[] params = { "a=0", "a=1002", "a=1001", "a=1003", "a=1005",
			"a=1006", "a=1011", "a=1009", "a=1010" };
	private String[] type = {"热站" , "表4初始化", "表2初始化", "公司", "监测点", "监测点数值",
			"热源", "公司数", "监测点初始化数据" };
	private SharedPreferences sp;
	private Editor editor;
	private int i=0;

	@Override
	public IBinder onBind(Intent intent) {
		return null ;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
//		 SPtext代表写入的文件名,文件存储位置为：/data/data/packageName/shared_prefs/yyy.xml
			sp = getSharedPreferences("data", Context.MODE_PRIVATE);
			editor = sp.edit();

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		for (i=0; i < params.length+1; i++) {
			getData(params, type);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	public void getData(final String[] params, final String[] type) {
		
		new Thread() {
			public void run() {
				try {
					String target = getString(R.string.url);

					URL url = new URL(target);

					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setReadTimeout(20000);
					connection.setConnectTimeout(20000);
					// 区别，默认是发送get请求，现在设置请求方法为POST请求
					connection.setRequestMethod("POST");
					connection.connect();
					
					// 区别，需要将post的参数写到服务器端
					
						String data = params[i];
						OutputStream os = connection.getOutputStream();
						// 注意和服务器端的编码
						os.write(data.getBytes("utf-8"));

						if (connection.getResponseCode() == 200) {
							InputStream is = connection.getInputStream();
							ByteArrayOutputStream bao = new ByteArrayOutputStream();
							byte buffer[] = new byte[1024];
							int len = 0;
							while ((len = is.read(buffer)) != -1) {
								bao.write(buffer, 0, len);
							}
							is.close();
							bao.close();
							final String result = bao.toString();
							Log.v("TAG", result);
							editor.putString(type[i], result).commit();
						}

						//
						// runOnUiThread(new Runnable() {
						//
						// @Override
						// public void run() {
						// edit_result.setText(result);
						// }
						// });
					
					Log.v("TAG", "数据获取完毕");
					connection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					
				}
			};
		}.start();

	}
	
	
}
