package com.guodai.adapter;

import java.util.List;


import com.guodai.bean.Heatsite;
import com.guodai.heatpope.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HeatsiteAdapter extends BaseAdapter {
	private Context context;
	private List<Heatsite> data;

	@Override
	public int getCount() {
		return data.size();
	}

	public HeatsiteAdapter(Context context, List<Heatsite> data) {
		super();
		this.context = context;
		this.data = data;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null) {
			convertView = View.inflate(context,R.layout.heatsiteinfo, null);
		} else {
			//当前的convertView就是复用的ItemView
		}
		
		TextView nameTV = (TextView) convertView.findViewById(R.id.hsinfo_tv_name);
		TextView idTV = (TextView) convertView.findViewById(R.id.hsinfo_tv_ID);
		TextView typeTV = (TextView) convertView.findViewById(R.id.hsinfo_tv_type);
		TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
		
		Heatsite heatsite = data.get(position);
		nameTV.setText(heatsite.getHES_NAME());
		idTV.setText(heatsite.getHES_ID());
		textView3.setText(heatsite.getHES_TYPE());
		
		return convertView;
		
	}

}
