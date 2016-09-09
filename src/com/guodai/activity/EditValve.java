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
 *@注释：阀门的编辑页面
*/

public class EditValve extends Activity {
	
	private EditText valveId;
	private EditText lineNum;
	private EditText valveName;
	private EditText caliber;
	private EditText x;
	private EditText y;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.valve);
		setTitle("编辑阀门");
		valveId=(EditText) findViewById(R.id.valve_edit_valveID);
		lineNum=(EditText) findViewById(R.id.valve_edit_linePipeNum);
		valveName=(EditText) findViewById(R.id.valve_edit_valveName);
		caliber=(EditText) findViewById(R.id.valve_edit_caliber);
		x=(EditText) findViewById(R.id.valve_edit_x);
		y=(EditText) findViewById(R.id.valve_edit_y);
		
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
		this.finish();
	}
}
