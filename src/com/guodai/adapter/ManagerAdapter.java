package com.guodai.adapter;

import java.util.Iterator;
import java.util.List;

import com.guodai.activity.Danger;
import com.guodai.bean.Manager;
import com.guodai.heatpope.R;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerAdapter extends BaseAdapter {
	
	private Context context;
	private List<Manager> data;
	private OnClickListener  call;
	

	public ManagerAdapter(Context context, List<Manager> data,OnClickListener call) {
		super();
		this.context = context;
		this.data = data;
		this.call=call;
	}

	@Override
	public int getCount() {
		return data.size();
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
			convertView = View.inflate(context,R.layout.group_list, null);
		} else {
			//当前的convertView就是复用的ItemView
		}
		//初始化view的组件
		TextView gruopName=(TextView) convertView.findViewById(R.id.tv_groupName);
		TextView leader=(TextView) convertView.findViewById(R.id.tv_leader);
		TextView member=(TextView) convertView.findViewById(R.id.tv_members);
		TextView leaderId=(TextView) convertView.findViewById(R.id.tv_workNum);
		ImageButton callmanager=(ImageButton) convertView.findViewById(R.id.imb_callmanager);
		ImageButton sendmission=(ImageButton) convertView.findViewById(R.id.imb_sendmission);
		
		
		//设置出勤，在勤状态
		final Manager manager = data.get(position);
		gruopName.setText(manager.getGroup());
		leader.setText(manager.getLeader().getName());
		if (manager.isStatus()) {
			leaderId.setText("在勤");
			leaderId.setTextColor(Color.rgb(34, 208, 34));
		}else {
			leaderId.setText("出勤");
			leaderId.setTextColor(Color.rgb(205, 30, 30));
		}
		
		
		
		List<String> team = manager.getTeam();
		Log.v("team", team.size()+"");
		String memberStr="";
		
		for (Iterator iterator = team.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			memberStr=memberStr+string;
		if (iterator.hasNext()) {
			memberStr=memberStr+"、";
		}
		
		}
		member.setText(memberStr);
		
		callmanager.setTag(manager.getLeader().getTel());
		
		callmanager.setOnClickListener(call);
		
		
		sendmission.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "任务已指派至"+manager.getGroup()+"!", Toast.LENGTH_SHORT).show();
			}
		});
		
		if (manager.isStatus()) {
			sendmission.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

}
