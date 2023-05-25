package com.lwm.springcloud.controller;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.File;
import java.io.FileNotFoundException;
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

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.lwm.springcloud.entities.User;
import com.lwm.springcloud.service.ContentService;
import com.lwm.springcloud.service.MsgIndexService;
import com.lwm.springcloud.service.UserService;

@Controller
public class UserController {
	
	private static final String REST_URL_PREFIX = "http://springcloud-user";
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MsgIndexService msgIndexService;
	
	@Autowired
	ContentService contentService;

	@RequestMapping("/consumer/logincheck")
	@ResponseBody
	public void logincheck(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Integer accountnum = Integer.parseInt(request.getParameter("accountnum").trim());
		String password = request.getParameter("password").trim();
		String result = restTemplate.getForObject(
				REST_URL_PREFIX+"/consumer/logincheck?account={account}&password={password}",
			    String.class,
			    accountnum, password); //restTemplate会对参数进行URI编码
		if(result.equals("验证成功")){
			User tmp_user=userService.getUser(accountnum);
			try {//password is right.
				Cookie cookie = new Cookie("accountnum",URLEncoder.encode(Integer.toString(accountnum), "utf-8"));
				cookie.setMaxAge(3600*2);
				cookie.setPath("/");
				response.addCookie(cookie);
				Cookie cookie2 = new Cookie("password", URLEncoder.encode(password, "utf-8"));
				//4小时后过期
				cookie2.setMaxAge(3600*2);
				cookie2.setPath("/");
				response.addCookie(cookie2);
				Cookie cookie3 = new Cookie("school", URLEncoder.encode(tmp_user.getSchool(), "utf-8"));
				//4小时后过期
				cookie3.setMaxAge(3600*2);
				cookie3.setPath("/");
				response.addCookie(cookie3);
				Cookie cookie4 = new Cookie("name", URLEncoder.encode(tmp_user.getNickname(), "utf-8"));
				//4小时后过期
				cookie4.setMaxAge(3600*2);
				cookie4.setPath("/");
				response.addCookie(cookie4);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.getWriter().println(result);
	}
	
	@RequestMapping("/consumer/registercheck")
	@ResponseBody
	public void registercheck(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Integer accountnum = Integer.parseInt(request.getParameter("accountnum").trim());
		String nickname = request.getParameter("nickname").trim();
		String school = request.getParameter("school").trim();
        String password = request.getParameter("password").trim();
		String result = restTemplate.getForObject(
				REST_URL_PREFIX+"/consumer/registercheck?accountnum={accountnum}&password={password}&nickname={nickname}&school={school}",
			    String.class,
			    accountnum, password,nickname,school); //restTemplate会对参数进行URI编码
		response.getWriter().println(result);
	}
	
	@RequestMapping("/consumer/updateData")
	@ResponseBody
	public void updateUserData(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Integer accountnum = Integer.parseInt(request.getParameter("accountnum").trim());
		String name = request.getParameter("name").trim();
		String school = request.getParameter("school").trim();
		String password = request.getParameter("password").trim();
		String result = restTemplate.getForObject(
				REST_URL_PREFIX+"/consumer/updateData?accountnum={accountnum}&password={password}&name={name}&school={school}",
			    String.class,
			    accountnum, password,name,school); //restTemplate会对参数进行URI编码
		response.getWriter().println(result);
	}
	
	
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
	
/*	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public String upload(HttpServletRequest req, @RequestParam("imgFile") MultipartFile file) {
        try {
    		//get user unique id as his img name
        	String userId="";
        	Cookie[] cookies = req.getCookies();
        	if (cookies != null) {
        	 for (Cookie cookie : cookies) {
        	   if (cookie.getName().equals("accountnum")) {
        		   userId = cookie.getValue(); 
        	    }
        	  }
        	}
        	
        	//trans img to destination
            String fileName = userId+"."+file.getOriginalFilename().split("\\.")[1];
            String destFileName=req.getServletContext().getRealPath("")+"\\userImg\\"+fileName;
            System.out.println(destFileName);
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            System.out.println(destFile);
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "error," + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "error," + e.getMessage();
        }
         
        return "userData";
}*/
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/consumer/getRecord")
	@ResponseBody
	public List<String> getRecord(int accountNum){
		List<String> data=new ArrayList<String>();
		
		data = restTemplate.getForObject(
				REST_URL_PREFIX+"/consumer/getRecord?accountNum={accountnum}",
				List.class,
			    accountNum); //restTemplate会对参数进行URI编码
		return data;
	}
}
