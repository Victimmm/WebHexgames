<%@page import="javax.annotation.Resource"%>
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
	org.apache.ibatis.session.*,com.lwm.springcloud.entities.*,com.lwm.springcloud.service.*,
	com.google.gson.Gson,
	java.text.SimpleDateFormat"
%>

<jsp:declaration>	
   Hotel hotel;
   HotelService hotelService = new HotelService();
   InputStream in = null;

</jsp:declaration>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>我的订单</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="css/sidebar.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	
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
				$("#order-bar").addClass("active");
		});

	</script>
	
	<script>
	$(document).ready(function(){
		$("button").click(function(){
		$.get("CleanOrders",function(data,status){
    			alert(data);
    			location.reload();
  			});
		  });
		});
		
	</script>
	
	<style type="text/css">
		body{
		background-color:#d9edf7;
		}
		#orders-content{
			float:left;
			width:90%;
			height:100%;
		}
	</style>
  </head>
  
  <body>
  <%@ include file="sidebar.jsp" %>
  
  <div id="orders-content">
	  <table id="gradient-style" summary="Meeting Results">
	    <thead>

	    	<tr>
	        	<th scope="col">订单号</th>
	            <th scope="col">用户名</th>
	            <th scope="col">宾馆名</th>
	            <th scope="col">房间号</th>
	            <th scope="col">酒店星级</th>
	            <th scope="col">订单金额</th>
	            <th scope="col">入住时间</th>
	            <th scope="col">退房时间</th>
	            <th scope="col">订单状态</th>
	        </tr>
	    </thead>
	    <tfoot>
	    	<tr>
	        	<td id = "footer" colspan="9"><span style="vertical-align: sub;">当前订单总数为</span> 
	        	<span id="ordernum" style="vertical-align: sub; color:#a94442">${ordnum}</span> 
	        	<span style="vertical-align: sub;">条</span>
	        	<button id="clear-order">清除过期订单</button>
	        	</td>
	        </tr>
	    </tfoot>
	    
	    <tbody>
	    <% List<CusOrder> orders = (List<CusOrder>)session.getAttribute("orders"); 
	    	for(CusOrder order:orders){
	   		    request.setAttribute("order", order);
	    %>
	    	<tr>
	    		<td  class="order-no">${order.cusorderNo} </td>
	    		<td  class="user-name">${user.customerName} </td>
	    		<%
	    		  int hotelNo = order.getHotelNo();
	    		  System.out.println("get hotelNo"+hotelNo+".....");
	    		  List<Hotel> hotels = (List<Hotel>)session.getAttribute("hotels"); 
	    		  Hotel hotel = hotels.get(hotelNo);
	    		  System.out.println("get hotel"+hotel.getHotelName()+".....");	    		  
	    		  request.setAttribute("hotel", hotel);
	    		%>
	    		<td  class="hotel-name">${hotel.hotelName} </td>
	    		<!-- <td  class="hotel-name">北岸公寓 </td>  -->
	    		<td class="room-no">${order.roomNo} </td>
	    		<td class="hotel-level">${hotel.hotelLevel} </td>
	    		<td class="order-price">￥ ${order.cusorderPrice} </td>
	    		<%
	    		  Date checkin_date = order.getCheckinTime(); 
	    		  Date checkout_date = order.getCheckoutTime();
	    		  String out_date = "暂未离店";
	    		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            	  String in_date = sdf.format(checkin_date);
            	  if(checkout_date != null)
            	  out_date = sdf.format(checkout_date);
            	  request.setAttribute("checkin_date", in_date);
	    		  request.setAttribute("checkout_date",out_date);            	  
	    		%>
	    		<td class="checkin-date">${checkin_date} </td>
	    		<td class="checkout-date">${checkout_date} </td>
	    		<%
	    		if(order.getCusorderValue().equals("当前有效")){ 
	    		%>
	    		<td class="order-state">${order.cusorderValue} </td>
	    		<%
	    		} else {
	    		%>
	    		<td class="order-state" style="color:red">${order.cusorderValue} </td>
	    		<%} %>
   		</tr>
	    <%}%>

	    </tbody>
	</table>
  </div>
  </body>
</html>
