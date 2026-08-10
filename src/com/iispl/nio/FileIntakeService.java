package com.iispl.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.iispl.util.Constants;

public class FileIntakeService {

    public FileIntakeService() throws IOException {

        Files.createDirectories(Paths.get(Constants.INCOMING_DIR));
        Files.createDirectories(Paths.get(Constants.PROCESSING_DIR));
        Files.createDirectories(Paths.get(Constants.ARCHIVE_DIR));
        Files.createDirectories(Paths.get(Constants.OUTPUT_DIR));
        Files.createDirectories(Paths.get(Constants.REJECTED_DIR));
    }

    public List<Path> getNextFiles() throws IOException {
        
        List<Path> processingFiles = new ArrayList<>();

        Path incomingPath = Paths.get(Constants.INCOMING_DIR);

        System.out.println("Incoming Folder : " + incomingPath.toAbsolutePath());

        if (!Files.exists(incomingPath)) {
            throw new IOException("Incoming directory does not exist.");
        }

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(incomingPath, "*.xml")) {

            for (Path file : stream) {

                validateFile(file);

                Path processingFile = Paths.get(Constants.PROCESSING_DIR,file.getFileName().toString());

                Files.move(file,processingFile,StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Moved File : " + processingFile.getFileName());

                processingFiles.add(processingFile);
            }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (processingFiles.isEmpty()) {
            System.out.println("No XML files found in Incoming folder.");
        }

        return processingFiles;
    }

    private void validateFile(Path path) throws Exception {
    	// File exists
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found : " + path);
        }
        
        //  XML extension check
        if (!path.toString().toLowerCase().endsWith(".xml")) {
            throw new RuntimeException("Invalid file type : "
                    + path.getFileName());
        }
        
        
	    //  Regular file check
	    if (!Files.isRegularFile(path)) {
	        throw new Exception("Not a valid file.");
	    }

//	    //  File name validation
//	    String fileName = path.getFileName().toString();
//
//	    if (!fileName.matches(Constants.FILE_NAME_REGEX)) {
//	        throw new Exception("Invalid file name : " + fileName);
//	    }

	    // Empty file validation
	    if (Files.size(path) == 0) {
	        throw new Exception("Input file is empty.");
	    }

	    // Read Basic File Attributes
	    BasicFileAttributes attributes =
	            Files.readAttributes(path, BasicFileAttributes.class);

	    System.out.println("========== File Details ==========");

	    System.out.println("File Size      : " + attributes.size() + " bytes");
	    System.out.println("Created Time   : " + attributes.creationTime());
	    System.out.println("Last Modified  : " + attributes.lastModifiedTime());
	    System.out.println("==================================");
	}
    
}