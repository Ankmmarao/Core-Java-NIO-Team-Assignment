package com.iispl.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

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
    	 private void validateFile(Path path) throws Exception {

    		    // 1. File exists
    		    if (!Files.exists(path)) {
    		        throw new Exception("File does not exist.");
    		    }

    		    // 2. Regular file check
    		    if (!Files.isRegularFile(path)) {
    		        throw new Exception("Not a valid file.");
    		    }

    		    // 3. XML extension check
    		    if (!path.toString().endsWith(Constants.XML_EXTENSION)) {
    		        throw new Exception("Only XML files are allowed.");
    		    }

    		    // 4. File name validation
    		    String fileName = path.getFileName().toString();

    		    if (!fileName.matches(Constants.FILE_NAME_REGEX)) {
    		        throw new Exception("Invalid file name : " + fileName);
    		    }

    		    // 5. Empty file validation
    		    if (Files.size(path) == 0) {
    		        throw new Exception("Input file is empty.");
    		    }

    		    // 6. Read Basic File Attributes
    		    BasicFileAttributes attributes =
    		            Files.readAttributes(path, BasicFileAttributes.class);

    		    System.out.println("========== File Details ==========");
    		    System.out.println("File Name      : " + fileName);
    		    System.out.println("File Size      : " + attributes.size() + " bytes");
    		    System.out.println("Created Time   : " + attributes.creationTime());
    		    System.out.println("Last Modified  : " + attributes.lastModifiedTime());
    		    System.out.println("==================================");
    		}
     }

