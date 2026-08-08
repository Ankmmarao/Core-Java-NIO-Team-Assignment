package com.iispl.nio;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.iispl.model.TransactionResult;
import com.iispl.util.Constants;

public class ResponseXmlWriter {

    public void writeResponse(List<TransactionResult> results, String inputFileName) throws Exception {

        File outputDir = new File(Constants.OUTPUT_DIR);

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String outputFileName = "response_" + inputFileName;

        File outputFile = new File(outputDir, outputFileName);

        FileWriter writer = new FileWriter(outputFile);

        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<response>\n");

        for (TransactionResult result : results) {

            writer.write("    <transaction>\n");

            writer.write("        <transactionId>" + result.getTransactionId() + "</transactionId>\n");

            writer.write("        <batchId>" + result.getBatchId() + "</batchId>\n");

            writer.write("        <status>" + result.getTransactionStatus() + "</status>\n");

            writer.write("        <code>" + result.getCode() + "</code>\n");

            writer.write("        <reason>" + result.getReason() + "</reason>\n");

            writer.write("    </transaction>\n");
        }

        writer.write("</response>");

        writer.close();

        System.out.println("Creating Response XML...");
        System.out.println(outputFileName + " created.");
    }

}