<%@ page language="java" import="java.util.*,protector.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>服务端列表</title>
    
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
    服务端列表<br><br>
    <script  language="JavaScript">
		function setaction1() {
		document.forms[0].action = "/L4d2_Server_Protector/servlet/Action?action=1";
       	document.forms[0].submit();
		}
		function setaction2() {
		document.forms[0].action = "/L4d2_Server_Protector/servlet/Action?action=2";
       	document.forms[0].submit();
		}
		function setaction3() {
		document.forms[0].action = "/L4d2_Server_Protector/servlet/Action?action=3";
       	document.forms[0].submit();
		}
		function setaction4() {
		document.forms[0].action = "/L4d2_Server_Protector/servlet/Action?action=4";
       	document.forms[0].submit();
		}
		function setaction5() {
		document.forms[0].action = "/L4d2_Server_Protector/servlet/Action?action=5";
       	document.forms[0].submit();
		}
		function setaction6() {
		document.forms[0].action = "/L4d2_Server_Protector/servlet/Action?action=6";
       	document.forms[0].submit();
		}
		function setaction7() {
		document.forms[0].action = "/L4d2_Server_Protector/servlet/Action?action=7";
	    document.forms[0].submit();
		}
		</script>
    <jsp:include page="/servlet/Showserverstatus"/>
  
  </body>
</html>
