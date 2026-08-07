package com.iispl.util;

public final class Constants {

    private Constants() {
        
    }

  
    public static final String INCOMING_DIR = "data/incoming";
    public static final String PROCESSING_DIR = "data/processing";
    public static final String OUTPUT_DIR = "data/output";
    public static final String ARCHIVE_DIR = "data/archive";
    public static final String REJECTED_DIR = "data/rejected";


    public static final String FILE_NAME_REGEX ="TXN_[A-Z0-9]+_\\d{8}_\\d{3}\\.xml";

   
    public static final String RESPONSE_PREFIX = "RESP_";
    public static final String SUMMARY_PREFIX = "SUMMARY_";

  
    public static final String XML_EXTENSION = ".xml";
    public static final String TXT_EXTENSION = ".txt";

    public static final String CHARSET = "UTF-8";
}