package com.lwm.springcloud.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class HexController {
	private static final String REST_URL_PREFIX = "http://AiServer";
	@Autowired
	private RestTemplate restTemplate;
	
    @RequestMapping(value = "/begain",method = RequestMethod.POST)
    @ResponseBody
    public void  begainAI(HttpServletRequest request){
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
    	String username=request.getParameter("username");
    	String team=request.getParameter("team");
    	String diff=request.getParameter("diff");
    	map.add("username",username);
    	map.add("team", team);
    	map.add("diff", diff);
    	HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
    	restTemplate.postForLocation(REST_URL_PREFIX + "/begain", request1);
    }
    
    @RequestMapping(value = "/move",method = RequestMethod.POST)
    @ResponseBody
    public String  move(HttpServletRequest request){
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		String username=request.getParameter("username");
		String id=request.getParameter("id");
    	map.add("username",username);
    	map.add("id", id);
    	HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		return restTemplate.postForObject(REST_URL_PREFIX+"/move",request1,String.class);
    }
    
    @RequestMapping(value = "/undo",method = RequestMethod.POST)
    @ResponseBody
    public void  undo(HttpServletRequest request){
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		String username=request.getParameter("username");
    	map.add("username",username);
    	System.out.println(username);
    	HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		restTemplate.postForLocation(REST_URL_PREFIX+"/undo",request1);
    }
}
