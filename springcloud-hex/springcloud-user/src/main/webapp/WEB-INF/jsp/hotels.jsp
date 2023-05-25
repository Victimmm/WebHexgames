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
	<link rel="stylesheet" type="text/css" href="css/hotels.css">
	
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
	
	<script>
		function reserve(hotelNo){
			window.location.href="reserve?hotelNo="+hotelNo;
		}
	</script>
	
	<style type="text/css">
	#hotels-content{
		float:left;
		width:90%;
		height:100%;
	}
</style>

  </head>
  
  <body>
   <%@ include file="sidebar.jsp" %>
  	<div id = hotels-content>
	 	<%	List<Hotel> hotels = (List<Hotel>)session.getAttribute("hotels");
	 		for(Hotel hotel:hotels){ 
	  			request.setAttribute("hotel", hotel);
  		%>
	     <div class="singlehotel">
	     		<div class="hotelinfo">
		    		<p class="hotel-no">${hotel.hotelNo} </p>
		    		<a  href="reserve.jsp?hotelNo=${hotel.hotelNo}" class="hotel-name">${hotel.hotelName} </a>
		    	</div>
		    	<div class="separater"></div>
		    	<div class="hoteldetails">
		    		<div class="hotel-address">地址：${hotel.hotelAddress} </div>
		    		<div class="hotel-level">星级：${hotel.hotelLevel} </div>
		    		<span class="hotel-rooms">单人间余量：<span class="empty-num">${hotel.singleEmpty}</span> </span>
		    		<span class="hotel-rooms">双人间余量：<span class="empty-num">${hotel.doubleEmpty}</span> </span>
		    		<span class="hotel-rooms">套房余量：<span class="empty-num">${hotel.suitEmpty}</span> </span>
	     		</div>
	     		<div class="separater"></div>
	     		<div class = reserve-box>
	     		<button id = "reserve-btn" class="btn btn-default" onclick="reserve(${hotel.hotelNo});">立即订房</button>
	     		</div>
	     </div>
<%} %>
	</div>
  </body>
</html>
