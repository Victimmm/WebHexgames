//1、判断是否登陆，登陆状态下直连websocket
var user_link = $.cookie("accountnum");//[[${username}]];
if(user_link!=null){
	var hexsocket = new WebSocket("ws://"+window.location.host+"/websocket/"+user_link);
//	var chatsocket = new WebSocket("ws://"+window.location.host+"/chatSocket/"+user_link);
	//非功能页的websocket 需求：1、能够处理好友请求，2、 显示聊天室的在线人数及消息提示
	hexsocket.onmessage=function (event){
    	var msg=JSON.parse(event.data);
    	var msgtype=msg.msgtype;
    	switch(msgtype){
    	case "battlemsg": link_alersure(msg.sender,msg.message);break;
    	
    	default : break;
    	}
    }
}

console.log(navigator.appName);

function link_alersure(contacts,oppteam){
	
	oppc=(oppteam=="1")?"red":"blue";
	var r=confirm(contacts+"玩家请求与您对战，对方队伍为"+oppc);
	//确认则发送确认信息给请求方，同时修改游戏必备的参数
	if(r==true){
		//准备好后将消息传回对方：
        var msg={
            	msgtype:"confirmbattle",
            	sender:user_link,
            	receiver:contacts,
            	message:"agree"
            };
        hexsocket.send(JSON.stringify(msg));
		//带参数跳转
		window.location.href="/hex_p2p?fight=true&contacts="+contacts+"&oppteam="+oppteam;
	}
	else{
        var msg={
            	msgtype:"confirmbattle",
            	sender:user_link,
            	receiver:contacts,
            	message:"disagree"
            };
        hexsocket.send(JSON.stringify(msg));
	}
	
}