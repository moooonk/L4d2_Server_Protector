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
    
    <title>设置服务器自动重启时间</title>
    
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
    <%
    String[] ports = request.getParameter("ports").split(":");
    for(String str :ports){
    	System.out.println(str);
    }
    %>
    <form id="login" action="servlet/Setautorestart" method="post"> 
    你要为以下服务器设置自动重启时间(:分隔){<%=request.getParameter("ports")%>}<br>
	每天几点：<input type="text" id="hour" name="hour"><br>
	每天几分：<input type="text" id="min" name="min"><br>
	<INPUT type=hidden name=ports value=<%=request.getParameter("ports")%> >
 	<input type="submit" value="添加" />
 	</form>
 	
  </body>
</html>
