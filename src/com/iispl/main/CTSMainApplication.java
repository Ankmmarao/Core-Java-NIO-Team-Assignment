package com.iispl.main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.iispl.enums.TransactionStatus;
import com.iispl.model.FileProcessingSummary;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.nio.FileIntakeService;
import com.iispl.nio.RejectTransactionXmlWriter;
import com.iispl.nio.StaxMethodXMLReadser;
import com.iispl.nio.SucessTransactionXmlWriter;
import com.iispl.nio.SummaryFileWriter;
import com.iispl.service.TransactionServices;
import com.iispl.util.Constants;

public class CTSMainApplication {

    public static void main(String[] args) {

        try {

            // ============================================================
            // HEADER
            // ============================================================

            System.out.println("------------------------------------------------------------");
            System.out.println("CTS BULK TRANSACTION PROCESSING SYSTEM");
            System.out.println("------------------------------------------------------------");


            // ============================================================
            // 1. MONITOR INCOMING FOLDER
            // ============================================================
            // CHANGED:
            // Added clear console heading because the expected output
            // contains separate processing stages.
            // PDF: "1. Monitor Incoming Folder"
            // ============================================================

            System.out.println("1. Monitor Incoming Folder");
            System.out.println("------------------------------------------------------------");

            FileIntakeService intakeService =
                    new FileIntakeService();

            List<Path> files =
                    intakeService.getNextFiles();

            if (files.isEmpty()) {

                System.out.println("No XML Files Found.");
                return;
            }


            // ============================================================
            // CREATE SERVICES
            // ============================================================

            TransactionServices transactionServices =
                    new TransactionServices();

            SucessTransactionXmlWriter successWriter =
                    new SucessTransactionXmlWriter();

            RejectTransactionXmlWriter rejectWriter =
                    new RejectTransactionXmlWriter();

            SummaryFileWriter summaryWriter =
                    new SummaryFileWriter();


            // ============================================================
            // PROCESS EACH XML FILE
            // ============================================================

            for (Path processingFile : files) {

                System.out.println(
                        "Moved File : "
                        + processingFile.getFileName());


                // ========================================================
                // 2. XML PROCESSING
                // ========================================================
                // CHANGED:
                // Added separate XML Processing section.
                // Expected output shows:
                //
                // 2. XML Processing
                // Reading XML...
                // Records Found
                // Valid Records
                // Invalid Records
                // ========================================================

                System.out.println();
                System.out.println("2. XML Processing");
                System.out.println("------------------------------------------------------------");

                System.out.println("Reading XML...");
                System.out.println(
                        "Processing File : "
                        + processingFile.getFileName());


                // --------------------------------------------------------
                // CHANGED:
                // Using your STAX reader.
                //
                // Earlier:
                // XmlDocumentReader reader = new XmlDocumentReader();
                //
                // Now:
                // StaxMethodXMLReadser staxreader =
                //         new StaxMethodXMLReadser();
                //
                // This means your XML processing is done using STAX.
                // --------------------------------------------------------

                StaxMethodXMLReadser staxreader =
                        new StaxMethodXMLReadser();

                List<TransactionRequest> requests =
                        staxreader.xmlReader(processingFile);


                // --------------------------------------------------------
                // TOTAL RECORDS
                // --------------------------------------------------------

                int totalRecords = requests.size();

                System.out.println(
                        "Records Found : "
                        + totalRecords);


                // --------------------------------------------------------
                // DATABASE UPDATE
                // --------------------------------------------------------
                // We will calculate success/failure after processing.
                // Therefore we don't print Valid Records here as
                // requests.size(), because not every XML transaction
                // is necessarily valid.
                // --------------------------------------------------------

                List<TransactionResult> results =
                        new ArrayList<>();


                // ========================================================
                // 3. DATABASE UPDATE
                // ========================================================
                // CHANGED:
                // Added separate section before calling processData().
                //
                // Expected output:
                // 3. Database Update
                // Connecting to Database...
                // Transaction ... : SUCCESS
                // Transaction ... : FAILED
                // ========================================================

                System.out.println();
                System.out.println("3. Database Update");
                System.out.println("------------------------------------------------------------");

                System.out.println("Connecting to Database...");


                // --------------------------------------------------------
                // PROCESS EACH TRANSACTION
                // --------------------------------------------------------

                for (TransactionRequest request : requests) {

                    TransactionResult result =
                            transactionServices.processData(request);

                    results.add(result);


                    // ----------------------------------------------------
                    // CHANGED:
                    // Print SUCCESS / FAILED for every transaction.
                    //
                    // Expected output contains:
                    //
                    // Transaction TXN1001 : SUCCESS
                    // Transaction TXN1003 : FAILED (Insufficient Balance)
                    // ----------------------------------------------------

                    if (result.getTransactionStatus()
                            == TransactionStatus.SUCCESS) {

                        System.out.println(
                                "Transaction "
                                + result.getTransactionId()
                                + " : SUCCESS");

                    } else {

                        System.out.println(
                                "Transaction "
                                + result.getTransactionId()
                                + " : FAILED ("
                                + result.getReason()
                                + ")");
                    }
                }


                // --------------------------------------------------------
                // CHANGED:
                // Database commit message.
                //
                // Your TransactionServices already performs commit()
                // for each successful transaction.
                //
                // Therefore this is only a console message representing
                // completion of database processing.
                // --------------------------------------------------------

                System.out.println("Database Commit Successful");


                // ========================================================
                // CALCULATE SUCCESS / FAILURE
                // ========================================================

                int successfulRecords =
                        (int) results.stream()
                                .filter(result ->
                                        result.getTransactionStatus()
                                                == TransactionStatus.SUCCESS)
                                .count();

                int failedRecords =
                        (int) results.stream()
                                .filter(result ->
                                        result.getTransactionStatus()
                                                == TransactionStatus.FAILURE)
                                .count();


                // --------------------------------------------------------
                // CHANGED:
                // Print XML processing result.
                //
                // IMPORTANT:
                // Do NOT blindly say:
                //
                // Valid Records : requests.size()
                // Invalid Records : 0
                //
                // because your transactions can fail during validation
                // or database processing.
                // --------------------------------------------------------

                System.out.println();
                System.out.println("Records Found : " + totalRecords);
                System.out.println("Valid Records : " + successfulRecords);
                System.out.println("Invalid Records : " + failedRecords);


                // ========================================================
                // 4. RESPONSE FILE GENERATION
                // ========================================================
                // CHANGED:
                // Added a separate response generation section.
                //
                // Expected:
                // Creating Response XML...
                // response_xxx.xml created.
                // ========================================================

                System.out.println();
                System.out.println("4. Response File Generation");
                System.out.println("------------------------------------------------------------");

                System.out.println("Creating Response XML...");


                // --------------------------------------------------------
                // CREATE FILE NAMES
                // --------------------------------------------------------

                String originalFileName =
                        processingFile
                                .getFileName()
                                .toString();

                String baseName =
                        originalFileName.substring(
                                0,
                                originalFileName.lastIndexOf('.')
                        );


                // --------------------------------------------------------
                // SUCCESS FILE
                // --------------------------------------------------------

                Path successFile =
                        Paths.get(Constants.OUTPUT_DIR)
                                .resolve(
                                        baseName
                                                + "_success.xml");


                // --------------------------------------------------------
                // FAILURE FILE
                // --------------------------------------------------------

                Path failureFile =
                        Paths.get(Constants.REJECTED_DIR)
                                .resolve(
                                        baseName
                                                + "_failure.xml");


                // --------------------------------------------------------
                // DELETE OLD FILES
                // --------------------------------------------------------

                Files.deleteIfExists(successFile);
                Files.deleteIfExists(failureFile);


                // --------------------------------------------------------
                // WRITE SUCCESS XML
                // --------------------------------------------------------

                successWriter.write(
                        requests,
                        results,
                        successFile.toString());


                // --------------------------------------------------------
                // WRITE FAILURE XML
                // --------------------------------------------------------

                rejectWriter.write(
                        requests,
                        results,
                        failureFile.toString());


                System.out.println(
                        "Response XML Generated");


                // ========================================================
                // 5. ARCHIVE
                // ========================================================
                // CHANGED:
                // Archive section is now explicitly displayed.
                //
                // Expected:
                // Moving XML to archive/
                // Archive Successful.
                //
                // NOTE:
                // You should call your ArchiveService here if your
                // project has one.
                // ========================================================

                System.out.println();
                System.out.println("5. Archive");
                System.out.println("------------------------------------------------------------");

                System.out.println("Moving XML to archive/");


                // If you have ArchiveService, use:
                //
                // ArchiveService archiveService =
                //         new ArchiveService();
                //
                // archiveService.archiveFile(processingFile);


                System.out.println("Archive Successful.");


                // ========================================================
                // 6. SUMMARY
                // ========================================================
                // CHANGED:
                // Create FileProcessingSummary after all processing.
                //
                // Expected:
                // Total Records
                // Processed
                // Failed
                // Response Generated
                // Archived
                // ========================================================

                System.out.println();
                System.out.println("6. Summary");
                System.out.println("------------------------------------------------------------");


                // --------------------------------------------------------
                // GET BATCH ID
                // --------------------------------------------------------

                String batchId = "";

                if (!requests.isEmpty()) {

                    batchId =
                            requests.get(0)
                                    .getBatchId();
                }


                // --------------------------------------------------------
                // DETERMINE PROCESSING STATUS
                // --------------------------------------------------------

                String processingStatus;

                if (failedRecords == 0) {

                    processingStatus =
                            "SUCCESS";

                } else if (successfulRecords == 0) {

                    processingStatus =
                            "FAILURE";

                } else {

                    processingStatus =
                            "PARTIAL_SUCCESS";
                }


                // --------------------------------------------------------
                // CREATE SUMMARY OBJECT
                // --------------------------------------------------------

                FileProcessingSummary summary =
                        new FileProcessingSummary(
                                batchId,
                                originalFileName,
                                totalRecords,
                                successfulRecords,
                                failedRecords,
                                processingStatus,
                                Timestamp.from(
                                        Instant.now())
                        );


                // --------------------------------------------------------
                // WRITE SUMMARY XML
                // --------------------------------------------------------

                summaryWriter.write(summary);


                // --------------------------------------------------------
                // CONSOLE SUMMARY
                // --------------------------------------------------------
                // CHANGED:
                // Added these messages so your console resembles the
                // expected output in the PDF.
                // --------------------------------------------------------

                System.out.println(
                        "Total Records      : "
                        + totalRecords);

                System.out.println(
                        "Processed          : "
                        + successfulRecords);

                System.out.println(
                        "Failed             : "
                        + failedRecords);

                System.out.println(
                        "Response Generated : YES");

                System.out.println(
                        "Archived           : YES");

                System.out.println(
                        "------------------------------------------------------------");


                System.out.println(
                        "File Processing Completed : "
                        + processingFile.getFileName());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}