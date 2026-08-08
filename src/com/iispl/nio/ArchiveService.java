package com.iispl.nio;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.iispl.util.Constants;

public class ArchiveService {

    public void archiveFile(Path processingFile) throws Exception {

        Files.createDirectories(Paths.get(Constants.ARCHIVE_DIR));

        Path archiveFile = Paths.get(
                Constants.ARCHIVE_DIR,
                processingFile.getFileName().toString());

        Files.move(
                processingFile,
                archiveFile,
                StandardCopyOption.REPLACE_EXISTING);

        System.out.println("------------------------------------------------------------");
        System.out.println("5. Archive");
        System.out.println("------------------------------------------------------------");
        System.out.println("Moving XML to archive/");
        System.out.println("Archive Successful.");
    }

}