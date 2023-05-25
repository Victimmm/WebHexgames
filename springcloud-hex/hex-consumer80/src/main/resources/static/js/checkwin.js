//判断输赢的函数
	function checkWinner(){
		checkedFlagReset(flag);
		//红方胜利
		if (checkWinPlayer(1)) {    	

			if(enemy=="")
			{	
		    	var redteam=(team=="1"?user:"11111111");
		    	var blueteam=(team=="1"?"11111111":user);
		    	var record="["+new Date().format("yyyy-MM-dd")+"]";
		    	for(var i=0;i<movenum;i++){
		    		if(i%2==0)
		    			record=record+"R("+history[i]+");";
		    		else 
		    			record=record+"B("+history[i]+");";
		    	}
		    	record = record.substr(0, record.length - 1);
				var msg={
	    				msgtype:"Man-machine battle over",
	    				sender:user,
	    				redteam:redteam	,
	    				blueteam:blueteam ,	
	    				winteam:"red" ,
	    				manual:record
	    		}
	    		websocket.send(JSON.stringify(msg));
				window.alert("对局结束：Red Team wins");
			}
			else {
		    	var redteam=(team=="1"?user:enemy);
		    	var blueteam=(team=="1"?enemy:user);
		    	var record="["+new Date().format("yyyy-MM-dd")+"]";
		    	for(var i=0;i<movenum;i++){
		    		if(i%2==0)
		    			record=record+"R("+history[i]+");";
		    		else 
		    			record=record+"B("+history[i]+");";
		    	}
		    	record = record.substr(0, record.length - 1);
		    	showOnChatroom("对局结束：Red Team wins");
			}
				
        	$("#Chessrecord").css("visibility","visible");
        	$("#Chessrecord").css("pointer-events","visible");
        	//玩家对战胜方发送数据
        	if(team=="1"&&enemy!=""){
				var msg={
	    				msgtype:"battleover",
	    				sender:user,
	    				redteam:redteam	,
	    				blueteam:blueteam ,	
	    				winteam:"red" ,
	    				manual:record
	    		}
	    		websocket.send(JSON.stringify(msg));
        	}      	
        	isrun=false;
        	team=""
        	enemy="";
        	return false;
        }
		//蓝方胜利
		else if (checkWinPlayer(2)) {

        	
        	//System.out.println("对局结束："+"RedTeam wins");
			if(enemy=="")
			{
		    	var redteam=(team=="1"?user:"11111111");
		    	var blueteam=(team=="1"?"11111111":user);
		    	var record="["+new Date().format("yyyy-MM-dd")+"]";
		    	for(var i=0;i<movenum;i++){
		    		if(i%2==0)
		    			record=record+"R("+history[i]+");";
		    		else 
		    			record=record+"B("+history[i]+");";
		    	}
		    	record = record.substr(0, record.length - 1);
				var msg={
	    				msgtype:"Man-machine battle over",
	    				sender:user,
	    				redteam:redteam	,
	    				blueteam:blueteam ,	
	    				winteam:"blue" ,
	    				manual:record
	    		}
	    		websocket.send(JSON.stringify(msg));
				window.alert("对局结束：Blue Team wins");
				
			}
			else 
			{
		    	var redteam=(team=="1"?user:enemy);
		    	var blueteam=(team=="1"?enemy:user);
		    	var record="["+new Date().format("yyyy-MM-dd")+"]";
		    	for(var i=0;i<movenum;i++){
		    		if(i%2==0)
		    			record=record+"R("+history[i]+");";
		    		else 
		    			record=record+"B("+history[i]+");";
		    	}
		    	record = record.substr(0, record.length - 1);
				showOnChatroom("对局结束：Blue Team wins");
			}
        	$("#Chessrecord").css("visibility","visible");
        	$("#Chessrecord").css("pointer-events","visible");
        	if(team=="2"&&enemy!=""){
				var msg={
	    				msgtype:"battleover",
	    				sender:user ,
	    				redteam:redteam	,
	    				blueteam:blueteam ,	
	    				winteam:"blue" ,				
	    				manual:record
	    		}
	    		websocket.send(JSON.stringify(msg));    
        	}
        	isrun=false;
        	enemy="";
        	return false;
        }
        return true;
	}
	
	function checkWinPlayer(team1){
		if(team1==2){
			for (var i = 0; i < 11; i++) {
				if (checkWinTeam(2, 11, i)) {
					return true;
				}
			}
			return false;
		}
		else{
			for (var i = 0; i < 11; i++) {
				if (checkWinTeam( 1, i, 11)) {
					checkedFlagReset();
					return true;
				}
			}
			return false;
		}
	}
	
	function checkedFlagReset(){
		for (var x = 10; x >= 0; x--) {
			for (var y = 10; y >= 0; y--) {
				flag[x][y] = false;
			}
		}
	}
	
	function checkSpot(team1,x,y){
	    if (team1 == 2 && x == 0) {
            return true;
        }
        if (team1 == 1 && y == 0) {
            return true;
        }
        return false;
	}
	
	function checkpiece(team2,x,y){
	   if (team2 == board[x][y] && !flag[x][y]) {
        	flag[x][y]= !flag[x][y];
            if (checkSpot(team2, x, y) || checkWinTeam(team2, x, y)) {
                return true;
            }
        }
        return false;
	}
	
	function checkWinTeam(t,x,y){
		if (y < 11 && x - 1 >= 0
                && checkpiece(t, x - 1, y)) {
            return true;
        }
        if (y < 11 && x + 1 < 11
                && checkpiece(t, x + 1, y)) {
            return true;
        }
        if (x < 11 && y - 1 >= 0
                && checkpiece(t, x, y - 1)) {
            return true;
        }
        if (x < 11 && y + 1 < 11
                && checkpiece(t, x, y + 1)) {
            return true;
        }
        if (y + 1 < 11
                && x - 1 >= 0
                && checkpiece(t, x - 1, y + 1)) {
            return true;
        }
        if (y - 1 < 11
                && x + 1 < 11
                && y - 1 >= 0
                && checkpiece(t, x + 1, y - 1))
        {
            return true;
        }
        return false;
    }