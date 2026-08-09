package com.iispl.validation;

import com.iispl.enums.AccountStatus;
import com.iispl.enums.AccountValidationEnum;
import com.iispl.model.Account;

public class AccountStatusValidation implements ValidationRule {

    @Override
    public AccountValidationEnum validate(Account account) {

        if (account == null) {
            return AccountValidationEnum.ACCOUNT_NOT_FOUND;
        }
        if(account.getAccountStatus()==AccountStatus.ACTIVE) {
        	return AccountValidationEnum.VALID_ACCOUNT;
        }
        return AccountValidationEnum.NOT_ACTIVE;
        
    }
}