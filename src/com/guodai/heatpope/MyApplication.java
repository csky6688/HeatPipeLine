package com.guodai.heatpope;

import java.util.ArrayList;
import java.util.List;

import com.guodai.bean.User;

import android.app.Application;

public class MyApplication extends Application {
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	private List<User> users=new ArrayList<User>();
	
	private User currentUser;
	
	@Override
	public void onCreate() {
		super.onCreate();
		User user=new User("wangwei","wangwei","王伟", "10012", "热电一公司巡检组");
		User user1=new User("lishuai","lishuai","李帅", "10016", "热电一公司巡检组");
		
		users.add(user);
		users.add(user1);
		
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
