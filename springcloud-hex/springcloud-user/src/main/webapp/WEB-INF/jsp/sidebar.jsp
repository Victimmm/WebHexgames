<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

 <div id="sidebar">  
  <ul class="nav nav-pills nav-stacked">
      <img class ="userimg" src="./images/panda.png" >
  	  <li role="presentation" id="userinfo">Welcome,<span id="username">${user.customerName}</span>!</li>
	  <li role="presentation" id = "reserve-bar"><a href="Hotels">酒店预订</a></li>
	  <li role="presentation" id = "order-bar"><a href="MyOrders">我的订单</a></li>
	  <li role="presentation" id = "mydata-bar" class="active"><a href="mydata">个人信息</a></li>
	  <li role="presentation" id = "remark-bar"><a href="#">我的评价</a></li>
	  <li role="presentation" id="exit-bar"><a href="index">退出</a></li>
  </ul>
 </div>

