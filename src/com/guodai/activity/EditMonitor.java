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
 *@注释：监测点的编辑页面
*/

public class EditMonitor extends Activity {
	
	private EditText uPID;
	private EditText hsoNum;
	private EditText hsiNum;
	private EditText addr;
	private EditText x;
	private EditText y;
	private EditText theNum;
	private EditText monitorID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heatsite);
		setTitle("编辑检测点");
		hsoNum=(EditText) findViewById(R.id.ms_edit_hsoNum);
		hsiNum=(EditText) findViewById(R.id.ms_edit_hsiNum);
		uPID=(EditText) findViewById(R.id.ms_edit_uPID);
		addr=(EditText) findViewById(R.id.ms_edit_addr);
		x=(EditText) findViewById(R.id.ms_edit_x);
		y=(EditText) findViewById(R.id.ms_edit_y);
		theNum=(EditText) findViewById(R.id.ms_edit_theNum);
		monitorID=(EditText) findViewById(R.id.ms_edit_monitorID);
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
