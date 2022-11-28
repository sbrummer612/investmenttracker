package com.brummer.investmenttracker.transactions.imports;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class TransactionImportFactory {

	private final TransactionImporterFidelityATPFile transactionImporterFidelityATPFile;
	private final TransactionImporterFidelityWebAllAccounts transactionImportFidelityWebAllAccounts;
	
	public TransactionImportFactory(TransactionImporterFidelityATPFile transactionImporterFidelityATPFile, TransactionImporterFidelityWebAllAccounts transactionImportFidelityWebAllAccounts) {
		this.transactionImporterFidelityATPFile = transactionImporterFidelityATPFile;
		this.transactionImportFidelityWebAllAccounts = transactionImportFidelityWebAllAccounts;
		
	}
	
	public TransactionImporter getImporter(InputStream inputStream) throws IOException {
		if(inputStream == null) {
			return null;
		}
		
		TransactionImporter transactionImporter = null;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		
		String line = reader.readLine();
		while(line != null) {
			if(line.indexOf("\"Date\",\"Symbol\",\"Security Description\",\"Quantity\",\"Description\",\"Price\",\"Amount\",\"Commission\",\"Fees\",\"Type\",\"Account\"") > -1) {
				transactionImporter = transactionImporterFidelityATPFile; //ATP importer
			}
			else if(line.indexOf("Run Date,Action,Symbol,Security Description,Security Type,Quantity,Price ($),Commission ($),Fees ($),Accrued Interest ($),Amount ($),Settlement Date") > -1) {
				transactionImporter = new TransactionImportFidelityWebSingleAccount(); //web single account
			}
			else if(line.indexOf("Run Date,Account,Action,Symbol,Security Description,Security Type,Quantity,Price ($),Commission ($),Fees ($),Accrued Interest ($),Amount ($),Settlement Date") > -1) {
				transactionImporter = transactionImportFidelityWebAllAccounts; // web multi account;
			}
			line = reader.readLine();
		}
		reader.close();
		
		return transactionImporter;
	}
	
}
