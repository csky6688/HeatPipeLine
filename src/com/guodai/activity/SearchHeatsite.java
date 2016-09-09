package com.guodai.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guodai.adapter.HeatsiteAdapter;
import com.guodai.bean.Company;
import com.guodai.bean.Heatsite;
import com.guodai.heatpope.MainActivity;
import com.guodai.heatpope.R;

public class SearchHeatsite extends Activity implements OnItemLongClickListener ,OnItemSelectedListener{
	private Spinner company;
	private EditText search;
	private ListView heatsite;
	private SharedPreferences sp;
	private List<String> spinerContent;
	private String companyInfo;
	private String heatsiteInfo;
	private ArrayAdapter<String> adapter;
	private String currentSelceted;
	private List<Heatsite> company1;
	private List<Heatsite> company2;
	private List<Heatsite> company3;
	private List<Heatsite> company4;
	private List<Heatsite> edQuery;
	private List<Heatsite> companyAll;
	private HeatsiteAdapter myHsData;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heatsitelist);
		setTitle("查询热站");
		company = (Spinner) findViewById(R.id.hsinfo_spinner_class);
		search = (EditText) findViewById(R.id.hsinfo_search);
		heatsite = (ListView) findViewById(R.id.hs_listview_heatsite);

		// 从SP中取出Json
		sp = getSharedPreferences("data", Context.MODE_PRIVATE);
		companyInfo = sp.getString("公司", "查无此数据！");
		heatsiteInfo = sp.getString("热站", "查无此数据！");

		Log.v("data", heatsiteInfo);

		// 初始化数据
		initSpinerContent();
		initListviewContent();

		// 填充spinner
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, spinerContent);
		company.setAdapter(adapter);
		company.setOnItemSelectedListener(this);

		heatsite.setOnItemLongClickListener(this);

		Log.v("TAG", company1.size() + "_" + company1.get(1));
		Log.v("TAG", company2.size() + "_" + company2.get(1));
		Log.v("TAG", company3.size() + "_" + company3.get(1));
		Log.v("TAG", company4.size() + "_" + company4.get(1));
		}

		

	// 将json的公司数转化为spinner的下拉列表
	public  void initSpinerContent() {

		spinerContent = new ArrayList<String>();
		List<Company> company = null;
		Gson gson = new Gson();
		company = gson.fromJson(companyInfo, new TypeToken<List<Company>>() {
		}.getType());
		for (int i = 0; i < company.size(); i++) {
			Company item = company.get(i);
			String hs_NAME = item.getHS_NAME();
			spinerContent.add(hs_NAME);
		}
		spinerContent.add(0, "全部");
		spinerContent.add(0, "无");

	}

	// 将热站json按公司分组
	public void initListviewContent() {

		company1 = new ArrayList<Heatsite>();
		company2 = new ArrayList<Heatsite>();
		company3 = new ArrayList<Heatsite>();
		company4 = new ArrayList<Heatsite>();
		Gson gson = new Gson();
		companyAll = gson.fromJson(heatsiteInfo,
				new TypeToken<List<Heatsite>>() {
				}.getType());
		for (int i = 0; i < companyAll.size(); i++) {
			Heatsite item = companyAll.get(i);
			String key = item.getHES_TYPE();
			switch (key) {
			case "1":
				company1.add(item);
				break;

			case "2":
				company2.add(item);
				break;
			case "3":
				company3.add(item);
				break;
			case "4":
				company4.add(item);
				break;

			}
		}
	}

	// 查询到的热站在地图上面进行显示
	public void showOnMap(View view) {
		Intent intent = new Intent();
		List<Heatsite> data = null;
		//判断有无查询条件
		String querytext = search.getText().toString();
		if (!querytext.isEmpty()) {
			data=edQuery;
		}else {
			switch (currentSelceted) {
			case "全部":
				data = companyAll;
				break;
			case "一公司":
				data = company1;
				break;
			case "二公司":
				data = company2;
				break;
			case "三公司":
				data = company3;
				break;
			case "四公司":
				data = company4;
				break;
			
			}
		}
		intent.putExtra("data", (Serializable)data);
		setResult(112, intent);
		finish();

	}
	
	public void getEdQuery(List<Heatsite> company,String text){
		edQuery=new ArrayList<Heatsite>();
		for (Iterator iterator = companyAll.iterator(); iterator.hasNext();) {
			Heatsite hs = (Heatsite) iterator.next();
			if (hs.getHES_NAME().contains(text)) {	
				edQuery.add(hs);
				Log.v("query", hs.toString());
			}
		}
		myHsData = new HeatsiteAdapter(this, edQuery);
		heatsite.setAdapter(myHsData);
	}

	// 查询热站并列表显示
	public void query(View view){
		//情况
		heatsite.removeAllViewsInLayout();
		//取得查询条件
		String querytext = search.getText().toString();
		//如果查询条件非空，就从公司里面二次过滤
		if (!querytext.isEmpty()) {
			switch (currentSelceted) {
			case"无":Toast.makeText(this, "尚未选择数据分类！", Toast.LENGTH_LONG);break;
			case "全部":
				getEdQuery(companyAll, querytext);
				break;
			case "一公司":
				getEdQuery(company1, querytext);
				break;
			case "二公司":
				getEdQuery(company2, querytext);
				break;
			case "三公司":
				getEdQuery(company3, querytext);
				break;
			case "四公司":
				getEdQuery(company4, querytext);
				break;
			}
			
		}else {
			switch (currentSelceted) {
			case "全部":
				myHsData = new HeatsiteAdapter(this, companyAll);
				heatsite.setAdapter(myHsData);
				break;
			case "一公司":
				myHsData = new HeatsiteAdapter(this, company1);
				heatsite.setAdapter(myHsData);
				break;
			case "二公司":
				myHsData = new HeatsiteAdapter(this, company2);
				heatsite.setAdapter(myHsData);
				break;
			case "三公司":
				myHsData = new HeatsiteAdapter(this, company3);
				heatsite.setAdapter(myHsData);
				break;
			case "四公司":
				myHsData = new HeatsiteAdapter(this, company4);
				heatsite.setAdapter(myHsData);
				break;
			}
		}
		
	}


	//选中热战长按，在地图上面显示
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Heatsite selected = (Heatsite) arg0.getItemAtPosition(arg2);
		Intent intent=getIntent();
		intent=new Intent();
		intent.putExtra("selected", selected);
		setResult(111, intent);
		finish();
		return true;
	}


	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		currentSelceted = (String) company.getItemAtPosition(position);
	}



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
