package com.iispl.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import com.iispl.model.Account;

public interface AccountDAO {
	
	public boolean isAccountExist(Connection con,String accountNumber) throws Exception;
	
	public Account getAccount(Connection con,String accountNumber) throws Exception;
	
	public boolean updateBalance(Connection con, String accountNumber,BigDecimal acccountMoney) throws Exception;

}
