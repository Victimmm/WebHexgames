//初始化一个11*11de二维数组，记录当前的棋盘
var board=new Array();
var flag=new Array();
for(i=0;i<11;i++)
{	
	flag[i]=new Array();
	board[i]=new Array();
	for(j=0;j<11;j++){
		board[i][j]=0;
		flag[i][j]=false;
	}
}

//游戏进行的标志
var isrun=false;
//客户端的队伍属性
var team="";
//当前下棋的队伍
var current;
//ai默认为蓝色
var AIcolor="button blue larrowB";
//用以保存历史记录，记录当前的步数，用于悔棋功能
var history=new Array();
//总移动步数
var movenum;
//AI下棋时不允许悔棋，设置这个参数判断能否悔棋
var isundo=true;
//对手的颜色属性
var oppcolor;
//记录发送请求对战消息时本方的队伍属性
var teams;
//聊天框中已选择的对手（未开始）
var oppuser;
//对战连接建立后的对手id
var enemy="";
//我方积分，传参用
var integral;


function show(id){
      var xy=id.split(",", 2);
      var x=parseInt(xy[0]);
      var y=parseInt(xy[1]);
      
      if(isundo&&isrun&&current==1&&board[x][y]==0&&team=="1"){
      history[movenum]=id;
      movenum++;
      board[x][y]=current;
      document.getElementById(id).className="button red larrowR";
      current = (current % 2) + 1;
      if(enemy!=""){
    	  var msg={
    			  sender:user,
    			  receiver: enemy,
    			  msgtype:"setpiece",
    			  message:id
    	  }
    	  websocket.send(JSON.stringify(msg));
      }
 	  
 	  if(checkWinner()&&enemy=="")
 		 AI(id);
      val.innerHTML="我方："+id;
      }
      else if(isundo&&isrun&&current==2&&board[x][y]==0&&team=="2"){
         history[movenum]=id;
     	 movenum++;
         board[x][y]=current;
      	 document.getElementById(id).className="button blue larrowB";
      	 current = (current % 2) + 1;
	   	  var msg={
				  sender:user,
				  receiver: enemy,
				  msgtype:"setpiece",
				  message:id
		 }
       	 if(enemy!=""){
 		 	websocket.send(JSON.stringify(msg));
 		 }
 	     if(checkWinner()&&enemy==""){
 	     	AI(id);
 	     }
      	 val.innerHTML="我方："+id;
      }
}

//初始化相关参数，清空棋盘, 
function begin(){	
	if(isrun==true){
		var r=confirm("您正在对局中，是否重新开始？");
		//确认则发送确认信息给请求方，同时修改游戏必备的参数
		if(r==false)
			return;
	}
	history=new Array();
	movenum=0;
	for ( i=0;i<11;i++)
       	for( j=0;j<11;j++)
       	{	document.getElementById(i+","+j).className="button gray larrowG"; 
       		board[i][j]=0;
       	}
	//选择的队伍的值，单选框的标准获取值得方法
  	var radio=document.getElementsByName("Team");
  	for(var i=0;i<radio.length;i++){
  		if(radio[i].checked==true){
  			team=radio[i].value;
  			break;
  		}
  	}
  	if(team==""){
  		return;
  	}
  	
	var diff=document.getElementById("level_choices").value;
	if(team=="1")
	{	AIcolor="button blue larrowB";
	}
	else {
		document.getElementById("5,5").className="button red larrowR";
		board[5][5]=1;
		val.innerHTML="我方：";
		document.getElementById("vall").innerHTML="对方：5,5";
		AIcolor="button red larrowR";
		history[movenum]="5,5";
      	movenum++;
	}
	  var xmlhttp;
	  if (window.XMLHttpRequest)
	  {
	    // IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
	    xmlhttp=new XMLHttpRequest();
	  }
	  else
	  {
	    // IE6, IE5 浏览器执行代码
	    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  } 
		xmlhttp.onreadystatechange=function()
		{
			if (xmlhttp.readyState==4 && xmlhttp.status==200)
			{	
				//document.getElementById("vall").innerHTML=xmlhttp.responseText;//
				current=parseInt(team);
				isrun=true;
				$("#Chessrecord").css("visibility","hidden");
				$("#Chessrecord").css("pointer-events","none");
			}
		}
		//get方法处理开始的请求，创建新的AI类
		xmlhttp.open("post","/begain",true);
		xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	  	xmlhttp.send("username="+user+"&team="+team+"&diff="+diff);
}


function AI(id){
//AI计算的过程中不允许悔棋，得算完才行；
isundo=false;
var xmlhttp1;
if (window.XMLHttpRequest)
  {
    //IE7+, Firefox, Chrome, Opera, Safari浏览器执行代码
    xmlhttp1=new XMLHttpRequest();
  }
  else
  {
    // IE6, IE5 浏览器执行代码
    xmlhttp1=new ActiveXObject("Microsoft.XMLHTTP");
  } 
	xmlhttp1.onreadystatechange=function()
	{
		if (xmlhttp1.readyState==4 && xmlhttp1.status==200)
		{	
			//获得下一步的值后
			var AiMove=xmlhttp1.responseText;
			//test.innerHTML=AiMove;
			var xy=AiMove.split(",", 2);
      		var x=parseInt(xy[0]);
      		var y=parseInt(xy[1]);
      		document.getElementById(x+","+y).className=AIcolor;
      		document.getElementById("vall").innerHTML="对方："+AiMove;//
      		board[x][y]=current;
      		history[movenum]=AiMove;
      		movenum++;
      		current = (current%2) + 1;
      		isundo=true;
      		//sleep(200);
      		checkWinner();
		}
	}
	xmlhttp1.open("post","/move",true);
  	xmlhttp1.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  	xmlhttp1.send("id="+id+"&username="+user);
}

function undocall(){
	if(!isrun)
		return;
	if(movenum>1&&isundo){
	var t=history[movenum-1].split(",");
	document.getElementById(parseInt(t[0])+","+parseInt(t[1])).className="button gray larrowG";
	board[parseInt(t[0])][parseInt(t[1])]=0;
	t=history[movenum-2].split(",");
	document.getElementById(parseInt(t[0])+","+parseInt(t[1])).className="button gray larrowG";
	board[parseInt(t[0])][parseInt(t[1])]=0;
	//document.getElementById(history[movenum-2]+"").className="button gray larrowG";
	//change(history[movenum-2]);
	movenum=movenum-2;
	//向服务器发送相关消息：不要判断了，现在不信还有人用IE5、6
	var xmlhttp=new XMLHttpRequest();
	//get方法
	xmlhttp.open("post","/undo",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("username="+user);
	}
}



//好友对战时的回退操作
function userundo(step_count){
	if(movenum>=1&&step_count==1){
		var t=history[movenum-1].split(",");
		document.getElementById(parseInt(t[0])+","+parseInt(t[1])).className="button gray larrowG";
		board[parseInt(t[0])][parseInt(t[1])]=0;
		movenum=movenum-1;
		current=current%2+1;
	}
	else if(movenum>1&&step_count==2){
		var t1=history[movenum-1].split(",");
		document.getElementById(parseInt(t1[0])+","+parseInt(t1[1])).className="button gray larrowG";
		board[parseInt(t1[0])][parseInt(t1[1])]=0;
		var t2=history[movenum-2].split(",");
		document.getElementById(parseInt(t2[0])+","+parseInt(t2[1])).className="button gray larrowG";
		board[parseInt(t2[0])][parseInt(t2[1])]=0;
		movenum=movenum-2;
	}
}


function exportrecord(){
	if(isrun)
		return;
	//team 1为红，2为蓝
	var record="["+new Date().format("yyyy-MM-dd")+"]"+"[玩家为"+(team=="1"?"红":"蓝")+"方队伍]";
	for(var i=0;i<movenum;i++){
		if(i%2==0)
			record=record+"R("+history[i]+");";
		else 
			record=record+"B("+history[i]+");";
	}
	record = record.substr(0, record.length - 1);
    var data =record;
    console.log(record);
    var name = 'record.txt';//文件名
    this.exportRaw(data, name);
 }

function exportRaw (data, name) {
    var urlObject = window.URL || window.webkitURL || window;
    var export_blob = new Blob([data.toString()]);
    var save_link = document.createElementNS("http://www.w3.org/1999/xhtml", "a")
    save_link.href = urlObject.createObjectURL(export_blob);
    save_link.download = name;
    save_link.click();
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