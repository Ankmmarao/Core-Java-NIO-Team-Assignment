package com.iispl.daoimpl;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;

import com.iispl.dao.TransactionDAO;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public class TransactionDAOImpl implements TransactionDAO {

    private static final String INSERT_TRANSACTION =
            "INSERT INTO transactions "
          + "(transaction_id, batch_id, from_account, to_account, "
          + "transaction_type, amount, transaction_date, remarks) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_TRANSACTION_RESULT =
            "INSERT INTO transaction_result "
          + "(transaction_id, batch_id, transaction_status, "
          + "code, source_file, reason) "
          + "VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public void insertTransaction(Connection con,
                                  TransactionRequest transaction) throws Exception {

        try (PreparedStatement ps = con.prepareStatement(INSERT_TRANSACTION)) {

            ps.setString(1, transaction.getTransactionId());
            ps.setString(2, transaction.getBatchId());
            ps.setString(3, transaction.getFromAccount());
            ps.setString(4, transaction.getToAccount());
            ps.setString(5, transaction.getTransactionType());
            ps.setBigDecimal(6, transaction.getAmount());
            ps.setDate(7, Date.valueOf(transaction.getTransactionDate()));
            ps.setString(8, transaction.getRemarks());

            ps.executeUpdate();

            System.out.println("Transaction inserted successfully.");
        }
    }

    @Override
    public void insertTransactionResult(Connection con,
                                        TransactionResult result) throws Exception {

        try (PreparedStatement ps = con.prepareStatement(INSERT_TRANSACTION_RESULT)) {

            ps.setString(1, result.getTransactionId());
            ps.setString(2, result.getBatchId());
            ps.setString(3, result.getTransactionStatus().name());
            ps.setString(4, result.getCode());
            ps.setString(5, result.getSourceFile());
            ps.setString(6, result.getReason());

            ps.executeUpdate();

            System.out.println("Transaction Result inserted successfully.");
        }
    }
}