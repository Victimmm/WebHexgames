package com.lwm.springcloud.websocket;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.lwm.springcloud.entities.ChatMessage;
import com.lwm.springcloud.entities.Content;
import com.lwm.springcloud.entities.ContentExample;
import com.lwm.springcloud.entities.ContentVo;
import com.lwm.springcloud.entities.Msgindex;

import com.lwm.springcloud.entities.User;
import com.lwm.springcloud.service.ContentService;
import com.lwm.springcloud.service.MsgIndexService;

@Component
//访问服务端的url地址
@ServerEndpoint(value = "/chatSocket/{userNum}") 
public class ChatSocket {
	
	public static ContentService contentService;
	public static MsgIndexService msgIndexService;
	
	private Integer userNum;
	
	private static Gson gson = new Gson();
	
	
	
	//ChatSocket类的静态成员--所有websock session和好友id,以及一个"姓名：session"的字典
	private static List<Session> sessions = new ArrayList<Session>();
	private static List<Integer> nums = new ArrayList<Integer>();
	private static Map<Integer,Session> map = new HashMap<Integer,Session>();
	
	@OnOpen
	public void open(@PathParam(value = "userNum") Integer num,Session session){
		
		
		/*当前的websocket的session对象不是servlet中的session!!*/
		
		//获取新用户姓名
		String QueryString = session.getQueryString();
		//System.out.println("ChatSocket get:"+QueryString);
		userNum = num;
		
		//将新用户session和姓名添加至ChatSocket类的静态成员列表中
		ChatSocket.sessions.add(session);
		ChatSocket.nums.add(userNum);
		ChatSocket.map.put(this.userNum, session);
		System.out.println("userNum="+userNum+"上线了");
		
		/*//新建一条群发的欢迎信息
		String msg = "欢迎"+this.userNum+"进入聊天室!";
		Message message = new Message();
		message.setWelcome(msg);
		message.setUsernames(ChatSocket.nums);
		
		//广播发送给所有好友
		this.broadcast(ChatSocket.sessions,message.toJson());
		System.out.println("broadcast done!");*/
	}
	
	//向所有session广播发送msg
	public void broadcast(List<Session> ss,String msg){
		
		for(Iterator<Session> iterator = ss.iterator();iterator.hasNext();){
			Session session = (Session)iterator.next();
			try {
				session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@OnClose
	public void close(Session session){
		System.out.println("userNum="+userNum+"下线了");
		ChatSocket.sessions.remove(session);
		ChatSocket.nums.remove(this.userNum);
		ChatSocket.map.remove(this.userNum, session);
		
		
		
		/*//新建一条群发的退出信息
		String msg = this.userNum+"已退出聊天室!";
		Message message = new Message();
		message.setWelcome(msg);
		message.setUsernames(ChatSocket.nums);
		
		//向所有还在群中的用户发送离开公告
		broadcast(sessions,message.toJson());*/
		
		
	}
	
	
	
	 @OnMessage
	 public void onMessage(String json, Session session) throws IOException{
		ContentVo vo = new ContentVo(json);
		if(vo.getType()==2){
			System.out.println("userNum="+vo.getFrom()+"向userNum="+vo.getTo()+"发送了type="+vo.getMsgType()+"的消息："+vo.getMsg());
			Integer to = vo.getTo();
			Session to_session = this.map.get(to);
			
			//新增数据库
			ChatMessage message = new ChatMessage(vo.getFrom(),vo.getTo(),vo.getMsg());
			message.setMsgtype(vo.getMsgType());
			message.setType(vo.getType());
			//System.out.println("messageJson:"+message.toJson());
			
			Content content=new Content();
			content.setContentid(0);
			content.setCreatetime(message.getCreatTime());
			content.setMessage(vo.getMsg());
			content.setType(vo.getMsgType());
			Integer contentID=contentService.add(content);
			
			Msgindex ind1=new Msgindex ();
			ind1.setContentid(contentID);
			ind1.setOwner(vo.getFrom());
			ind1.setFriend(vo.getTo());		
			ind1.setReceive(false);
			ind1.setUnread(false);
			
			Msgindex ind=new Msgindex ();
			ind.setContentid(contentID);
			ind.setOwner(vo.getTo());
			ind.setFriend(vo.getFrom());
			ind.setReceive(true);
			ind.setUnread(true);
			
			msgIndexService.add(ind1);
			msgIndexService.add(ind);
			
			
			if(to_session!=null){
				
				try {
					to_session.getBasicRemote().sendText(message.toJson());
					//session.getBasicRemote().sendText(message.toJson());
				} catch (IOException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		else if(vo.getType()==3){
			
		}else{
			
		}
		
		
		/*
		ContentVo vo = new ContentVo(json);
		InputStream in = null;
		IndexMapper indexMapper =null;
		ContentMapper contentMapper=null;
		RelationMapper relationMapper=null;
		UserMapper userMapper=null;
		VerificationMapper verificationMapper=null;
		SqlSession sqlSeesion=null;
		
		try{
			in = Resources.getResourceAsStream("mybatis-config.xml"); //根据相关的 mybatis 配置文件， 创建连接 SQLSessionFactory 连接对象
			SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in); //创建出 SQLSession 对象 
			sqlSeesion = factory.openSession(); //通过 sqlSession 取到映射接口
			indexMapper=sqlSeesion.getMapper(IndexMapper.class);
			contentMapper=sqlSeesion.getMapper(ContentMapper.class);
			relationMapper=sqlSeesion.getMapper(RelationMapper.class);
			userMapper=sqlSeesion.getMapper(UserMapper.class);
			verificationMapper=sqlSeesion.getMapper(VerificationMapper.class);
		}catch (IOException e) { 
			e.printStackTrace(); 
		} 
		
		if(vo.getType()==2){
			//单聊
			//根据当前userid 找到对应的session对象
			System.out.println("userNum="+vo.getFrom()+"向userid="+vo.getTo()+"发送了type="+vo.getMsgType()+"的消息："+vo.getMsg());
			Integer to = vo.getTo();
			Session to_session = this.map.get(to);
			
			//新增数据库
			ChatMessage message = new ChatMessage(vo.getFrom(),vo.getTo(),vo.getMsg());
			message.setMsgtype(vo.getMsgType());
			message.setType(vo.getType());
			
			Content content=new Content();
			content.setContentid(0);
			content.setCreatdate(message.getCreatTime());
			content.setMessage(message.getContent());
			content.setType(message.getMsgtype());
			contentMapper.insert(content);
			sqlSeesion.commit();
			
			Integer contentID=contentMapper.getMaxID();
			
			Msgindex ind1=new Msgindex ();
			ind1.setContentid(contentID);
			ind1.setOwner(message.getSenderID());
			ind1.setFriend(message.getReceiverID());		
			ind1.setReceive(false);
			ind1.setUnread(false);
			
			Msgindex ind=new Msgindex ();
			ind.setContentid(contentID);
			ind.setOwner(message.getReceiverID());
			ind.setFriend(message.getSenderID());
			ind.setReceive(true);
			ind.setUnread(true);
			
			
			
			indexMapper.insert(ind);
			indexMapper.insert(ind1);
			sqlSeesion.commit();
			
			
			if(to_session!=null){
				
				try {
					to_session.getBasicRemote().sendText(message.toJson());
					//session.getBasicRemote().sendText(message.toJson());
				} catch (IOException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		
			
		}
		else if(vo.getType()==3){
			System.out.println("Server get info about form"+vo.getFrom()+" to "+vo.getTo());
			Integer to = vo.getTo();
			Session to_session = this.map.get(to);
			//如果对方在线，就直接发送验证信息
			if(to_session!=null){
				System.out.println("userNum="+vo.getTo()+"的用户在线，将userid="+vo.getFrom()+"的好友请求信息发送给他");
				ChatMessage message = new ChatMessage(vo.getFrom(),vo.getTo(),vo.getMsg());
				System.out.println("Type:"+vo.getType());
				
				message.setType(vo.getType());
				try {
					to_session.getBasicRemote().sendText(message.toJson());
				} catch (IOException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{//如果对方不在线，就需要存到数据库里verification表
				System.out.println("因为userid="+vo.getTo()+"的用户不在线，所有向表verification插入("+vo.getFrom()+","+vo.getTo()+")");
				VerificationKey vk=new  VerificationKey();
				vk.setSendid(vo.getFrom());
				vk.setReceiveid(vo.getTo());
				verificationMapper.insert(vk);
				sqlSeesion.commit();
			}
		}
		else if(vo.getType()==4){
			System.out.println("用户userid="+vo.getFrom()+"成功添加userid="+vo.getTo()+"现将好友关系写入数据库");
			//将两人的id 存入relation表中
			
			RelationKey relation =new RelationKey();
			relation.setOwner(vo.getFrom());
			relation.setFriend(vo.getTo());
			relationMapper.insert(relation);
			
			relation.setOwner(vo.getTo());
			relation.setFriend(vo.getFrom());
			relationMapper.insert(relation);
			sqlSeesion.commit();
			Integer to = vo.getTo();
			Session to_session = this.map.get(to);
			if(to_session!=null){//如果对方在线，则让对方在联系人表加上
				User friend=userMapper.selectByPrimaryKey(vo.getFrom());
				ChatMessage message = new ChatMessage(vo.getFrom(),vo.getTo(),friend.getNickname());				
				message.setType(vo.getType());
				try {
					to_session.getBasicRemote().sendText(message.toJson());
				} catch (IOException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			to_session = this.map.get(vo.getFrom());
			if(to_session!=null){//如果自己在线，则发送信息让在自己联系人表加上
				User friend=userMapper.selectByPrimaryKey(vo.getTo());
				ChatMessage message = new ChatMessage(vo.getTo(),vo.getFrom(),friend.getNickname());				
				message.setType(vo.getType());
				try {
					to_session.getBasicRemote().sendText(message.toJson());
				} catch (IOException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		*/
		
	}
	    @OnError
	    public void onError(Session session, Throwable error) {
	    	System.out.println("发生错误");
	        error.printStackTrace();
	    }
	

}
