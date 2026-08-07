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
	public List<TransactionRequest> xmlReader() throws Exception {
		FileIntakeService fileIntakeService = new FileIntakeService();
		TransactionRequest transactionRequest;
		
		
		Path path = fileIntakeService.getNextFiles();
		FileChannel channel=FileChannel.open(path, StandardOpenOption.READ);
		
		
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		InputStream inputStream = Channels.newInputStream(channel);
		Document document = builder.parse(inputStream);
		
		Element bulkTransactionsElement = document.getDocumentElement();
		String batchId =bulkTransactionsElement.getAttribute("batchId");

		String corporateId =bulkTransactionsElement.getAttribute("corporateId");

		String createdDate =bulkTransactionsElement.getAttribute("createdDate");
		
		NodeList transactions=document.getElementsByTagName("transaction");
		
		List<TransactionRequest> transactionList=new ArrayList<>();
		
		String transactionId;
	    String fromAccount;
	    String toAccount;
	    String transactionType;
	    BigDecimal amount;
	    LocalDate transactionDate;
	    String remarks;

		for(int i=0;i<transactions.getLength();i++) {
			
		    Element transactionElement = (Element) transactions.item(i);

			
			transactionId= transactionElement.getElementsByTagName("transactionId").item(0).getTextContent();
			fromAccount = transactionElement.getElementsByTagName("fromAccount").item(0).getTextContent();
			toAccount= transactionElement.getElementsByTagName("toAccount").item(0).getTextContent();
			transactionType = transactionElement.getElementsByTagName("type").item(0).getTextContent();
			amount = new BigDecimal(transactionElement.getElementsByTagName("amount").item(0).getTextContent());
			transactionDate=LocalDate.parse(transactionElement.getElementsByTagName("transactionDate").item(0).getTextContent());
			remarks=transactionElement.getElementsByTagName("remarks").item(0).getTextContent();
			
			transactionRequest = new TransactionRequest(transactionId,batchId,fromAccount,
					toAccount,transactionType,amount,transactionDate,remarks);
			transactionList.add(transactionRequest);
		}
		
		inputStream.close();
		channel.close();
		return transactionList;
		
	}
}
