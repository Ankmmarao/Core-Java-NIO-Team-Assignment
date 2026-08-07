package com.iispl.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.iispl.util.Constants;

public class FileIntakeService {
     public FileIntakeService() throws IOException{
    	 Files.createDirectories(Paths.get(Constants.INCOMING_DIR));
    	 Files.createDirectories(Paths.get(Constants.PROCESSING_DIR));
    	 Files.createDirectories(Paths.get(Constants.ARCHIVE_DIR));
    	 Files.createDirectories(Paths.get(Constants.OUTPUT_DIR));
    	 Files.createDirectories(Paths.get(Constants.REJECTED_DIR));
     }
    	 public Path getNextFiles() throws Exception {
    		 Path incomingpath=Paths.get(Constants.INCOMING_DIR);
    		 try(DirectoryStream<Path> stream=Files.newDirectoryStream(incomingpath,".xml")){
    			 for(Path path:stream) {
    				 validateFile(path);
    				 Path processingFile=Paths.get(Constants.PROCESSING_DIR,path.getFileName().toString());
    				 Files.move(path, processingFile, StandardCopyOption.REPLACE_EXISTING);
    				 
    				return  processingFile;
    			 }
    		 }
    		 catch(Exception e) {
    			System.out.println(e.getMessage());
    		 }
    		 return null;
    	 }
		 private void validateFile(Path path) {
			// TODO Auto-generated method stub
			//FileAttribute attr=Files
		 }
     }

