package com.iispl.validation;

import java.math.BigDecimal;

import com.iispl.enums.AccountValidationEnum;
import com.iispl.model.Account;

public class AccountBalanceValidation implements ValidationRule {

    @Override
    public AccountValidationEnum validate(Account account) {

        if (account == null) {
            return AccountValidationEnum.ACCOUNT_NOT_FOUND;
        }

        if (account.getAccountmoney() == null) {
            return AccountValidationEnum.INVALID_ACCOUNT_BALANCE;
        }

        if (account.getAccountmoney()
                .compareTo(BigDecimal.ZERO) < 0) {

            return AccountValidationEnum.INVALID_ACCOUNT_BALANCE;
        }

        return AccountValidationEnum.VALID_ACCOUNT;
    }
}