package com.lwm.springcloud.serviceimpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.lwm.springcloud.dao.*;
import com.lwm.springcloud.entities.*;
import com.lwm.springcloud.service.*;


@Service("userService")
public class UserServiceImpl implements UserService {
	@Resource
	UserMapper userMapper;
	
	@Override
	public int addUser(User user) {
		// TODO Auto-generated method stub
		return userMapper.insert(user);
	}

	@Override
	public User getUser(int accountNum) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(accountNum);
	}

	@Override
	public int update(User user) {
		// TODO Auto-generated method stub
		return userMapper.updateByPrimaryKey(user);
	}
	
	@Override
	public List<User> list() {
		// TODO Auto-generated method stub
		UserExample example =new UserExample();
		return userMapper.selectByExample(example);
	}
	
	//邓麒麟 新增 开始
	@Override
	public List<User> getAllUser(int myNum) {
		UserExample example =new UserExample();
		UserExample.Criteria cri=example.createCriteria();
		cri.andAccountnumNotEqualTo(myNum);
		
		return userMapper.selectByExample(example);
	}

	//邓麒麟新增  结束
}
