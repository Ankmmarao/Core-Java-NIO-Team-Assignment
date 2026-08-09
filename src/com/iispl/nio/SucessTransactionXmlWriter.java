package com.iispl.nio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.iispl.enums.TransactionStatus;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public class SucessTransactionXmlWriter {

    public void write(
            List<TransactionRequest> requests,
            List<TransactionResult> results,
            String filename) throws IOException {

        Path file =
                Paths.get(filename);

        Files.createDirectories(
                file.getParent());

        try (BufferedWriter writer =
                Files.newBufferedWriter(
                        file,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

            writer.newLine();

            writer.write("<transactions>");
            writer.newLine();

            for (TransactionResult result : results) {

                if (result.getTransactionStatus()
                        != TransactionStatus.SUCCESS) {

                    continue;
                }

                for (TransactionRequest request : requests) {

                    if (request.getTransactionId()
                            .equals(result.getTransactionId())) {

                        writer.write(
                                "    <transaction>");

                        writer.newLine();

                        writer.write(
                                "        <transactionId>"
                                + request.getTransactionId()
                                + "</transactionId>");

                        writer.newLine();

                        writer.write(
                                "        <batchId>"
                                + request.getBatchId()
                                + "</batchId>");

                        writer.newLine();

                        writer.write(
                                "        <fromAccount>"
                                + request.getFromAccount()
                                + "</fromAccount>");

                        writer.newLine();

                        writer.write(
                                "        <toAccount>"
                                + request.getToAccount()
                                + "</toAccount>");

                        writer.newLine();

                        writer.write(
                                "        <transactionType>"
                                + request.getTransactionType()
                                + "</transactionType>");

                        writer.newLine();

                        writer.write(
                                "        <amount>"
                                + request.getAmount()
                                + "</amount>");

                        writer.newLine();

                        writer.write(
                                "        <transactionDate>"
                                + request.getTransactionDate()
                                + "</transactionDate>");

                        writer.newLine();

                        writer.write(
                                "        <remarks>"
                                + request.getRemarks()
                                + "</remarks>");

                        writer.newLine();

                        writer.write(
                                "    </transaction>");

                        writer.newLine();

                        break;
                    }
                }
            }

            writer.write("</transactions>");
            writer.newLine();
        }
    }
}