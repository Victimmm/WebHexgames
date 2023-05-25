<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>登录失败</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style='background-color:#e3ebe0'>
	<div id = error-box style='text-align: center'>
		<img src='images/error.png' style='margin-top:10%'>
		<p style='font-family: serif'>您输入的账号或密码有误，请重新输入~</p>
	</div>
	<% response.setHeader("refresh","2;URL=index"); %>

</body>
</html>