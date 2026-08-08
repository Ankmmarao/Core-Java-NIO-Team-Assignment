package com.iispl.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.iispl.dao.AccountDAO;
import com.iispl.dao.TransactionDAO;
import com.iispl.daoimpl.AccountDAOImpl;
import com.iispl.daoimpl.TransactionDAOImpl;
import com.iispl.enums.AccountStatus;
import com.iispl.enums.TransactionStatus;
import com.iispl.model.Account;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.util.DBUtils;

public class TransactionServices {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private ValidationServices validationServices;

    public TransactionServices() {

        accountDAO = new AccountDAOImpl();
        transactionDAO = new TransactionDAOImpl();
        validationServices = new ValidationServices();
    }

    public List<TransactionResult> processTransactions(List<TransactionRequest> requests) {

        List<TransactionResult> results = new ArrayList<>();

        for (TransactionRequest request : requests) {

            try {

                TransactionResult result = processTransaction(request);
                results.add(result);

            } catch (Exception e) {

                TransactionResult result = new TransactionResult(
                        request.getTransactionId(),
                        request.getBatchId(),
                        TransactionStatus.FAILURE,
                        "SYS001",
                        "transactions.xml",
                        e.getMessage());

                results.add(result);
            }
        }

        return results;
    }

    public TransactionResult processTransaction(TransactionRequest request) throws Exception {

        TransactionResult result = new TransactionResult(
                request.getTransactionId(),
                request.getBatchId(),
                null,
                null,
                "transactions.xml",
                null);

        Connection con = null;

        try {

            con = DBUtils.getDataSource().getConnection();
            con.setAutoCommit(false);

            // From Account Validation
            if (!accountDAO.isAccountExist(con, request.getFromAccount())) {

                result.setTransactionStatus(TransactionStatus.FAILURE);
                result.setCode("ACC001");
                result.setReason("From Account Not Found");

                transactionDAO.insertTransactionResult(con, result);

                con.commit();

                return result;
            }

            // To Account Validation
            if (!accountDAO.isAccountExist(con, request.getToAccount())) {

                result.setTransactionStatus(TransactionStatus.FAILURE);
                result.setCode("ACC002");
                result.setReason("To Account Not Found");

                transactionDAO.insertTransactionResult(con, result);

                con.commit();

                return result;
            }

            Account fromAccount =
                    accountDAO.getAccount(con, request.getFromAccount());

            Account toAccount =
                    accountDAO.getAccount(con, request.getToAccount());

            // Account Status Validation
            if (fromAccount.getAccountStatus() != AccountStatus.ACTIVE
                    || toAccount.getAccountStatus() != AccountStatus.ACTIVE) {

                result.setTransactionStatus(TransactionStatus.FAILURE);
                result.setCode("ACC003");
                result.setReason("Account Inactive");

                transactionDAO.insertTransactionResult(con, result);

                con.commit();

                return result;
            }

            // Balance Validation
            if (fromAccount.getAccountmoney().compareTo(request.getAmount()) < 0) {

                result.setTransactionStatus(TransactionStatus.FAILURE);
                result.setCode("BAL001");
                result.setReason("Insufficient Balance");

                transactionDAO.insertTransactionResult(con, result);

                con.commit();

                return result;
            }

            // Debit
            if (!accountDAO.debitAccount(
                    con,
                    request.getFromAccount(),
                    request.getAmount())) {

                throw new Exception("Debit Failed");
            }

            // Credit
            if (!accountDAO.creditAccount(
                    con,
                    request.getToAccount(),
                    request.getAmount())) {

                throw new Exception("Credit Failed");
            }

            // Insert Transaction
            transactionDAO.insertTransaction(con, request);

            // Success Result
            result.setTransactionStatus(TransactionStatus.SUCCESS);
            result.setCode("00");
            result.setReason("Transaction Completed Successfully");

            transactionDAO.insertTransactionResult(con, result);

            con.commit();

        } catch (Exception e) {

            if (con != null) {
                con.rollback();
            }

            result.setTransactionStatus(TransactionStatus.FAILURE);
            result.setCode("SYS001");
            result.setReason(e.getMessage());

            // Save failure result
            if (con != null) {

                try {

                    con.setAutoCommit(true);
                    transactionDAO.insertTransactionResult(con, result);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } finally {

            if (con != null) {
                con.close();
            }
        }

        return result;
    }
}