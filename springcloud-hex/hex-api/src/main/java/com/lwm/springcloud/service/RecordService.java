package com.lwm.springcloud.service;
import java.util.List;

import com.lwm.springcloud.entities.*;


public interface RecordService {
	int addRecord(Record recordkey);
	int delRecord(RecordKey recordkey);
	Record getRecord(RecordKey recordkey);
	void update(Record record);
	public List<Record> list();
}