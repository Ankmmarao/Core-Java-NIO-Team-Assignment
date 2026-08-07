package com.iispl.main;

import java.nio.file.Path;

import com.iispl.nio.FileIntakeService;

public class CTSMainApplication {
	
	public static void main(String[] args) throws Exception {
		FileIntakeService fi=new FileIntakeService();
		Path fileintakepath=fi.getNextFiles();
		
	}

}
