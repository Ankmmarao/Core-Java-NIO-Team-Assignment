package com.iispl.main;

import java.nio.file.Path;
import java.util.List;

import com.iispl.enums.TransactionStatus;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.nio.ArchiveService;
import com.iispl.nio.FileIntakeService;
import com.iispl.nio.RejectTransactionXmlWriter;
import com.iispl.nio.ResponseXmlWriter;
import com.iispl.nio.XmlDocumentReader;
import com.iispl.service.TransactionServices;

public class CTSMainApplication {

    public static void main(String[] args) {

        try {

            System.out.println("------------------------------------------------------------");
            System.out.println("CTS BULK TRANSACTION PROCESSING SYSTEM");
            System.out.println("------------------------------------------------------------");

            FileIntakeService intakeService = new FileIntakeService();

            List<Path> files = intakeService.getNextFiles();

            if (files.isEmpty()) {
                System.out.println("No XML Files Found.");
                return;
            }

            for (Path processingFile : files) {

                System.out.println("\nProcessing File : "
                        + processingFile.getFileName());

                XmlDocumentReader reader = new XmlDocumentReader();

                List<TransactionRequest> requests =
                        reader.xmlReader(processingFile);

                System.out.println("Records Found : " + requests.size());

                TransactionServices service =
                        new TransactionServices();

                List<TransactionResult> results =
                        service.processTransactions(requests);

                int success = 0;
                int failure = 0;

                for (TransactionResult result : results) {

                    if (result.getTransactionStatus() == TransactionStatus.SUCCESS) {

                        success++;

                        System.out.println(
                                result.getTransactionId()
                                + " : SUCCESS");

                    } else {

                        failure++;

                        System.out.println(
                                result.getTransactionId()
                                + " : FAILED ("
                                + result.getReason()
                                + ")");
                    }
                }

                // Response XML (All Transactions)
                ResponseXmlWriter responseWriter =
                        new ResponseXmlWriter();

                responseWriter.writeResponse(
                        results,
                        processingFile.getFileName().toString());

                // Rejected XML (Only Failed Transactions)
                if (failure > 0) {

                    RejectTransactionXmlWriter rejectWriter =
                            new RejectTransactionXmlWriter();

                    rejectWriter.writeRejectedTransactions(
                            requests,
                            results,
                            processingFile.getFileName().toString());

                    System.out.println(
                            "Rejected XML Created Successfully.");
                }

                // Archive Original XML
                ArchiveService archive =
                        new ArchiveService();

                archive.archiveFile(processingFile);

                System.out.println("Original XML Archived.");

                System.out.println("\n---------------- Summary ----------------");
                System.out.println("Total Records : " + requests.size());
                System.out.println("Success       : " + success);
                System.out.println("Failure       : " + failure);
                System.out.println("-----------------------------------------");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}