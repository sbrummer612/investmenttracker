package com.brummer.investmenttracker.transactions.imports;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.brummer.investmenttracker.transactions.Transaction;

@Service
public class TransactionImportFidelityWebSingleAccount extends TransactionImporterAbstract implements TransactionImporter{

//	@Autowired
//	private AccountRepository accountRepository;
	
	
	@Override
	public Transaction parseLine(String line) {
		
		Transaction transaction = null;
		if(line == null || 
				line.indexOf("Run Date,Action,Symbol,Security Description,Security Type,Quantity,Price ($),Commission ($),Fees ($),Accrued Interest ($),Amount ($),Settlement Date") > -1) {
			return null;
		}
		
		String[] fields = line.split(",");
		if(fields.length == 1) {
			return null;
		}
		
		String firstField = fields[0];
		// remove pre and post quotes
		firstField = firstField.trim();
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
		
		String symbol = fields[2];
		symbol = symbol.trim();
		if(symbol.indexOf("-") == -1) {
			return null;
		}
		
		transaction = createTransaction(symbol.substring(1));
		if(transaction == null) {
			return null;
		}
		
		transaction.setTransactionDate(transactionDate);
		
		String action = fields[1];
		transaction.setAction(action.trim());
		
		String securityDescription = fields[3];
		transaction.setSecurityDescription(securityDescription.trim());
		
		String quantity = fields[5];
		// remove pre and post quotes
		transaction.setQuantity(Double.valueOf(quantity.trim()));
		
		String priceStr = "0.00";
		if(fields.length > 9) {
			priceStr = fields[10];
		}
		Double price = Double.valueOf(priceStr.trim());
		transaction.setAmount(price);
		
		return transaction;
		
	}

}
