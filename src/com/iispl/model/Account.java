package com.iispl.model;

import java.math.BigDecimal;

import com.iispl.enums.AccountStatus;
import com.iispl.enums.AccountType;

public class Account {
   private String customerName;
   
   private String accountNumber;
   private AccountType accountType;
   private BigDecimal accountmoney;
   private AccountStatus accountStatus;
   public Account(String customerName, String accountNumber, AccountType accountType, BigDecimal accountmoney,
		AccountStatus accountStatus) {
	super();
	this.customerName = customerName;
	this.accountNumber = accountNumber;
	this.accountType = accountType;
	this.accountmoney = accountmoney;
	this.accountStatus = accountStatus;
   }
   public String getCustomerName() {
	return customerName;
   }
   public void setCustomerName(String customerName) {
	this.customerName = customerName;
   }
   public String getAccountNumber() {
	return accountNumber;
   }
   public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
   }
   public AccountType getAccountType() {
	return accountType;
   }
   public void setAccountType(AccountType accountType) {
	this.accountType = accountType;
   }
   public BigDecimal getAccountmoney() {
	return accountmoney;
   }
   public void setAccountmoney(BigDecimal accountmoney) {
	this.accountmoney = accountmoney;
   }
   public AccountStatus getAccountStatus() {
	return accountStatus;
   }
   public void setAccountStatus(AccountStatus accountStatus) {
	this.accountStatus = accountStatus;
   }
   
   
   
}
