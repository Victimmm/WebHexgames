package com.lwm.springcloud.serviceimpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lwm.springcloud.dao.MsgindexMapper;
import com.lwm.springcloud.entities.Msgindex;
import com.lwm.springcloud.entities.MsgindexExample;
import com.lwm.springcloud.service.MsgIndexService;

@Service("msgIndexService")
public class MsgIndexServiceImpl implements MsgIndexService{
	@Resource
	MsgindexMapper msgindexMapper;

	@Override
	public boolean add(Msgindex msgindex) {
		// TODO Auto-generated method stub
		try{
			msgindexMapper.insert(msgindex);
			return true;
		}
		catch(Exception e){
			return false;
		}
		
		
	}

	@Override
	public List<Msgindex> getMsg(int owner, int friend) {
		// TODO Auto-generated method stub
		MsgindexExample example=new MsgindexExample();
		MsgindexExample.Criteria cri=example.createCriteria();
		cri.andOwnerEqualTo(owner);
		cri.andFriendEqualTo(friend);
		return msgindexMapper.selectByExample(example);
	}

	@Override
	public Integer getUnreadNum(int owner, int friend) {
		// TODO Auto-generated method stub
		MsgindexExample example=new MsgindexExample();
		MsgindexExample.Criteria cri=example.createCriteria();
		cri.andOwnerEqualTo(owner);
		cri.andFriendEqualTo(friend);
		cri.andReceiveEqualTo(true);
		cri.andUnreadEqualTo(true);
		return msgindexMapper.countByExample(example);
	}

	@Override
	public void setRead(int owner, int friend) {
		// TODO Auto-generated method stub
		MsgindexExample example=new MsgindexExample();
		MsgindexExample.Criteria cri=example.createCriteria();
		cri.andOwnerEqualTo(owner);
		cri.andFriendEqualTo(friend);
		cri.andUnreadEqualTo(true);
		List<Msgindex> results=  msgindexMapper.selectByExample(example);
		for( Msgindex index:results){
			index.setUnread(false);
			msgindexMapper.updateByPrimaryKey(index);
		}
	}

	@Override
	public Integer getRecentContentID(int owner, int friend) {
		// TODO Auto-generated method stub
		MsgindexExample example=new MsgindexExample();
		example.setOrderByClause("contentID desc");
		MsgindexExample.Criteria cri=example.createCriteria();
		cri.andOwnerEqualTo(owner);
		cri.andFriendEqualTo(friend);
		List<Msgindex> results=  msgindexMapper.selectByExample(example);
		if(results.size()>0){
			return results.get(0).getContentid();
		}else
			return -1;
	}
	
	
}
