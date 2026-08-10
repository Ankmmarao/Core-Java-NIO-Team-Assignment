package com.iispl.validation;

import com.iispl.enums.AccountValidationEnum;
import com.iispl.model.Account;

public class FromAccountValidation implements AccountValidationRule {

    @Override
    public AccountValidationEnum validate(Account account) {

        if (account == null) {
            return AccountValidationEnum.ACCOUNT_NOT_FOUND;
        }

        if (account.getAccountNumber() == null
                || account.getAccountNumber().trim().isEmpty()) {

            return AccountValidationEnum.INVALID_ACCOUNT_NUMBER;
        }

        return AccountValidationEnum.VALID_ACCOUNT;
    }
}