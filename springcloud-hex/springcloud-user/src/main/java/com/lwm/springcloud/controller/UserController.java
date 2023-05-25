package com.lwm.springcloud.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lwm.springcloud.entities.User;
import com.lwm.springcloud.service.ContentService;
import com.lwm.springcloud.service.MsgIndexService;
import com.lwm.springcloud.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	MsgIndexService msgIndexService;
	
	@Autowired
	ContentService contentService;

	@RequestMapping("/consumer/logincheck")
	@ResponseBody
	public String logincheck(HttpServletRequest request) throws IOException{
		request.setCharacterEncoding("utf-8");
		Integer accountnum = Integer.parseInt(request.getParameter("account").trim());
		String password = request.getParameter("password").trim();
		System.out.println("======User Login======");
		System.out.println("Get account_num:"+accountnum+" ; password: "+password);
		System.out.println("Searching...");
		if(userService.getUser(accountnum)!=null){
			//this user exists.
			User tmp_user = userService.getUser(accountnum);
			System.out.println("User "+accountnum+" exists!");
			if(tmp_user.getPwd().equalsIgnoreCase(password)){

				System.out.println("Successful!\n");
				return ("验证成功");
			}else{//password is wrong.
					System.out.println("Password is wrong!"); 
					return ("密码错误");
				 }
		}else{//this user don't exist.
			System.out.println("User "+accountnum+" don't exist!");
			return ("账号错误");
		}
	}
	
	@RequestMapping("/consumer/registercheck")
	@ResponseBody
	public String registercheck(HttpServletRequest request) throws IOException{
		request.setCharacterEncoding("utf-8");
		Integer accountnum = Integer.parseInt(request.getParameter("accountnum").trim());
		String nickname = request.getParameter("nickname").trim();
		String school = request.getParameter("school").trim();
        String password = request.getParameter("password").trim();
        System.out.println("======User Register======");
        if(userService.getUser(accountnum)!=null){
        	System.out.println("AccountNum is repeated!\n");
        	return ("账号重复");
        }else{
        	Date date = new Date();
        	User new_user = new User(accountnum,nickname,school,password,300,"B",0,0,date);
            if(userService.addUser(new_user)==1){
            	System.out.println("Successful!\n");
            	return ("注册成功");
            }	
        }
		return null;
	}
	
	@RequestMapping("/consumer/updateData")
	@ResponseBody
	public String updateUserData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Integer accountnum = Integer.parseInt(request.getParameter("accountnum").trim());
		String name = request.getParameter("name").trim();
		String school = request.getParameter("school").trim();
		String password = request.getParameter("password").trim();
		
		User user = userService.getUser(accountnum);
		user.setNickname(name);
		user.setPwd(password);
		user.setSchool(school);
		System.out.println("======Update User Data======");
		if(userService.update(user)==1){
			System.out.println("Successful!\n");
			return ("修改成功");
		}else{
			return ("修改失败");
		}
	}
	
	
	/*未完成……*/
	@RequestMapping("/consumer/logOut")
	@ResponseBody
	public void logOut(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Integer accountnum = Integer.parseInt(request.getParameter("accountnum").trim());
		String name = request.getParameter("name").trim();
		String school = request.getParameter("school").trim();
		String password = request.getParameter("password").trim();
		
		User user = userService.getUser(accountnum);
		user.setNickname(name);
		user.setPwd(password);
		user.setSchool(school);
		System.out.println("======Update User Date======");
		if(userService.update(user)==1){
			System.out.println("Successful!\n");
			response.getWriter().println("修改成功");
		}else{
			response.getWriter().println("修改失败");
		}
		System.out.println("=========================\n");
	}
	
	
	@RequestMapping("/consumer/getRecord")
	@ResponseBody
	public List<String> getRecord(int accountNum){
		List<String> data=new ArrayList<String>();
		User u=userService.getUser(accountNum);
		if (u==null)
			return data;
		//注册时间
		Date registerDay=u.getRegisterDay();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		data.add(sdf.format(registerDay));
		
		//时间差
		Calendar start=Calendar.getInstance();
		Calendar end=Calendar.getInstance();
		start.setTime(registerDay);
		int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
		data.add(String.valueOf(days));
		
		//个人胜绩		
		int all=u.getGameMatch();
		int win=u.getWin();
		data.add(win+"/"+all);
		
		//个人称号
		String g=u.getGrade();
		String grade=null;
		switch(g){
			case"B": grade="黄铜";break;
			case"S": grade="白银";break;
			case"G": grade="黄金";break;
			case"P": grade="白金";break;
			case"D": grade="钻石";break;
			case"C": grade="最强";break;
			default: grade="黄铜";
		};
		data.add(grade+"棋士");
		
		//胜率
		float winRate=((float)win)/all;	
		data.add(String.valueOf((int)(winRate*100)));
		
		//积分
		int integral = u.getIntegral();
		System.out.println("======jifen======"+integral);
		data.add(integral+"分");
		
		return data;
		
	}
}
