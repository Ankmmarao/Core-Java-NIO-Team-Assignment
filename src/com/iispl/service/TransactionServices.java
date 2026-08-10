package com.iispl.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.iispl.dao.AccountDAO;
import com.iispl.daoimpl.AccountDAOImpl;
import com.iispl.enums.AccountValidationEnum;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionValidationEnum;
import com.iispl.model.Account;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.nio.RejectTransactionXmlWriter;
import com.iispl.nio.SucessTransactionXmlWriter;
import com.iispl.util.DBUtils;
import com.iispl.validation.AccountBalanceValidation;
import com.iispl.validation.AccountStatusValidation;
import com.iispl.validation.AccountTypeValidation;
import com.iispl.validation.AccountValidationRule;
import com.iispl.validation.FromAccountValidation;
import com.iispl.validation.TransactionAmountValidation;
import com.iispl.validation.TransactionIdValidation;
import com.iispl.validation.TransactionValidationRule;

public class TransactionServices {

    private final AccountDAO accountDAO =
            new AccountDAOImpl();

    private final List<Account> accounts;

    private final SucessTransactionXmlWriter successWriter =
            new SucessTransactionXmlWriter();

    private final RejectTransactionXmlWriter rejectWriter =
            new RejectTransactionXmlWriter();

    public TransactionServices(List<Account> accounts) {
        this.accounts = accounts;
    }

    public TransactionResult processData(TransactionRequest tr) throws Exception {


        // Transaction validations
        List<TransactionValidationRule> transactionValidations =
                new ArrayList<>();

        transactionValidations.add(new TransactionIdValidation());
        transactionValidations.add(new TransactionAmountValidation());

        for (TransactionValidationRule rule : transactionValidations) {

            TransactionValidationEnum result = rule.validate(tr);

            if (result != TransactionValidationEnum.VALID_TRANSACTION) {

                return createFailureResult(
                        tr,
                        result.name(),
                        result.name());
            }
        }
    	// Find FromAccount
        Account fromAccount =
                findAccount(tr.getFromAccount());

        if (fromAccount == null) {

            return createFailureResult(
                    tr,
                    tr.getFromAccount(),
                    "From account not found");
        }

        // Find ToAccount
        Account toAccount =
                findAccount(tr.getToAccount());

        if (toAccount == null) {

            return createFailureResult(
                    tr,
                    tr.getToAccount(),
                    "To account not found");
        }

        // AccountValidation rules
        List<AccountValidationRule> validations =
                new ArrayList<>();

        validations.add(new FromAccountValidation());
        validations.add(new AccountStatusValidation());
        validations.add(new AccountTypeValidation());
        validations.add(new AccountBalanceValidation());

        // Validate From Account
        for (AccountValidationRule rule : validations) {

            AccountValidationEnum validationResult =
                    rule.validate(fromAccount);

            if (validationResult
                    != AccountValidationEnum.VALID_ACCOUNT) {

                return createFailureResult(
                        tr,
                        validationResult.name(),
                        validationResult.name());
            }
        }

        // Debit and Credit
        try (Connection con =
                DBUtils.getDataSource()
                        .getConnection()) {

            con.setAutoCommit(false);

            // Debit
            boolean debit =
                    accountDAO.debitAccount(
                            con,
                            tr.getFromAccount(),
                            tr.getAmount());

            if (!debit) {

                con.rollback();

                return createFailureResult(
                        tr,
                        "DEBIT001",
                        "Debit operation failed");
            }

            // Credit
            boolean credit =
                    accountDAO.creditAccount(
                            con,
                            tr.getToAccount(),
                            tr.getAmount());

            if (!credit) {

                con.rollback();

                return createFailureResult(
                        tr,
                        "CREDIT001",
                        "Credit operation failed");
            }

            // Both successful
            con.commit();

            return createSuccessResult(tr);

        } catch (Exception e) {

            return createFailureResult(
                    tr,
                    "SYS001",
                    e.getMessage());
        }
    }

    private Account findAccount(
            String accountNumber) {

        if (accountNumber == null) {
            return null;
        }

        for (Account account : accounts) {

            if (accountNumber.equals(
                    account.getAccountNumber())) {

                return account;
            }
        }

        return null;
    }
    
    private TransactionResult createFailureResult(
            TransactionRequest tr,
            String code,
            String reason) {

        TransactionResult result =
                new TransactionResult();

        result.setTransactionId(
                tr.getTransactionId());

        result.setBatchId(
                tr.getBatchId());

        result.setTransactionStatus(
                TransactionStatus.FAILURE);

        result.setCode(code);

        result.setReason(reason);

        return result;
    }

    private TransactionResult createSuccessResult(
            TransactionRequest tr) {

        TransactionResult result =
                new TransactionResult();

        result.setTransactionId(
                tr.getTransactionId());

        result.setBatchId(
                tr.getBatchId());

        result.setTransactionStatus(
                TransactionStatus.SUCCESS);

        result.setCode("SUCCESS");

        result.setReason(
                "All validations passed");

        return result;
    }
}