package com.brummer.investmenttracker.transactions.imports;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.transactions.Transaction;

@Service
public class TransactionImporterFidelityATPFile extends TransactionImporterAbstract implements TransactionImporter{

	private AccountRepository accountRepository;
	
	public TransactionImporterFidelityATPFile(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public Transaction parseLine(String line) {

		Transaction transaction = null;
		if(line == null) {
			return null;
		}
		
		// How to handle commas in the data of a csv in Java
		// https://stackoverflow.com/questions/15979688/how-to-handle-commas-in-the-data-of-a-csv-in-java
		String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		
		if(fields.length == 1) {
			return null;
		}
		
		String firstField = fields[0];
		// remove pre and post quotes
		firstField = firstField.substring(1, firstField.length() - 1);
		Date transactionDate = null;
		// see if first field is valid date
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date dAcquired = dateFormat.parse(firstField);
			transactionDate = new Date(dAcquired.getTime());
			// fist field is valid date import record
		} catch (ParseException e) {
			// it isn't a valid date so do nothing and return null 
			return null;
		}
		
		String symbol = fields[1];
		if(symbol == null) {
			return null;
		}
		// remove pre and post quotes
		symbol = symbol.substring(1, symbol.length() - 1);
		if("".equals(symbol)){
			return null;
		}

		transaction = createTransaction(symbol);
		if(transaction == null) {
			return null;
		}
		
		transaction.setTransactionDate(transactionDate);
		
		String accountName = fields[10];
		// remove pre and post quotes
		accountName = accountName.substring(1, accountName.length() - 1);
//		accountName = accountName.replace("(", "");
//		accountName = accountName.replace(")", "");
		List<Account> accounts = accountRepository.findByName(accountName);
		if(accounts.size() == 1) {
			transaction.setAccount(accounts.get(0));
		}
		else {
			return null;
		}
		
		transaction.setSymbol(symbol);
		
		String securityDescription = fields[2];
		securityDescription = securityDescription.substring(1, securityDescription.length() -1);
		transaction.setSecurityDescription(securityDescription);
		
		String quantity = fields[3];
		// remove pre and post quotes
		quantity = quantity.substring(1, quantity.length() - 1);
		transaction.setQuantity(Double.valueOf(quantity.replace(",", "")));
		
		String priceStr = fields[6];
		// remove pre and post quotes,
		priceStr = priceStr.substring(2, priceStr.length() - 1);
		Double price = Double.valueOf(priceStr.replace(",", ""));
		transaction.setAmount(price);
		
		String description = fields[4];
		// remove pre and post quotes,
		description = description.substring(1, description.length() - 1);
		transaction.setAction(description);
		
		return transaction;
		
	}

	
	
}
