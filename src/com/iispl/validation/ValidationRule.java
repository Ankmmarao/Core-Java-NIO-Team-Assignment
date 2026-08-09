package com.iispl.validation;

import com.iispl.enums.AccountValidationEnum;
import com.iispl.model.Account;

public interface ValidationRule {
     public AccountValidationEnum validate(Account account);
}
