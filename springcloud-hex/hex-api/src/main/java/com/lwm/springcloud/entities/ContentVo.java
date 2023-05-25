package com.lwm.springcloud.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentVo {
	JSONObject jo;
	public ContentVo(String s){
		try {
			jo=new JSONObject(s);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Integer getFrom(){
		Integer From = null;
		try {
			From = jo.getInt("from");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return From;
	}
	
	public Integer  getTo(){
		Integer To = null;
		try {
			To = jo.getInt("to");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return To;
	}
	public String getMsg(){
		String Msg = null;
		try {
			Msg = jo.getString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Msg;
	}
	public String getMsgType(){
		String type = null;
		try {
			type = jo.getString("msgtype");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}
	public Integer getType(){
		Integer type = null;
		try {
			type = jo.getInt("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
			
	}*/
}
