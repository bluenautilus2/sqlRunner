package org.bluenautilus.ldap.servlet;


import javax.servlet.http.*;
import java.io.*;

public class MyServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		try {
			PrintWriter out = response.getWriter();
			out.println("Hello, this is a servlet!!!");
		} catch (IOException e) {
			System.out.println(e);
		}


	}
}

