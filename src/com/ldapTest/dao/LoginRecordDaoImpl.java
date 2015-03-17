package com.ldapTest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.ldapTest.bean.LoginInfo;

public class LoginRecordDaoImpl implements LoginRecordDao {

	private DataSource dataSource;
	
	public LoginRecordDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(LoginInfo loginInfo) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ifNull = null;
		PreparedStatement statement = null;
		try{
			conn = dataSource.getConnection();
			ifNull = conn.prepareStatement(
					"SELECT uid FROM user WHERE uid = ?");
			ifNull.setString(1, loginInfo.getUid());
			ResultSet rs = ifNull.executeQuery();
			
			if(!rs.next()){
				statement = conn.prepareStatement(
						"INSERT INTO user (uid,name,mail,location,department,title,telephone,manager) VALUES(?,?,?,?,?,?,?,?)");
				statement.setString(1, loginInfo.getUid());
				statement.setString(2, loginInfo.getName());
				statement.setString(3, loginInfo.getMail());
				statement.setString(4, loginInfo.getLocation());
				statement.setString(5, loginInfo.getDepartment());
				statement.setString(6, loginInfo.getTitle());
				statement.setString(7, loginInfo.getTelephone());
				statement.setString(8, loginInfo.getManager());
				statement.executeUpdate();
			}
			ifNull.close();
			
		}catch(SQLException ex){
			Logger.getLogger(LoginRecordDaoImpl.class.getName()).log(Level.SEVERE,null,ex);
			throw new RuntimeException(ex);
		}finally{
			try{
				if(statement != null){
					statement.close();
				}
			}catch(SQLException ex){
				Logger.getLogger(LoginRecordDaoImpl.class.getName()).log(Level.SEVERE,null,ex);
				throw new RuntimeException(ex);
			}
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ex){
				Logger.getLogger(LoginRecordDaoImpl.class.getName()).log(Level.SEVERE,null,ex);
				throw new RuntimeException(ex);
			}
		}
	}

}
