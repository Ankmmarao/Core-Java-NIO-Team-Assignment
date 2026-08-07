package com.iispl.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import com.iispl.model.Account;

public interface AccountDAO {
	boolean debitAccount(Connection con, String accountNumber,
            BigDecimal amount) throws Exception;

boolean creditAccount(Connection con, String accountNumber,
             BigDecimal amount) throws Exception;
	
	public boolean isAccountExist(Connection con,String accountNumber) throws Exception;
	
	public Account getAccount(Connection con,String accountNumber) throws Exception;
	
	public boolean updateBalance(Connection con, String accountNumber,BigDecimal acccountMoney) throws Exception;

}
