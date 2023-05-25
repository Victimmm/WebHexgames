package com.lwm.springcloud.service;

import java.util.List;

import com.lwm.springcloud.entities.Msgindex;

public interface MsgIndexService {
	boolean add(Msgindex msgindex);
	List<Msgindex> getMsg(int owner,int friend);
	Integer getUnreadNum(int owner,int friend);
	
	void setRead(int owner , int friend);
	Integer getRecentContentID(int owner,int friend);
	
	
}
