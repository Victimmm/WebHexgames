package com.lwm.springcloud.serviceimpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lwm.springcloud.dao.ContentMapper;
import com.lwm.springcloud.entities.Content;
import com.lwm.springcloud.entities.ContentExample;
import com.lwm.springcloud.service.ContentService;
@Service("contentService")
public class ContentServiceImpl implements ContentService{
	@Resource
	ContentMapper contentMapper;
	
	@Override
	public Integer add(Content content) {
		// TODO Auto-generated method stub
		try{
			contentMapper.insert(content);
			int id=contentMapper.selectMaxID();
			return id;
		}
		catch(Exception e){
			return -1;
		}
	}

	@Override
	public Content getByID(int contentID) {
		// TODO Auto-generated method stub
		return contentMapper.selectByPrimaryKey(contentID);
	}
	
}
