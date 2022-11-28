package com.brummer.investmenttracker.transactions.imports;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionImportFactoryTest {

	@Autowired
	private TransactionImportFactory transactionImportFactory;
	
	@Test
	void testNull() throws IOException {
		
		TransactionImporter importFactory = transactionImportFactory.getImporter(null);
		assertThat(importFactory).isNull();;
		
		
	}
	
	@Test
	void testFidelityATPImport() throws IOException {
		
		File initialFile = new File("src/test/java/com/brummer/investmenttracker/transactions/imports/TransactionImportServiceFidelityATPTest.csv");
	    InputStream targetStream = new FileInputStream(initialFile);
		
		TransactionImporter importFactory = transactionImportFactory.getImporter(targetStream);
		assertThat(importFactory).isNotNull();
		assertThat(importFactory).isInstanceOf(TransactionImporterFidelityATPFile.class);
		
	}
	
	@Test
	void testFidelityWebAllAccountsImport() throws IOException {
		
		File initialFile = new File("src/test/java/com/brummer/investmenttracker/transactions/imports/TransactionImportServiceFidelityWebAllAccounts.csv");
	    InputStream targetStream = new FileInputStream(initialFile);
		
		TransactionImporter importFactory = transactionImportFactory.getImporter(targetStream);
		assertThat(importFactory).isNotNull();
		assertThat(importFactory).isInstanceOf(TransactionImporterFidelityWebAllAccounts.class);
		
	}
	
	@Test
	void testFidelityWebSingleAccountsImport() throws IOException {
		
		File initialFile = new File("src/test/java/com/brummer/investmenttracker/transactions/imports/TransactionImportServiceFidelityWebSingleAccount.csv");
	    InputStream targetStream = new FileInputStream(initialFile);
		
		TransactionImporter importFactory = transactionImportFactory.getImporter(targetStream);
		assertThat(importFactory).isNotNull();
		assertThat(importFactory).isInstanceOf(TransactionImportFidelityWebSingleAccount.class);
		
	}
	
}
