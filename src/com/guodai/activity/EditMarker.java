package com.guodai.activity;

import com.guodai.heatpope.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
 *@author semxy
 *@日期：2016年4月13日
 *@注释：标记的编辑页面
*/

public class EditMarker extends Activity {
	
	private EditText name;
	private EditText remark;
	private Spinner classify;
	private EditText x;
	private EditText y;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker);
		setTitle("编辑标记");
		name=(EditText) findViewById(R.id.marker_edit_name);
		remark=(EditText) findViewById(R.id.marker_edit_remark);
		x=(EditText) findViewById(R.id.marker_edit_x);
		y=(EditText) findViewById(R.id.marker_edit_y);
		classify=(Spinner) findViewById(R.id.marker_spinner_classify);
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
