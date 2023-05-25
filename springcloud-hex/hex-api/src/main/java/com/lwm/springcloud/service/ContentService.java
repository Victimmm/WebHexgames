package com.lwm.springcloud.service;

import com.lwm.springcloud.entities.Content;

public interface ContentService {
	Integer  add (Content content);
	Content getByID(int contentID);
}
