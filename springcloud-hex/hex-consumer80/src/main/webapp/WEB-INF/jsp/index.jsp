<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
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
<link rel="stylesheet" type="text/css" href="css/login.css">

<style type="text/css">    
     body{    
        background-image: url(./images/background.jpg);    
        background-size:cover;  
     }    
 </style> 
 
</head>
<body>
	<div class="main">
		<div id="title"><h1 class="title-text">宾馆预订系统</h1></div>
		<form id = "login-form" action="login" method="POST">
			<div class="box-setting-content">
				<div class="inputgroup">
					  <span class="input-group-addon" id="basic-addon1">用户名</span>
					  <input name="username" type="text" class="form-control" style="float:left" aria-describedby="basic-addon1">
				</div>
				<div class="inputgroup">
					  <span class="input-group-addon" id="basic-addon1">密码</span>
					  <input name="password" type="password" class="form-control" style="float:left" aria-describedby="basic-addon1">
				</div>
				<div class="inputgroup">
						<button class="btn btn-primary btn-block">登录</button>
				</div>
				<div class="register">
						<a href="register">立即注册</a>
				</div>
			</div>
		</form>	
	</div>
</body>
</html>