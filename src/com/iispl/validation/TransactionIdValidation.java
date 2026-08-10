package com.iispl.validation;

import com.iispl.enums.AccountValidationEnum;
import com.iispl.enums.TransactionValidationEnum;
import com.iispl.model.Account;
import com.iispl.model.TransactionRequest;

public class TransactionIdValidation implements TransactionValidationRule {
	
	@Override
	public TransactionValidationEnum validate(TransactionRequest transaction) {
		if(transaction.getTransactionId() == null ||
				transaction.getTransactionId().trim().isEmpty())
		{
			return TransactionValidationEnum.INVALID_TRANSACTION_ID;
		}
		return TransactionValidationEnum.VALID_TRANSACTION;
	}

}
