package com.ldapTest.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import com.ldapTest.dao.LoginRecordDaoImpl;
import com.ldapTest.service.LoginService;


public class ldapTestInitializer implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		DataSource dataSource = null;
		try{
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/UserInfo");			
		}catch(NamingException ex){
			Logger.getLogger(LoginRecordDaoImpl.class.getName()).log(Level.SEVERE,null,ex);
			throw new RuntimeException(ex);			
		}
		LoginService loginService = new LoginService(new LoginRecordDaoImpl(dataSource));
		sce.getServletContext().setAttribute("loginService",loginService);
	}

}
