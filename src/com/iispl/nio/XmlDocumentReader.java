package com.iispl.nio;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.iispl.model.TransactionRequest;

public class XmlDocumentReader {

    public List<TransactionRequest> xmlReader(Path path) throws Exception {

        if (path == null) {
            throw new Exception("XML file path is null.");
        }

        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream inputStream = Channels.newInputStream(channel);
        Document document = builder.parse(inputStream);

        Element rootElement = document.getDocumentElement();

        String batchId = rootElement.getAttribute("batchId");
        String corporateId = rootElement.getAttribute("corporateId");
        String createdDate = rootElement.getAttribute("createdDate");

        System.out.println("Batch ID      : " + batchId);
        System.out.println("Corporate ID  : " + corporateId);
        System.out.println("Created Date  : " + createdDate);

        NodeList transactions = document.getElementsByTagName("transaction");

        List<TransactionRequest> transactionList = new ArrayList<>();

        for (int i = 0; i < transactions.getLength(); i++) {

            Element transactionElement = (Element) transactions.item(i);

            String transactionId = transactionElement
                    .getElementsByTagName("transactionId")
                    .item(0)
                    .getTextContent();

            String fromAccount = transactionElement
                    .getElementsByTagName("fromAccount")
                    .item(0)
                    .getTextContent();

            String toAccount = transactionElement
                    .getElementsByTagName("toAccount")
                    .item(0)
                    .getTextContent();

            String transactionType = transactionElement
                    .getElementsByTagName("transactionType")  
                    .item(0)
                    .getTextContent();

            BigDecimal amount = new BigDecimal(
                    transactionElement
                    .getElementsByTagName("amount")
                    .item(0)
                    .getTextContent());

            LocalDate transactionDate = LocalDate.parse(
                    transactionElement
                    .getElementsByTagName("transactionDate")
                    .item(0)
                    .getTextContent());

            String remarks = transactionElement
                    .getElementsByTagName("remarks")
                    .item(0)
                    .getTextContent();

            TransactionRequest transactionRequest =
                    new TransactionRequest(
                            transactionId,
                            batchId,
                            fromAccount,
                            toAccount,
                            transactionType,
                            amount,
                            transactionDate,
                            remarks);

            transactionList.add(transactionRequest);
        }

        inputStream.close();
        channel.close();

        return transactionList;
    }
}