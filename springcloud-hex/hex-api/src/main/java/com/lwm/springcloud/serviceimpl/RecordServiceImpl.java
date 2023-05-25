package com.lwm.springcloud.serviceimpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.lwm.springcloud.dao.*;
import com.lwm.springcloud.entities.*;
import com.lwm.springcloud.service.*;



@Service("recordService")
public class RecordServiceImpl implements RecordService {
	@Resource
	RecordMapper recordMapper;
	
	@Override
	public int addRecord(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.insert(record);
	}

	@Override
	public int delRecord(RecordKey recordkey) {
		
		return recordMapper.deleteByPrimaryKey(recordkey);
	}
	
	@Override
	public Record getRecord(RecordKey recordkey) {
		// TODO Auto-generated method stub
		return recordMapper.selectByPrimaryKey(recordkey);
	}
	
	@Override
	public List<Record> list() {
		// TODO Auto-generated method stub
		return recordMapper.selectByExample(new  RecordExample());
	}
	
	@Override
	public void update(Record record) {
		// TODO Auto-generated method stub
		recordMapper.updateByPrimaryKey(record);
	}

}
