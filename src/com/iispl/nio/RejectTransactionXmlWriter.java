package com.iispl.nio;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.iispl.enums.TransactionStatus;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.util.Constants;

public class RejectTransactionXmlWriter {

    public void writeRejectedTransactions(
            List<TransactionRequest> requests,
            List<TransactionResult> results,
            String originalFileName) throws Exception {

        File rejectedDir = new File(Constants.REJECTED_DIR);

        if (!rejectedDir.exists()) {
            rejectedDir.mkdirs();
        }

        String rejectedFileName = "rejected_" + originalFileName;

        File rejectedFile = new File(rejectedDir, rejectedFileName);

        FileWriter writer = new FileWriter(rejectedFile);

        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<transactions>\n");

        for (TransactionResult result : results) {

            if (result.getTransactionStatus() == TransactionStatus.FAILURE) {

                for (TransactionRequest request : requests) {

                    if (request.getTransactionId()
                            .equals(result.getTransactionId())) {

                        writer.write("    <transaction>\n");
                        writer.write("        <transactionId>"
                                + request.getTransactionId()
                                + "</transactionId>\n");

                        writer.write("        <batchId>"
                                + request.getBatchId()
                                + "</batchId>\n");

                        writer.write("        <fromAccount>"
                                + request.getFromAccount()
                                + "</fromAccount>\n");

                        writer.write("        <toAccount>"
                                + request.getToAccount()
                                + "</toAccount>\n");

                        writer.write("        <transactionType>"
                                + request.getTransactionType()
                                + "</transactionType>\n");

                        writer.write("        <amount>"
                                + request.getAmount()
                                + "</amount>\n");

                        writer.write("        <transactionDate>"
                                + request.getTransactionDate()
                                + "</transactionDate>\n");

                        writer.write("        <remarks>"
                                + request.getRemarks()
                                + "</remarks>\n");

                        writer.write("    </transaction>\n");

                        break;
                    }
                }
            }
        }

        writer.write("</transactions>");
        writer.close();

        System.out.println("Rejected XML Created : "
                + rejectedFile.getAbsolutePath());
    }
}