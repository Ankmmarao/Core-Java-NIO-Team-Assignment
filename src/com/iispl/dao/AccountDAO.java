package com.iispl.dao;

import java.sql.Connection;

public interface AccountDAO {
	
	public boolean isAccountExist(Connection con,String accountNumber) throws Exception;
	

}
