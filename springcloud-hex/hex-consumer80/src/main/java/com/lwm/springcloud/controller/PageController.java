package com.lwm.springcloud.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

	
	@RequestMapping("/login_register")
	public String login_register(HttpServletRequest request){
		return "login_register";
	}
	
	@RequestMapping("/")
	public String url_index(HttpServletRequest request){
		return "index";
	}
	
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		return "index";
	}
	
	@RequestMapping("/About")
	public String about(HttpServletRequest request){
		return "About";
	}
	
	@RequestMapping("/GameRecord")
	public String gamerecord(HttpServletRequest request){
		return "GameRecord";
	}
	
	@RequestMapping("/hex")
	public String hex(HttpServletRequest request){
		return "hex";
	}

	@RequestMapping("/hex_p2p")
	public String hex_p2p(HttpServletRequest request){
		return "hex_p2p";
	}
	
	@RequestMapping("/chat")
	public String chatRoom(){
		return "chatRoom";
	}
		
	@RequestMapping("/userData")
	public String userData(){
		return "userData";
	}
	
	@RequestMapping("/teach")
	public String teach(){
		return "teach";
	}
	
	@RequestMapping("/pagerror")
	public String error(){
		return "error";
	}
	
	@RequestMapping("/uploadd")
	public String uploadd(){
		return "uploadd";
	}
}
