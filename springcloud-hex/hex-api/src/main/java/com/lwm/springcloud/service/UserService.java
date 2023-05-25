package com.lwm.springcloud.service;
import java.util.List;

import com.lwm.springcloud.entities.*;

public interface UserService {
	int addUser(User user);
	User getUser(int accountNum);
	int update(User user);
	List<User> list();
	//邓麒麟新增 开始
	//1.
	List<User> getAllUser(int myNum);
	
	//邓麒麟新增  结束
}
