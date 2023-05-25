<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>个人信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="./css/sidebar.css">
	<link rel="stylesheet" type="text/css" href="./css/mydata.css">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
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
	
	<style type="text/css">
		#setting-content{
			float:left;
			width:90%;
			height:100%;
		}
	</style>
	
	<script>
	$(document).ready(function(){
		$("button").click(function(){
			$.post("UpdateUserData",{
				userNo:${user.customerNo},
				userName:$("#name").val(),
				userPwd:$("#userpwd").val(),
				userNumber:$("#userphone").val(),
				userID:$("#userID").val()
			},
			function(data){
				alert(data);
			});
		});
		});
	</script>
  </head>
  
  <body>
  <%@ include file="sidebar.jsp" %>
  <div id="setting-content">
	  	<div id="content-wrapper">
				<div id="box-top">
					<div class="photo-wrapper">
						<img id="photo-image" src="images/panda.png">
						<div class="text">编辑头像</div>
					</div>
				</div>
			
				<div class="box-setting-content">
				
					<div class="inputgroup" >
						  <span class="input-group-addon" id="basic-addon1">昵称</span>
						  <input id = "name" type="text" class="form-control" style="float:left" value="${user.customerName}" aria-describedby="basic-addon1">
					</div>
					<div class="inputgroup">
						  <span class="input-group-addon" id="basic-addon1">密码</span>
						  <input id = "userpwd" type="text" class="form-control" style="float:left" value="${user.pwd}" aria-describedby="basic-addon1">
					</div>
					<div class="inputgroup">
						  <span class="input-group-addon" id="basic-addon1">手机号码</span>
						  <input id = "userphone" type="text" class="form-control" style="float:left" value="${user.customerNumber}" aria-describedby="basic-addon1">
					</div>
					<div class="inputgroup">
						  <span class="input-group-addon" id="basic-addon1">身份证号</span>
						  <input id = "userID" type="text" class="form-control" style="float:left" value="${user.customerId}" aria-describedby="basic-addon1">
					</div>
					
					<div class="inputgroup">
					<button type="submit" class="btn btn-primary btn-block" value="确认修改">确认修改</button>
					</div>
					
				</div>
	  	</div>
  </div>
  </body>
</html>
