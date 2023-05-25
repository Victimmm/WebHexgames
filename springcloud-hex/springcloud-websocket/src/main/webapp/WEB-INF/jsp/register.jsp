<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">-->
<html>
<head>
<base href="<%=basePath%>">

<title>登录页面</title>

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


<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="css/register.css">

	<script>
	$(document).ready(function(){
		$("button").click(function(){
			if($("#password").val()!=$("#repeat_password").val())
				{
				console.info($("#password").val());
				console.info($("#repeat_password").val());
				alert("两次输入密码相同,请重新输入!");
				$("#account").val("");
				$("#password").val("");
				$("#repeat_password").val("");
				$("#number").val("");
				$("#ID").val("");
				}
				else{
						$.post("consumer/Register",{
						userName:$("#account").val(),
						userPwd:$("#password").val(),
						userNumber:$("#number").val(),
						userID:$("#ID").val()
					},
					function(data){
					alert(data);
					window.location.href="consumer/mydata";
					});
				}
		});
		});
	</script>

<style type="text/css">    
     body{    
        background-image: url(images/background.jpg);    
        background-size:cover;  
     }    
 </style> 
 
</head>

<body>
	<div class="main">
		<div id = "register-form">
				<div class="form-container">
					<h3 class="page-title">注册</h3>
					<div class="form-group">
						用户名：<input id="account" type="text" class="form-control" placeholder="请输入用户名" name="username">
					</div>
					<div class="form-group">
						手机号码：<input id="number" type="text" class="form-control" placeholder="请输入手机号码" name="number">
					</div>
					<div class="form-group">
						身份证号：<input id="ID" type="text" class="form-control" placeholder="请输入身份证号" name="ID">
					</div>
					<div class="form-group">
						密码：<input id="password" type="text" class="form-control" placeholder="请输入密码" name="password">
					</div>
					<div class="form-group">
						确认密码：<input id="repeat_password" type="text" class="form-control" placeholder="请再次输入密码" name="password">
					</div>
					<div class="form-group">
						<button class="btn btn-primary btn-block">立即注册</button>
					</div>
				</div>
		</div>	
	</div>
</body>

</html>
