package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import protector.Serverlist;

public class Action extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Action() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
		String str1 = (String) request.getParameter("action");
		System.out.println(str1);
		String[] serverport = request.getParameterValues("serverport");
		if (serverport == null && !"6".equals(str1)) {
			response.sendRedirect("/L4d2_Server_Protector/serverstatus.jsp");
			return;
		}
		if ("1".equals(str1)) {
			for (int i = 0; i < serverport.length; i++) {
				Serverlist.serverlist.startprotect(serverport[i]);
				// System.out.println(serverport[i]);
			}

		}

		if ("2".equals(str1)) {
			// System.out.println(serverport);
			for (int i = 0; i < serverport.length; i++) {
				Serverlist.serverlist.stopprotect(serverport[i]);
				// System.out.println(serverport[i]);
			}
			System.out.println("Í£Ö¹¼à¿Ø");

		}

		if ("3".equals(str1)) {
			for (int i = 0; i < serverport.length; i++) {
				Serverlist.serverlist.restartserver(serverport[i]);
				// System.out.println(serverport[i]);
			}

		}
		if ("4".equals(str1)) {
			for (int i = 0; i < serverport.length; i++) {
				Serverlist.serverlist.stopserver(serverport[i]);
				// System.out.println(serverport[i]);
			}

		}

		if ("5".equals(str1)) {
			for (int i = 0; i < serverport.length; i++) {
				Serverlist.serverlist.deleteserver(serverport[i]);
				// System.out.println(serverport[i]);
			}

		}

		if ("6".equals(str1)) {
			response.sendRedirect("/L4d2_Server_Protector/add.jsp");
			return;
		}

		if ("7".equals(str1)) {
			String ports = "";
			for (int i = 0; i < serverport.length; i++) {
				ports = ports + serverport[i] + ":";
				// System.out.println(serverport[i]);
			}
			response.sendRedirect("/L4d2_Server_Protector/setautorestart.jsp?ports="
					+ ports);
			return;
		}

		response.sendRedirect("/L4d2_Server_Protector/serverstatus.jsp");

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
