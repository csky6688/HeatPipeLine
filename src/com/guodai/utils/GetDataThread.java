package com.guodai.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.SharedPreferences.Editor;
import android.util.Log;

public class GetDataThread extends Thread {
	
	private Editor editor;
	private String param;
	public Editor getEditor() {
		return editor;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	private String info;
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	private String[] params = { "a=0", "a=1002", "a=1001", "a=1003", "a=1007","a=1005",
			"a=1006", "a=1011", "a=1009", "a=1010" };
	private String[] type = {"热站" , "表4初始化", "表2初始化", "公司", "管线","监测点", "监测点数值",
			"热源", "公司数", "监测点初始化数据" };

	@Override
	public void run() {
		try {
			getData(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HttpURLConnection init() throws Exception{
		String target ="http://192.168.2.23:8080//service/ajax.php?action=ok";
		URL url = new URL(target);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(20000);
		connection.setConnectTimeout(20000);
		connection.setRequestMethod("POST");	
		return connection;
	}
	
	public void getData(String param) throws Exception{
		this.param=param;
		HttpURLConnection connection = init();
		connection.connect();
		OutputStream os = connection.getOutputStream();
			String data = param;
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
				editor.putString(info, result).commit();
				Log.v("TAG", result);			
				connection.disconnect();
			}
	}
}
