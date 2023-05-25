package com.lwm.springcloud.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.lwm.springcloud.service.*;


@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    @Autowired 
    public void setMessageService(ContentService contentService ,MsgIndexService msgIndexService,UserService userservice ,RecordService recordservice){
    	ChatSocket.contentService=contentService;
    	ChatSocket.msgIndexService=msgIndexService;
    	HexServer.userservice=userservice;
    	HexServer.recordservice=recordservice;
    	
    }
    
}
