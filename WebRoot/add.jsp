<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	if (request.getSession(true).getAttribute("islogin") != null
			&& request.getSession(true).getAttribute("name") != null) {
		if (request.getSession(true).getAttribute("islogin")
				.equals("yes")) {
				
		} else {

			response.sendRedirect("/L4d2_Server_Protector/index.jsp");

		}
	}else{
	response.sendRedirect("/L4d2_Server_Protector/index.jsp");
	}
	
%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>添加服务器</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <form id="login" action="servlet/Addserver" method="post"> 
    服务端路径：<input type="text" id="path" name="path"><br>
 启动参数命令行:<input type="text" id="commandline" name="commandline"><br>
 <input type="submit" value="添加">
 </form>
 
 
  </body>
</html>
