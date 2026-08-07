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


    public List<TransactionResult> processTransactions(
            List<TransactionRequest> requests) throws Exception {

        List<TransactionResult> results = new ArrayList<>();

        for (TransactionRequest request : requests) {

            TransactionResult result = processTransaction(request);
            results.add(result);

        }

        return results;
    }



    public TransactionResult processTransaction(
            TransactionRequest request) throws Exception {


        TransactionResult result = new TransactionResult(null, null, null, null, null, null);


        result.setTransactionId(request.getTransactionId());
        result.setBatchId(request.getBatchId());
        result.setReason(result.getSourceFile());
        


        Connection con = null;


        try {

            con = DBUtils.getDataSource().getConnection();
            con.setAutoCommit(false);


            boolean fromAccountExist =
                    accountDAO.isAccountExist(
                            con,
                            request.getFromAccount()
                    );


            boolean toAccountExist =
                    accountDAO.isAccountExist(
                            con,
                            request.getToAccount()
                    );


            if (!fromAccountExist || !toAccountExist) {


                result.setTransactionStatus(
                        TransactionStatus.FAILURE
                );

                result.setCode("ACC001");
                result.setReason("Account not found");


                con.rollback();

                return result;
            }



            Account account =
                    accountDAO.getAccount(
                            con,
                            request.getFromAccount()
                    );



            if (!(account.getAccountStatus()==AccountStatus.ACTIVE)) {


                result.setTransactionStatus(
                        TransactionStatus.FAILURE
                );

                result.setCode("ACC002");
                result.setReason("Account inactive");


                con.rollback();

                return result;
            }



            if (account.getAccountmoney()
                    .compareTo(request.getAmount()) < 0) {


                result.setTransactionStatus(
                        TransactionStatus.FAILURE
                );

                result.setCode("BAL001");
                result.setReason("Insufficient balance");


                con.rollback();

                return result;
            }



            boolean debit =
                    accountDAO.debitAccount(
                            con,
                            request.getFromAccount(),
                            request.getAmount()
                    );


            if (!debit) {

                throw new Exception("Debit failed");

            }



            boolean credit =
                    accountDAO.creditAccount(
                            con,
                            request.getToAccount(),
                            request.getAmount()
                    );


            if (!credit) {

                throw new Exception("Credit failed");

            }



            // Transaction successful

            result.setTransactionStatus(
                    TransactionStatus.SUCCESS
            );

            result.setCode("00");
            result.setReason(
                    "Transaction completed successfully"
            );



            transactionDAO.insertTransaction(
                    con,
                    request,
                    result
            );



            con.commit();


        }
        catch(Exception e) {


            if(con != null) {

                con.rollback();

            }


            result.setTransactionStatus(
                    TransactionStatus.FAILURE
            );

            result.setCode("SYS001");
            result.setReason(e.getMessage());


        }
        finally {


            if(con != null) {

                con.close();

            }

        }


        return result;

    }

}