package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import protector.Serverlist;

public class Showserverstatus extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Showserverstatus() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<form id=\"serverlist\" action=\"\" method=\"post\">");
		out.println("<table border=\"1\">");
		out.println("<tr><th>端口号</th><th>服务器名</th><th>路径</th><th>在线状态</th><th>监控状态</th><th>地图</th><th>自动重启时间</th><th>重启次数</th></tr>");
		for (int i = 0; i < Serverlist.serverlist.server_srcds.size(); i++) {
			out.println("<tr>");
			out.println("<td>");
			out.println("<input name=\"serverport\" type=\"checkbox\" value=\""
					+ Serverlist.serverlist.server_srcds.get(i).port + "\"/>"
					+ Serverlist.serverlist.server_srcds.get(i).port);
			out.println("</td>");
			out.println("<td>"
					+ Serverlist.serverlist.server_srcds.get(i).servername
					+ "</td>");
			out.println("<td>"
					+ Serverlist.serverlist.server_srcds.get(i).serverpath
					+ "</td>");
			out.println("<td>"
					+ Serverlist.serverlist.server_srcds.get(i).isonline
					+ "</td>");
			out.println("<td>"
					+ Serverlist.serverlist.server_srcds.get(i).isprotect
					+ "</td>");
			out.println("<td>" + Serverlist.serverlist.server_srcds.get(i).map
					+ "</td>");
			out.println("<td>"
					+ Serverlist.serverlist.server_srcds.get(i).restart_hour
					+ ":"
					+ Serverlist.serverlist.server_srcds.get(i).restart_min
					+ "</td>");
			out.println("<td>"
					+ Serverlist.serverlist.server_srcds.get(i).restartcount
					+ "</td>");
			out.println("<tr>");
		}
		out.println("</table>");
		out.println("<input type=\"submit\" name=\"startprotect\" value=\"启用监控\" onclick=\"setaction1()\"/>");
		out.println("<input type=\"submit\"  name=\"stopprotect\" value=\"停止监控\" onclick=\"setaction2()\"/>");
		out.println("<input type=\"submit\"  name=\"restartserver\" value=\"重启服务器\" onclick=\"setaction3()\"/>");
		out.println("<input type=\"submit\"  name=\"stopserver\" value=\"关闭服务器并停止监控\" onclick=\"setaction4()\"/>");
		out.println("<input type=\"submit\"  name=\"deleteserver\" value=\"移除服务器\" onclick=\"setaction5()\"/>");
		out.println("<input type=\"submit\"  name=\"addserver\" value=\"添加服务器\" onclick=\"setaction6()\"/>");
		out.println("<input type=\"submit\"  name=\"addserver\" value=\"设定自动重启时间\" onclick=\"setaction7()\"/>");
		out.println("</form>");
		out.flush();

	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");

		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
