package com.guodai.activity;

import com.guodai.heatpope.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 *@author semxy
 *@日期：2016年4月13日
 *@注释：热站的编辑页面
*/

public class EditHeatsite extends Activity {
	
	private EditText hsoNum;
	private EditText hsiNum;
	private EditText hsiName;
	private EditText hsiClass;
	private EditText x;
	private EditText y;
	private EditText monitorID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heatsite);
		setTitle("编辑热站");
		hsoNum=(EditText) findViewById(R.id.hs_edit_hsoNum);
		hsiNum=(EditText) findViewById(R.id.hs_edit_hsiNum);
		hsiName=(EditText) findViewById(R.id.hs_edit_hsiName);
		hsiClass=(EditText) findViewById(R.id.hs_edit_hsiClass);
		x=(EditText) findViewById(R.id.hs_edit_x);
		y=(EditText) findViewById(R.id.hs_edit_y);
		monitorID=(EditText) findViewById(R.id.hs_edit_monitorID);
	
		Intent intent = getIntent();
		double lat = intent.getDoubleExtra("lat", 120);
		double lon = intent.getDoubleExtra("lon", 40);
		x.setText(lon+"");
		y.setText(lat+"");
	}

	public void submit(View view){
		Toast.makeText(this, "数据提交成功！", Toast.LENGTH_SHORT);
	}
	
	public void close(View view){
		finish();
	}
}
