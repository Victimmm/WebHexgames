package com.lwm.springcloud.websocket;

/**
* @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
* 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
*/
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lwm.springcloud.entities.*;
import com.lwm.springcloud.service.RecordService;
import com.lwm.springcloud.service.UserService;

import javax.websocket.*;
import javax.websocket.server.PathParam; 
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


@Component
//访问服务端的url地址
@ServerEndpoint(value = "/websocket/{id}") 
public class HexServer {
	
    private static int onlineCount = 0;
    private static HashMap<String, HexServer> webSocketSet = new HashMap<>();
    
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String userid;
    private boolean isbattle=false;
    private static boolean thread_exist=false;
    
    public static  UserService userservice;
    public static RecordService recordservice;
    
    public class HeartBeats implements Runnable{
		   private Thread t;
		   private String threadName;
		   private boolean isexist=false;
		   public boolean exist_state(){
			   return isexist;
		   }
		   public HeartBeats( String name) {

			  isexist=true;
		      threadName = name;
		      System.out.println("Creating " +  threadName );
		   }
		  
		public void run() {
		      try {
		    	  while (true){
		          JSONObject newMsg=new JSONObject();
		          newMsg.put("receiver","all");
		          newMsg.put("msgtype", "heartbeats");
		          newMsg.put("message","break");
		          if(!webSocketSet.isEmpty()){
		          	  Iterator<String> it = webSocketSet.keySet().iterator();
		          	  webSocketSet.get(it.next()).sendtoAll(newMsg);
		          }
		          System.out.println(threadName);
		    	  Thread.sleep(1000*10);
		    	  
		    	  }
		      }catch (InterruptedException e) {
		         System.out.println("Thread " +  threadName + " interrupted.");
		      } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		   
		   public void start () {
		      System.out.println("Starting " +  threadName );
		      if (t == null) {
		         t = new Thread (this, threadName);
		         t.start ();
		      }
		   }
    }
    
    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public void onOpen(@PathParam(value = "id") String id, Session session) {
    	session.getUserProperties().put( "org.apache.tomcat.websocket.BLOCKING_SEND_TIMEOUT",1000L); 
        this.session = session;
        this.userid = id;//接收到发送消息的人员编号
        webSocketSet.put(id, this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("用户"+id+"加入！当前在线人数为" + getOnlineCount());
        if(!thread_exist){
        	System.out.println("111");
        	thread_exist=true;
        	HeartBeats R1 = new HeartBeats( userid+"===heartbeats");
        	R1.start();
        }
    }

    /**
     * 连接关闭调用的方法
     * @throws IOException 
     */
    @OnClose
    public void onClose() throws IOException {
    	
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        JSONObject newMsg=new JSONObject();
        newMsg.put("sender", userid);
        newMsg.put("receiver","all");
        newMsg.put("msgtype", "endbattle");
        newMsg.put("message","break");
//        sendtoAll(newMsg);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @throws IOException 
     * @throws JSONException */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
       	JSONObject msg = JSONObject.parseObject(message);
       	JSONObject newMsg=new JSONObject();
    	switch(msg.getString("msgtype")){
    	/*
    	 对话信息{
    	 msgtype:chat
    	 sender:***
    	 receiver:***
    	 message:***}*/
    		case "chat": 
    			this.sendtoUser(msg); 
    			break;
    		//下棋信息
    		case "setpiece":
    			sendtoUser(msg); 
    			break;
    		//对战信息 
    		case "battlemsg": 
    			//对手在线且空闲
    			if(webSocketSet.containsKey(msg.getString("receiver")) && webSocketSet.get(msg.getString("receiver")).isbattle==false)
    				this.sendtoUser(msg);
    			else {
    				newMsg.put("sender", userid);
    				newMsg.put("receiver", userid);
    				newMsg.put("msgtype","return");
    				//在线情况为忙碌
    				if(webSocketSet.containsKey(msg.getString("receiver")))
    					newMsg.put("message","busy");
    				else
    					newMsg.put("message","outline");
    				sendtoUser(newMsg);
    			}
    			break;
    		//对战的确认信息
    		case "confirmbattle": 
    			//同意对战
    			if(msg.getString("message").equals("agree")){
    				this.isbattle=true;
    				sendtoUser(msg);
    			}
    			else {
    				sendtoUser(msg);
    			}
    			break;
    		//退出对战的信息 
    		case "setfree": 
    			this.isbattle=false ;
    			//扣除对应的积分
    			String exit_team=msg.getString("enemy");
    			System.out.print(exit_team);
//    			User enemy=userservice.getUser(Integer.valueOf(exit_team));
//    			enemy.setIntegral(enemy.getIntegral()-200);
    			break; 
    		//对战正常结束，修改数据库
    		/*
    		  	    	msgtype:"battleover",
	    				sender:user,
	    				redteam:redteam	,
	    				blueteam:blueteam ,				
	    				winteam:"red" ,				
	    				manual:record
    		 */
    		case "battleover":
    			Record record=new Record();
    			String redteam=msg.getString("redteam");
    			String blueteam=msg.getString("blueteam");
    			String winteam=msg.getString("winteam");
    			String manual=msg.getString("manual");
    			
    			//改变双方的 状态
    			this.isbattle=false;
    			String oppteam=winteam.equals("red")?blueteam:redteam;
    			webSocketSet.get(oppteam).isbattle=false;
    			String winner=winteam.equals("red")?"R":"B";
    			
    			record.setBluesquare(Integer.valueOf(blueteam));
    			record.setRedsquare(Integer.valueOf(redteam));
    			record.setManual(manual);
    			record.setBattletime(new Date());
    			record.setWinner(winner);
    			record.setTotalTime(20);
    			//将对战记录存入数据库
    			recordservice.addRecord(record);
    			//修改积分表
    			//1 获取用户及队伍
    			User user=userservice.getUser(Integer.valueOf(msg.getString("sender")));
    			User enemy=userservice.getUser(Integer.valueOf(oppteam));
    			int num=user.getIntegral();
//    			int enemy_match_num=user.getGameMatch();
    			if(msg.get("sender").equals(blueteam))
    			{	
    				num+=100;
    			}
    			else 
    			{
    				num+=60;
    			}
    			user.setIntegral(num);
    			user.setWin(user.getWin()+1);
    			user.setGameMatch(user.getGameMatch()+1);
    			enemy.setGameMatch(enemy.getGameMatch()+1);
    			userservice.update(user);
    			userservice.update(enemy);
    			System.out.print(manual+"	"+redteam+"	"+blueteam);
    			break;
    		//回退的请求
    		case "undo":
    			sendtoUser(msg); 
    			break;
    		case "sureundo":
    			sendtoUser(msg);
    			break;
    		case "Man-machine battle over":
    			record=new Record();
    			redteam=msg.getString("redteam");
    			blueteam=msg.getString("blueteam");
    			winteam=msg.getString("winteam");
    			manual=msg.getString("manual");
    			winner=winteam.equals("red")?"R":"B";
    			oppteam=winteam.equals("red")?blueteam:redteam;
    			record.setBluesquare(Integer.valueOf(blueteam));
    			record.setRedsquare(Integer.valueOf(redteam));
    			record.setManual(manual);
    			record.setBattletime(new Date());
    			record.setWinner(winner);
    			record.setTotalTime(20);
     			recordservice.addRecord(record);
    			//对用户积分的处理
    			//1 获取用户及队伍
    			user=userservice.getUser(Integer.valueOf(msg.getString("sender")));
    			enemy=userservice.getUser(Integer.valueOf(oppteam));
    			num=user.getIntegral();
    			
    			if(msg.get("sender").equals(blueteam)&&winteam.equals("blue"))
    			{
    				num+=50;
    			}
    			else 
    				if(msg.get("sender").equals(redteam)&&winteam.equals("red"))
    				{
    					num+=30;
    				}
    			user.setIntegral(num);
    			user.setWin(user.getWin()+1);
    			user.setGameMatch(user.getGameMatch()+1);
    			enemy.setGameMatch(enemy.getGameMatch()+1);
    			userservice.update(user);
    			userservice.update(enemy);
    			break;
    			
    		default :break;
    	}
    }
        

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
    	System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    
    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     * @param message
     * @param sendUserId
     * @throws IOException
     */
    public void sendtoUser(JSONObject message) throws IOException {
    	String sendUserId=message.getString("receiver");
        if (webSocketSet.get(sendUserId) != null) {//在线
                webSocketSet.get(sendUserId).sendMessage(message.toString());
        }
    }

    /**
     * 发送信息给所有人
     * @param message
     * @throws IOException
     */
    public void sendtoAll(JSONObject message) throws IOException {
        for (String key : webSocketSet.keySet()) {
	            try {
	                webSocketSet.get(key).sendMessage(message.toString());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        HexServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        HexServer.onlineCount--;
    }
}