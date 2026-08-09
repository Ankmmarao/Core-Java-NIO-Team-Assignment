package com.iispl.nio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.iispl.model.FileProcessingSummary;
import com.iispl.util.Constants;

public class SummaryFileWriter {

    public void write(FileProcessingSummary summary) throws IOException {

        Path outputDir = Path.of(Constants.OUTPUT_DIR);

        Files.createDirectories(outputDir);

        String fileName = Constants.SUMMARY_PREFIX
                + summary.getFileName()
                .replace(Constants.XML_EXTENSION, Constants.TXT_EXTENSION);

        Path summaryFile = outputDir.resolve(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(
                summaryFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("========================================");
            writer.newLine();
            writer.write("       FILE PROCESSING SUMMARY");
            writer.newLine();
            writer.write("========================================");
            writer.newLine();

            writer.write("Batch ID           : " + summary.getBatchId());
            writer.newLine();

            writer.write("File Name          : " + summary.getFileName());
            writer.newLine();

            writer.write("Total Records      : " + summary.getTotalRecords());
            writer.newLine();

            writer.write("Successful Records : " + summary.getSuccessfulRecords());
            writer.newLine();

            writer.write("Failed Records     : " + summary.getFailedRecords());
            writer.newLine();

            writer.write("Processing Status   : " + summary.getProcessingStatus());
            writer.newLine();

            writer.write("Processed At       : " + summary.getProcessedAt());
            writer.newLine();

            writer.write("========================================");
            writer.newLine();
        }

        System.out.println("Summary File Created : "
                + summaryFile);
    }
}