package com.iispl.validation;

import com.iispl.enums.AccountType;
import com.iispl.enums.AccountValidationEnum;
import com.iispl.model.Account;

public class AccountTypeValidation implements ValidationRule {

    @Override
    public AccountValidationEnum validate(Account account) {

        if (account == null) {
            return AccountValidationEnum.ACCOUNT_NOT_FOUND;
        }

        if (account.getAccountType() == null) {
            return AccountValidationEnum.INVALID_ACCOUNT_TYPE;
        }

        return AccountValidationEnum.VALID_ACCOUNT;
    }
}