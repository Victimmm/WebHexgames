package com.lwm.springcloud.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lwm.springcloud.entities.Content;
import com.lwm.springcloud.entities.Msgindex;
import com.lwm.springcloud.entities.User;
import com.lwm.springcloud.service.ContentService;
import com.lwm.springcloud.service.MsgIndexService;
import com.lwm.springcloud.service.UserService;

@Controller
public class SqlController {
	@Autowired
	UserService userService;
	
	@Autowired
	MsgIndexService msgIndexService;
	
	@Autowired
	ContentService contentService;
	
	@ResponseBody
	@RequestMapping("/getNameByNum")
	public String getNameByNum(int num){
		return userService.getUser(num).getNickname();
	}
	
	@ResponseBody
	@RequestMapping("/getAllUser")
	public List<User> getAllUser(int myNum){
		return userService.getAllUser(myNum);
	}
	
	@ResponseBody
	@RequestMapping("/getMsg")
	public List<Msgindex> getMsg(int owner,int friend){
		msgIndexService.setRead(owner, friend);
		return msgIndexService.getMsg(owner, friend);
	}
	
	@ResponseBody
	@RequestMapping("/getContent")
	public Content getContent(int contentID){
		return contentService.getByID(contentID);
	}
	
	@ResponseBody
	@RequestMapping("/getUnreadNum")
	public Integer getUnreadNum(int owner,int friend){
		return msgIndexService.getUnreadNum(owner, friend);
	}

	@ResponseBody
	@RequestMapping("/getRecentContent")
	public String  getRecentContent(int owner,int friend){
		 int ID=msgIndexService.getRecentContentID(owner, friend);
		 if(ID==-1){
			 return "";
		 }else{
			 return contentService.getByID(ID).getMessage();
		 }
	}
	
	//拉去用户列表
	@RequestMapping(value="/get_integral",method=RequestMethod.POST)
	@ResponseBody
	public int get_integral(HttpServletRequest request,HttpServletResponse response)
	{
		return userService.getUser(Integer.parseInt(request.getParameter("user").trim())).getIntegral();
	}
	
	@RequestMapping(value="/consumer/getAllUser",method=RequestMethod.POST)
	@ResponseBody
	public List<User> list()
	{
		return userService.list();
	}
}
