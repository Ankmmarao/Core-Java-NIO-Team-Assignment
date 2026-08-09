package com.iispl.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        }

        if (processingFiles.isEmpty()) {
            System.out.println("No XML files found in Incoming folder.");
        }

        return processingFiles;
    }

    private void validateFile(Path path) {

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found : " + path);
        }

        if (!path.toString().toLowerCase().endsWith(".xml")) {
            throw new RuntimeException("Invalid file type : "
                    + path.getFileName());
        }
    }
}