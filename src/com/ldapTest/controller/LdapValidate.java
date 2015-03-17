package com.ldapTest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ldapTest.bean.LoginInfo;
import com.ldapTest.service.*;

/**
 * Servlet implementation class LdapValidate
 */
public class LdapValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LdapValidate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		LoginService loginService = (LoginService)getServletContext().getAttribute("loginService");
		LoginInfo user = loginService.login(username, password);
		if(user != null) {
			System.out.println(user.getMail());
			System.out.println(user.getDepartment());
			System.out.println(user.getTelephone());
			System.out.println(user.getName());
			System.out.println(user.getLocation());
			request.getRequestDispatcher("/success.html").forward(request, response);
						
		}
		else{
			response.sendRedirect("/ldapTest/fail.html");
		}
	}

}
