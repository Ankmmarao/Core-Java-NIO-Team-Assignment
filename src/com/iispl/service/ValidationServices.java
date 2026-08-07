package com.iispl.service;

import com.iispl.model.TransactionRequest;

public class ValidationServices {
     public static void validate(TransactionRequest tequest) throws Exception {
    	 if(tequest.getTransactionId()==null || tequest.getTransactionId().isEmpty()) {
    		 throw new Exception("Transaction Amount Can't be Null");
    		 
    	 }
    	 
     }
}
