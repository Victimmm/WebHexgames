package com.lwm.springcloud.entities;

import java.util.Date;

import com.google.gson.Gson;



public class ChatMessage {
	private Integer senderID;
	private Integer receiverID;
	private Date creatTime;
	private String  content;
	private String  msgtype;
	private Integer type;//是普通消息还是验证消息还是。。。
	private Gson gson = new Gson();
	
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public void setType(Integer t){
		type=t;
	}
	public Integer getType(){
		return type;
	}
	public ChatMessage(Integer sID,Integer rID,String  con){
		
		this.senderID = sID;
		this.receiverID = rID;
		this.creatTime = new Date();
		this.content = con;
		this.msgtype = "text";
		
	}
	public Integer getSenderID() {
		return senderID;
	}
	public void setSenderID(Integer senderID) {
		this.senderID = senderID;
	}
	public Integer getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(Integer receiverID) {
		this.receiverID = receiverID;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	//将ChatMessage Object转为Json字符串
	public String toJson(){
		return gson.toJson(this);
	}

}
