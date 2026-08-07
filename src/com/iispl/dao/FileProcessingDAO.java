package com.iispl.dao;

import java.sql.Connection;

import com.iispl.model.FileProcessingSummary;

public interface FileProcessingDAO {
	void processFile(Connection con,FileProcessingSummary summary) throws Exception;
	

}
