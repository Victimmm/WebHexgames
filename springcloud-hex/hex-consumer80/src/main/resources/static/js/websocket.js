 
	var link_parameter=getQueryString("fight");
	

	var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://"+window.location.host+"/websocket/"+user);
    }
    else {
        alert('当前浏览器不支持 websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
    	showOnChatroom("连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {    	      
        showOnChatroom("服务器连接成功");
    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        showOnChatroom("服务器连接关闭");
        
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
 		websocket.close();
    }

	if(link_parameter=="true"){
		oppteam=getQueryString("oppteam");
		contacts=getQueryString("contacts");
		
    	//初始化棋盘
		for ( i=0;i<11;i++)
	       	for( j=0;j<11;j++)
	       	{	document.getElementById(i+","+j).className="button gray larrowG"; 
	       		board[i][j]=0;
	       	}
		team=(oppteam=="1")?"2":"1";
		isrun=true;
		isundo=true;
		movenum=0;
		oppcolor=(team==2)?"button red larrowR":"button blue larrowB";
		current=1;
		enemy=contacts;
	}
    
    //接收到消息的回调方法(使用json传递数据)
    websocket.onmessage = function (event) {
    	
    	var msg=JSON.parse(event.data);
    	var msgtype=msg.msgtype;
    	//根据消息类型
    	switch(msgtype){
    		case "chat": var contacts=msg.sender;
			 var message=msg.message;
			//收到自己的消息不予显示
				if(contacts!=user){
			        var txt=document.createElement("div");
			        txt.className="friendMsg";
			        var time=document.createElement("p");
			        time.className="time";
			        time.innerHTML=new Date().format("yyyy-MM-dd hh:mm:ss");
			        
			        var mymsg=document.createElement("p");
			        mymsg.className="msg";
			        mymsg.innerHTML=message;
			        
			        txt.appendChild(time);
			        txt.appendChild(mymsg);
			        $("#message").append(txt);
			        document.getElementById('message').scrollTop=document.getElementById('message').scrollHeight;
		        }
		        break;
    		case "return": 
    			if(msg.message=="busy")
    				window.alert("对方对战中");
    			else 
    				window.alert("对方不在线");
    			break;
    		case "battlemsg": alersure(msg.sender,msg.message);break;
    		
    		case "confirmbattle":issure(msg.sender,msg.message);break;
    		
    		case "setpiece" :setpiece(msg.message);break;
    		
    		case "endbattle":
    			if(msg.sender==enemy)
	    		{ 
    				showOnChatroom("对方已经退出和您的对战，系统已经记录下对方的行为，并给予一定的处罚");
	    			//结束对战，并修改服务器的状态
    				isrun=false;
    				enemy="";
    				isundo=true;
    				team="";
    				var msg={
    					msgtype:"setfree",
    					sender:user,
    					message:"oppexit",
    					enemy:enemy
    				}
    				websocket.send(JSON.stringify(msg));
	    		}
    			break;
    		//回退
    		case "undo" :
    			alerundosure(msg.sender);
    			break;
    		case "sureundo":
    			isundo=true;
    			if(msg.message=="agree"){
    				if(team=="1"&&current==1 ||team=="2"&&current==2 ){
    	        		userundo(2);
    	        	}
    	        	//
    	        	else 
    	        	{
    	        		userundo(1);
    	        	}
    				showOnChatroom("对方已同意悔棋，现在是您的回合")
    			}
    			else{
    				showOnChatroom("对方不同意您悔棋，请继续")
    			}
    		
    			break
    			//处理心跳机制传过来的在线总人数及
    		case "heartbeats":console.log("hhhh");
    		default: break;
    	}
//    	var control=event.data.substring(0,1);
//    	//0为普通消息,4为在线人数更新 ,1为接受到的确认请求的消息
//    	switch(control){
//    	case "0":var contacts=event.data.substring(1,9);
//				 var news=event.data.substring(10);
//				//收到自己的消息不予显示
//					if(contacts!=user){
//			        document.getElementById('message').innerHTML +='<p align="left">'+contacts+"	"+new Date().toLocaleString()+'<br/>'+ news + '</p>';
//			        document.getElementById('message').scrollTop=document.getElementById('message').scrollHeight;
//			        }
//			        break;
//		//弹出
//		case "1":var contacts=event.data.substring(1,9);var oppteam=event.data.substring(9);alersure(contacts,oppteam);break;
//		//确认对战与否的消息
//		case "2":issure(event.data.substring(1));
//		//3为对战的走棋消息
//		case "3":setpiece(event.data.substring(9));break;
//		case "4":document.getElementById("online").innerHTML="在线人数："+event.data.substring(1)+"人";break;
//    	case "6":window.alert("对方退出了与您的对战,强烈谴责对方！");run=false;enemy="";break;
//    	case "7":var contacts=event.data.substring(1,9);
//    			if(contacts==enemy){
//    				window.alert("对方退出了与您的对战,强烈谴责对方！");
//    				run=false;
//    				enemy="";
//    				websocket.send("5");
//    			}
//    			break;
//    	default: break;
//    	}
    }
    
    function getQueryString(name) {
        let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        let r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return decodeURIComponent(r[2]);
        };
        return null;
     }
    
    //弹出悔棋的确认弹窗
    function alerundosure(enemy){
    	var r=confirm(enemy+"玩家请求悔棋");
    	//确认则发送确认信息给对方，同时根据情况修改棋盘
    	if(r==true){
        	//当前用户走棋时，需要回退1步
        	if(team=="1"&&current==1 ||team=="2"&&current==2 ){
        		userundo(1);
        	}
        	//
        	else 
        	{
        		userundo(2);
        	}
    		var msg={
    				sender:user,
    				receiver:enemy,
    				msgtype:"sureundo",
    				message:"agree"
    		}
        	websocket.send(JSON.stringify(msg)); 
    	}
    	else{
    		var msg={
    				sender:user,
    				receiver:enemy,
    				msgtype:"sureundo",
    				message:"disagree"
    		}
    		websocket.send(JSON.stringify(msg)); 
    	}
    }
    
    function setpiece(oppid) {
    		//悔棋过程中不允许走棋
    		if(!isundo)
    			return;
			//test.innerHTML=oppid;
			var Move=oppid;
			document.getElementById("vall").innerHTML="对方："+Move;//
			var xy=Move.split(",",2);
      		var x=parseInt(xy[0]);
      		var y=parseInt(xy[1]);
      		document.getElementById(x+","+y).className=oppcolor;
      		document.getElementById("vall").innerHTML="对方："+oppid;
      		board[x][y]=current;
      		history[movenum]=Move;
      		movenum++;
      		current = (current%2) + 1;
      		checkWinner();
    }
    
    //根据对手的状态做出相应的设置
    function issure(oppname,oppstate){
    	if(oppstate=="agree"){
    		window.alert(oppname+"同意对战，对战开始！");
    		for (i=0;i<11;i++)
		       	for( j=0;j<11;j++)
		       	{	
		       		document.getElementById(i+","+j).className="button gray larrowG";
		       		board[i][j]=0;
		       	}
    		current=1;
			team=teams;
    		isrun=true;
    		isundo=true;
    		movenum=0;
    		oppcolor=(team==2)?"button red larrowR":"button blue larrowB";
    		enemy=oppname;
    	}
    	else{
    		window.alert(oppname+"不在线或不同意对战");
    	}
    }
    
    //应该设置超时关闭的功能。弹出确认对战的消息
    function alersure(contacts,oppteam){
    	oppc=(oppteam=="1")?"red":"blue";
    	var r=confirm(contacts+"玩家请求与您对战，对方队伍为"+oppc);
    	//确认则发送确认信息给请求方，同时修改游戏必备的参数
    	if(r==true){
    	//初始化棋盘
    		for ( i=0;i<11;i++)
		       	for( j=0;j<11;j++)
		       	{	document.getElementById(i+","+j).className="button gray larrowG"; 
		       		board[i][j]=0;
		       	}
    		team=(oppteam=="1")?"2":"1";
    		isrun=true;
    		isundo=true;
    		movenum=0;
    		oppcolor=(team==2)?"button red larrowR":"button blue larrowB";
    		current=1;
    		enemy=contacts;
    		//准备好后将消息传回对方：
            var msg={
                	msgtype:"confirmbattle",
                	sender:user,
                	receiver:contacts,
                	message:"agree"
                };
    		websocket.send(JSON.stringify(msg));
    	}
    	//取消则发送拒绝信息给请求方,
    	else{
            var msg={
                	msgtype:"confirmbattle",
                	sender:user,
                	receiver:contacts,
                	message:"disagree"
                };
    		websocket.send(JSON.stringify(msg));
    	}
    }

    //设置回车发送消息，回车的键值为13
	document.onkeyup = function(e) {
	  // 兼容FF和IE和Opera
	  var event = e || window.event;
	  var key = event.which || event.keyCode || event.charCode;
	  if (key == 13) {
	    send();
	  }
	}

    //发送消息
    function send() {
    	//非对战情况不允许发信息
    	if(!isrun)
    		return;
        var message = document.getElementById('text').value;
        if(message=="")
        	return ;
        var msg={
        	msgtype:"chat",
        	sender:user,
        	receiver:enemy,
        	message:message
        };
        var str = JSON.stringify(msg);
        websocket.send(str);
        
        var txt=document.createElement("div");
        txt.className="myMsg";        
        var time=document.createElement("p");
        time.className="time";
        time.innerHTML=new Date().format("yyyy-MM-dd hh:mm:ss");
        
        var mymsg=document.createElement("p");
        mymsg.className="msg";
        mymsg.innerHTML=message;
        
        txt.appendChild(time);
        txt.appendChild(mymsg);
        $("#message").append(txt);
//        document.getElementById('message').innerHTML +='<p align="right">'+user+"	"+new Date().toLocaleString()+'<br/>'+ message + '</p>';
        document.getElementById('message').scrollTop=document.getElementById('message').scrollHeight;
        document.getElementById('text').value="";
        document.getElementById('text').focus();
        }
    
    
    function get_integral(){
    	var integral;
    	$.ajax({
    	    url: "/get_integral",
    	    data: {user: user},
    	    type: "POST",
    	    success: function(data) {
    	    		integral=data;
    	    	}
    	});
    }
    //发送消息给对手,请求对战
    //条件：我方为非对战状态，且选好队伍属性及对战方（聊天框中的头像）
    function find(){
    	$.ajax({
    	    url: "/get_integral",
    	    data: {user: user},
    	    type: "POST",
    	    async:false,
    	    success: function(result) {
    	    			if(result<300){
    	    				window.alert("积分不足");
    	    				integral=result;
    	    				console.log(result);
    	    				return;
    	    			}
    	    	}
    	});
    	if(integral<300){
    		return;
    	}
	   var radio=document.getElementsByName("Team");
	   for(var i=0;i<radio.length;i++){
		  		if(radio[i].checked==true){
		  			teams=radio[i].value;
		  			break;
		  		}
		}
	   //未设置好参数不允许开始
	    if(oppuser==null||teams==null)
	    	return;
	//非对战状态判断
    	if(enemy==""){
            var msg={
                	msgtype:"battlemsg",
                	sender:user,
                	receiver:oppuser,
                	message:teams
                };
    		
    		websocket.send(JSON.stringify(msg));
   	}
    	else
    		window.alert("请等待本场比赛结束");
    }
    
    Date.prototype.format = function(fmt) { 
        var o = { 
           "M+" : this.getMonth()+1,                 //月份 
           "d+" : this.getDate(),                    //日 
           "h+" : this.getHours(),                   //小时 
           "m+" : this.getMinutes(),                 //分 
           "s+" : this.getSeconds(),                 //秒 
           "q+" : Math.floor((this.getMonth()+3)/3), //季度 
           "S"  : this.getMilliseconds()             //毫秒 
       }; 
       if(/(y+)/.test(fmt)) {
               fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
       }
        for(var k in o) {
           if(new RegExp("("+ k +")").test(fmt)){
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            }
        }
       return fmt;
   }       
    
    //好友名称及id如何传输到函数中
    function choose(id){
    	//对战中不允许改变
    	if(isrun)
    		return;
    	oppuser=id;
    	var username
    	//通过获取json用户表，获取用户昵称
    	for(var i=0;i<userlist.length;i++){
    		if(userlist[i].accountnum==id){
    			username=userlist[i].nickname;
    		}
    	}
//		<img src="../static/images/core-img/user4.png"/>
//		<span>廖威明</span>
    	var usermsg=document.createElement("div");
    	usermsg.className="title";
    	usermsg.setAttribute("id", "usermsg");
    	
    	var avatar=document.createElement("img");

    	avatar.setAttribute("src", "../static/images/avatar/"+id+".png");
    	avatar.setAttribute("onerror", "this.src='../static/images/avatar/default.png'");
    	var name= document.createElement("span");
    	name.innerHTML=username;
    	
    	usermsg.appendChild(avatar);
    	usermsg.appendChild(name);
    	$("#usermsg").replaceWith(usermsg);
    }
    
    //以红色的字体显示在聊天框中
    function showOnChatroom(message){
    	
        var txt=document.createElement("div");
        txt.className="stateMsg";        
        var time=document.createElement("p");
        time.className="time";
        time.innerHTML=new Date().format("yyyy-MM-dd hh:mm:ss");
        
        var mymsg=document.createElement("p");
        mymsg.className="msg_state";
        mymsg.innerHTML=message;
        
        txt.appendChild(time);
        txt.appendChild(mymsg);
        $("#message").append(txt);
        document.getElementById('message').scrollTop=document.getElementById('message').scrollHeight;
    }
    
  //处理好友对战的请求
    function dill_userundo(){
    	//回退请求发出后到收到回复前，不允许走棋
    	
    	if(!isrun||enemy=="")
    		return;
    	isundo=false;
    	//发送回退的请求，当对方同意时下一步
    	var msg={
    			sender:user,
    			receiver:enemy,
    			msgtype:"undo"
    	};
    	websocket.send(JSON.stringify(msg));
    }