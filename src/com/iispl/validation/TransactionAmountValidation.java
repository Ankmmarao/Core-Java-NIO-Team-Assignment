package com.iispl.validation;

import java.math.BigDecimal;

import com.iispl.enums.TransactionValidationEnum;
import com.iispl.model.TransactionRequest;

public class TransactionAmountValidation implements TransactionValidationRule {

	@Override
	public TransactionValidationEnum validate(TransactionRequest transaction) {
		if(transaction.getAmount().compareTo(BigDecimal.ZERO)<=0 ||
				transaction.getAmount()==null)
		{
			return TransactionValidationEnum.INVALID_TRANSACTION_AMOUNT;
		}
		return TransactionValidationEnum.VALID_TRANSACTION;
	}
}
