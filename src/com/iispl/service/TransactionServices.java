package com.iispl.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.iispl.dao.AccountDAO;
import com.iispl.dao.TransactionDAO;
import com.iispl.daoimpl.AccountDAOImpl;
import com.iispl.daoimpl.TransactionDAOImpl;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.util.DBUtils;

public class TransactionServices {
	
	
	private AccountDAO accountDAO;
	private TransactionDAO transactionDAO;
	private ValidationServices validationservices;
	
	public TransactionServices() {
		accountDAO=new AccountDAOImpl();
		transactionDAO=new TransactionDAOImpl();
		validationservices=new ValidationServices();
		
	}
	public List<TransactionResult> processTransactions(List<TransactionRequest> request){
		List<TransactionResult> results=new ArrayList<>();
		for(TransactionRequest req:request) {
			TransactionResult result=new TransactionResult();
			results.add(result);
		}
		return results;
		
	}

	public TransactionResult processTransaction(TransactionRequest request) {
		List<TransactionResult> result=new ArrayList<>();
		((TransactionRequest) result).setTransactionId(request.getTransactionId());
		
	
		  Connection con=(Connection) DBUtils.getDataSource();
		  
		  
 		  transactionDAO.insertTransaction(con, request, result);
		  
	}
		

	}


