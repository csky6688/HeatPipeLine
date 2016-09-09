package com.guodai.activity;

import com.guodai.heatpope.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 *@author semxy
 *@日期：2016年4月13日
 *@注释：热源的编辑页面
*/

public class EditHeatsource extends Activity {
	
	private EditText hsoNam;
	private EditText hsoRemarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heatsource);
		setTitle("编辑热源");
		hsoNam=(EditText) findViewById(R.id.hso_edit_hsoName);
		hsoRemarker=(EditText) findViewById(R.id.hso_edit_hsoRemarker);
	
	}

	public void submit(View view){
		Toast.makeText(this, "数据提交成功！", Toast.LENGTH_SHORT);
	}
	
	public void close(View view){
		finish();
	}
}
