package com.iispl.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.iispl.dao.FileProcessingDAO;
import com.iispl.model.FileProcessingSummary;

public class FileProcessingDAOImpl implements FileProcessingDAO {
	
	public static final String INSERT_FILE_PROCESSING ="INSERT INTO file_processing " +"(batch_id, file_name, total_records, successful_records, failed_records, processing_status) " +
		    "VALUES (?, ?, ?, ?, ?, ?)";	
	@Override
	public void processFile(Connection con, FileProcessingSummary summary) throws Exception {
		try {
			PreparedStatement ps=con.prepareStatement(INSERT_FILE_PROCESSING);
			ps.setString(1, summary.getBatchId());
			ps.setString(2,summary.getFileName() );
			ps.setInt(3, summary.getTotalRecords());
			ps.setInt(4, summary.getSuccessfulRecords());
			ps.setInt(5, summary.getFailedRecords());
			ps.setLong(6, summary.getSuccessfulRecords());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		
	}

}
