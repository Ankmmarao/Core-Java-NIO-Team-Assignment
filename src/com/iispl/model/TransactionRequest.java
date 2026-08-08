package com.iispl.model;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDate;

public class TransactionRequest {
	 private String transactionId;
	    private String batchId;
	    private String fromAccount;
	    private String toAccount;
	    private String transactionType;
	    private BigDecimal amount;
	    
	    private LocalDate transactionDate;
	    
	    private String remarks;

		public TransactionRequest(String transactionId, String batchId, String fromAccount, String toAccount,
				String transactionType, BigDecimal amount, LocalDate transactionDate, String remarks) {
			super();
			this.transactionId = transactionId;
			this.batchId = batchId;
			this.fromAccount = fromAccount;
			this.toAccount = toAccount;
			this.transactionType = transactionType;
			this.amount = amount;
			this.transactionDate = transactionDate;
			this.remarks = remarks;
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

		public String getFromAccount() {
			return fromAccount;
		}

		public void setFromAccount(String fromAccount) {
			this.fromAccount = fromAccount;
		}

		public String getToAccount() {
			return toAccount;
		}

		public void setToAccount(String toAccount) {
			this.toAccount = toAccount;
		}

		public String getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public LocalDate getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(LocalDate transactionDate) {
			this.transactionDate = transactionDate;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		@Override
		public String toString() {
			return "TransactionRequest [transactionId=" + transactionId + ", batchId=" + batchId + ", fromAccount="
					+ fromAccount + ", toAccount=" + toAccount + ", transactionType=" + transactionType + ", amount="
					+ amount + ", transactionDate=" + transactionDate + ", remarks=" + remarks + "]";
		}
		
		
	    
}
