package com.iispl.validation;

import com.iispl.enums.TransactionValidationEnum;
import com.iispl.model.Account;
import com.iispl.model.TransactionRequest;

public interface TransactionValidationRule {
     public TransactionValidationEnum validate(TransactionRequest transaction);
}
