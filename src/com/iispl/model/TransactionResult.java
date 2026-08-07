package com.iispl.model;

import com.iispl.enums.TransactionStatus;

public class TransactionResult {
	 private String transactionId;
	    private String batchId;
	    private TransactionStatus transactionStatus;   
	    private String Code;
	    private String sourceFile;
	    
	    private String Reason;

		public TransactionResult(String transactionId, String batchId, TransactionStatus transactionStatus, String code,
				String sourceFile, String reason) {
			super();
			this.transactionId = transactionId;
			this.batchId = batchId;
			this.transactionStatus = transactionStatus;
			Code = code;
			this.sourceFile = sourceFile;
			Reason = reason;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getBatchId() {
			return batchId;
		}

		public void setBatchId(String batchId) {
			this.batchId = batchId;
		}

		public TransactionStatus getTransactionStatus() {
			return transactionStatus;
		}

		public void setTransactionStatus(TransactionStatus transactionStatus) {
			this.transactionStatus = transactionStatus;
		}

		public String getCode() {
			return Code;
		}

		public void setCode(String code) {
			Code = code;
		}

		public String getSourceFile() {
			return sourceFile;
		}

		public void setSourceFile(String sourceFile) {
			this.sourceFile = sourceFile;
		}

		public String getReason() {
			return Reason;
		}

		public void setReason(String reason) {
			Reason = reason;
		}
		

}
