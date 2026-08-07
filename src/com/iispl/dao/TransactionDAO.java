package com.iispl.dao;

import java.sql.Connection;
import java.util.List;

import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public interface TransactionDAO {
	
	
	void insertTransaction(Connection con,TransactionRequest request,TransactionResult result) throws Exception;
    
    
	
}
