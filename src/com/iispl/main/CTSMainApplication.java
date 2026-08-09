package com.iispl.main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.iispl.enums.TransactionStatus;
import com.iispl.model.Account;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.nio.FileIntakeService;
import com.iispl.nio.RejectTransactionXmlWriter;
import com.iispl.nio.SucessTransactionXmlWriter;
import com.iispl.nio.XmlDocumentReader;
import com.iispl.service.TransactionServices;
import com.iispl.util.Constants;

public class CTSMainApplication {

    public static void main(String[] args) {

        try {

            System.out.println(
                    "------------------------------------------------------------");

            System.out.println(
                    "CTS BULK TRANSACTION PROCESSING SYSTEM");

            System.out.println(
                    "------------------------------------------------------------");

            FileIntakeService intakeService =
                    new FileIntakeService();

            List<Path> files =
                    intakeService.getNextFiles();

            if (files.isEmpty()) {

                System.out.println(
                        "No XML Files Found.");

                return;
            }

            List<Account> accounts =
                    getAccounts();

            TransactionServices transactionServices =
                    new TransactionServices(accounts);

            SucessTransactionXmlWriter successWriter =
                    new SucessTransactionXmlWriter();

            RejectTransactionXmlWriter rejectWriter =
                    new RejectTransactionXmlWriter();

            // Process each input XML file
            for (Path processingFile : files) {

                System.out.println(
                        "\nProcessing File : "
                        + processingFile.getFileName());

                XmlDocumentReader reader =
                        new XmlDocumentReader();

                List<TransactionRequest> requests =
                        reader.xmlReader(processingFile);

                System.out.println(
                        "Total Transactions : "
                        + requests.size());

                // Store result of every transaction
                List<TransactionResult> results =
                        new ArrayList<>();

                // Process transactions one by one
                for (TransactionRequest request : requests) {

                    TransactionResult result =
                            transactionServices.processData(
                                    request);

                    results.add(result);
                }

                // Create file names
                String originalFileName =
                        processingFile.getFileName().toString();

                String baseName =
                        originalFileName.substring(
                                0,
                                originalFileName.lastIndexOf('.'));

                Path successFile =
                        Paths.get(Constants.OUTPUT_DIR)
                             .resolve(
                                 baseName + "_success.xml");

                Path failureFile =
                        Paths.get(Constants.REJECTED_DIR)
                             .resolve(
                                 baseName + "_failure.xml");

                // Remove old output files
                Files.deleteIfExists(successFile);
                Files.deleteIfExists(failureFile);

                // Write all SUCCESS transactions
                successWriter.write(
                        requests,
                        results,
                        successFile.toString());

                // Write all FAILURE transactions
                rejectWriter.write(
                        requests,
                        results,
                        failureFile.toString());

                System.out.println(
                        "File Processing Completed : "
                        + processingFile.getFileName());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static List<Account> getAccounts() {

        // Replace with your actual account loading logic
        return List.of();
    }
}