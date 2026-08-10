package com.iispl.nio;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import com.iispl.model.TransactionRequest;

public class StaxMethodXMLReadser {

    public List<TransactionRequest> xmlReader(Path path) throws Exception {

        List<TransactionRequest> list = new ArrayList<>();

        XMLStreamReader reader = XMLInputFactory.newInstance()
                .createXMLStreamReader(new FileInputStream(path.toFile()));

        String batchId = null;
        String transactionId = null, 
        		fromAccount = null, 
        		toAccount = null;
        String transactionType = null, remarks = null;
        BigDecimal amount = null;
        LocalDate transactionDate = null;

        while (reader.hasNext()) {

            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {

                String tag = reader.getLocalName();

                if (tag.equals("transactions")) {
                    batchId = reader.getAttributeValue(null, "batchId");
                }

                else if (tag.equals("transactionId"))
                    transactionId = reader.getElementText();

                else if (tag.equals("fromAccount"))
                    fromAccount = reader.getElementText();

                else if (tag.equals("toAccount"))
                    toAccount = reader.getElementText();

                else if (tag.equals("transactionType"))
                    transactionType = reader.getElementText();

                else if (tag.equals("amount"))
                    amount = new BigDecimal(reader.getElementText());

                else if (tag.equals("transactionDate"))
                    transactionDate =
                            LocalDate.parse(reader.getElementText());

                else if (tag.equals("remarks"))
                    remarks = reader.getElementText();
            }

            else if (event == XMLStreamConstants.END_ELEMENT
                    && reader.getLocalName().equals("transaction")) {

                list.add(new TransactionRequest(
                        transactionId, batchId, fromAccount,
                        toAccount, transactionType, amount,
                        transactionDate, remarks
                ));
            }
        }

        reader.close();
        return list;
    }
}