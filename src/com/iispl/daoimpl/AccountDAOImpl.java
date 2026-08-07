package com.iispl.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.iispl.dao.AccountDAO;

public class AccountDAOImpl implements AccountDAO {

	
	public static final String CHECK_ACCOUNT ="select accountnumber from Account where Acchount=?";	
	@Override
	public boolean isAccountExist(Connection con, String accountNumber) throws Exception {

	    try (PreparedStatement stmt = con.prepareStatement(CHECK_ACCOUNT)) {
	    	   stmt.setString(1, accountNumber);

	         ResultSet rs = stmt.executeQuery();

	        return rs.next(); 

	    } 
	    catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	    return false;
	    
	}
}
