<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ page import="java.io.*,
	java.util.*,
	java.util.List,
	java.util.ArrayList,
	org.apache.ibatis.io.*,
	org.apache.ibatis.session.*,com.lwm.springcloud.entities.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'reserve.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="css/sidebar.css">
	<link rel="stylesheet" type="text/css" href="css/reserve.css">
	<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
	
	<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">

	<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
	integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
	crossorigin="anonymous"></script>

	<script>
		$(function(){
				$("#mydata-bar").removeClass("active");
				$("#reserve-bar").addClass("active");
		});

	</script>

	<style type="text/css">
		#hotel-details{
			float:left;
			width:90%;
			height:100%;
			background-color:#d9edf7;
		}
	</style>
  </head>
  
  <body>
  <%@ include file="sidebar.jsp" %>
    <div id="hotel-details">
    	<div id = "reserve-box">
	    	<h1 class="hotel-name">${hotel.hotelName} </h1>
	    	
	    	<div class="list-group" style="margin-bottom: 0;">
			  <a href="javascript:;" class="list-group-item list-group-item-success">地址：${hotel.hotelAddress}</a>
			  <a href="javascript:;" class="list-group-item list-group-item-info">星级：${hotel.hotelLevel}</a>
			  <a href="javascript:;" class="list-group-item list-group-item-warning">单人间：￥${sin_price}/晚 <span class="emptynum">空余量：${sin_empty}</span></a>
			  <a href="javascript:;" class="list-group-item list-group-item-danger">双人间：￥${dou_price}/晚 <span class="emptynum">空余量：${dou_empty}</span></a>
			  <a href="javascript:;" class="list-group-item list-group-item-info">套房：￥${suit_price}/晚 <span class="emptynum">空余量：${suit_empty}</span></a>
			</div>
	    	
	   		<div class="choose">
		   		<p style="margin-bottom:1px">请选择预定房间类型：</p>
		   		<input type="radio" value="single" name="roomtype" >单人间
		   		<input type="radio" value="double" name="roomtype" >双人间
		   		<input type="radio" value="suit" name="roomtype" >套房
		   		<div class = reserve-box>
	     		<button type="submit" value="预定" onclick="reserve();" id = "reserve-btn" class="btn btn-default">立即订房</button>
	     		</div>
	    	</div>
    	</div>
    </div>
    
    
    <script>
		
		function reserve(){
			var selected = $("input[name='roomtype']:checked").val();
			console.info(selected);
 			var notempty = 1;
			switch (selected) {
			  case "single":
			    if('${sin_empty}'=='0')
					{notempty =0;
					alert("单人间已无空余!");}
			    break;
			  case "double":
			     if('${dou_empty}'=='0')
					{notempty =0;
					alert("双人间已无空余!");}
			    break;
			  case "suit":
			    if('${suit_empty}'=='0')
					{notempty =0;
					alert("套房已无空余!");}
			    break;
			} 
		
			if(notempty==1){
				var userName = '${user.customerName}';
				var userNumber = '${user.customerNumber}';
				var userID = '${user.customerId}';
				var hotelNo = '${hotelNo}';
				if(confirm("请确认您的个人信息正确？\n姓名："+ userName +"\n手机号："+ userNumber +"\n身份证号："+ userID )==true)
					{
					window.location.href = "Reserve?roomtype="+selected+"&hotelNo="+hotelNo;
					}
				else
					{
					alert("请到个人信息页进行修改!");
					window.location.href = "mydata";
					}
				
				}
			}
	</script>
  </body>
</html>
