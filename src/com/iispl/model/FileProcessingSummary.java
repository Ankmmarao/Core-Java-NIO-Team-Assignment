package com.iispl.model;

import java.sql.Timestamp;

public class FileProcessingSummary {

    private String batchId;
    private String fileName;
    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private String processingStatus;
    private Timestamp processedAt;
    
	public FileProcessingSummary(String batchId, String fileName, int totalRecords, int successfulRecords,
			int failedRecords, String processingStatus, Timestamp processedAt) {
		super();
		this.batchId = batchId;
		this.fileName = fileName;
		this.totalRecords = totalRecords;
		this.successfulRecords = successfulRecords;
		this.failedRecords = failedRecords;
		this.processingStatus = processingStatus;
		this.processedAt = processedAt;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getSuccessfulRecords() {
		return successfulRecords;
	}
	public void setSuccessfulRecords(int successfulRecords) {
		this.successfulRecords = successfulRecords;
	}
	public int getFailedRecords() {
		return failedRecords;
	}
	public void setFailedRecords(int failedRecords) {
		this.failedRecords = failedRecords;
	}
	public String getProcessingStatus() {
		return processingStatus;
	}
	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}
	public Timestamp getProcessedAt() {
		return processedAt;
	}
	public void setProcessedAt(Timestamp processedAt) {
		this.processedAt = processedAt;
	}
	
    
    
}
