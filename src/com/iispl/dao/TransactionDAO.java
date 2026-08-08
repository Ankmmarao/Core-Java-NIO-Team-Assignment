package com.iispl.dao;

import java.sql.Connection;

import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public interface TransactionDAO {

    void insertTransaction(Connection con,
                           TransactionRequest transaction) throws Exception;

    void insertTransactionResult(Connection con,
                                 TransactionResult result) throws Exception;
}