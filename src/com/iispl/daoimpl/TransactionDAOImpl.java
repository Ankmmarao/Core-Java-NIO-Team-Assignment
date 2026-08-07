package com.iispl.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.iispl.dao.TransactionDAO;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public class TransactionDAOImpl implements TransactionDAO {

	
	public static final String INSERT_TRANSACTION ="INSERT INTO transaction " +"(transaction_id, batch_id, from_account, to_account, " +
	        "transaction_type, amount, transaction_date, " +
	        "transaction_status, failure_reason, source_file) " +
	        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	@Override
	public void insertTransaction(Connection con, TransactionRequest transaction, List<TransactionResult> result)
			throws Exception {
		
		PreparedStatement ps = con.prepareStatement(INSERT_TRANSACTION);

		ps.setString(1, transaction.getTransactionId());
		ps.setString(2, transaction.getBatchId());
		ps.setString(3, transaction.getFromAccount());
		ps.setString(4, transaction.getToAccount());
		ps.setString(5, transaction.getTransactionType());
		ps.setBigDecimal(6, transaction.getAmount());
		ps.setObject(7, transaction.getTransactionDate());
		ps.setString(8, transaction.getTransactionStatus());
		ps.setString(9, transaction.getFailureReason());
		ps.setString(10, transaction.getSourceFile());

		int a=ps.executeUpdate();
		if(a>0) {
			System.out.println("Transaction Inserted Successfully");
		}
		
	}
	

}
