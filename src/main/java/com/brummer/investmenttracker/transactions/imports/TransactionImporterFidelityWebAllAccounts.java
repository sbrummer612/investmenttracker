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
public class TransactionImporterFidelityWebAllAccounts extends TransactionImporterAbstract implements TransactionImporter{

	private AccountRepository accountRepository;
	
	public TransactionImporterFidelityWebAllAccounts(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public Transaction parseLine(String line) {
		
		Transaction transaction = null;
		if(line == null || 
				line.indexOf("Run Date,Account,Action,Symbol,Security Description,Security Type,Quantity,Price ($),Commission ($),Fees ($),Accrued Interest ($),Amount ($),Settlement Date") > -1) {
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
		
		String symbol = fields[3];
		symbol = symbol.trim();
		
		transaction = createTransaction(symbol.replace("-", ""));
		if(transaction == null) {
			return null;
		}
		
		transaction.setTransactionDate(transactionDate);
		
		String accountName = fields[1];
		List<Account> accounts = accountRepository.findByName(accountName.trim());
		if(accounts.size() == 1) {
			transaction.setAccount(accounts.get(0));
		}
		else {
			return null;
		}
		
		String action = fields[2];
		transaction.setAction(action.trim());
		
		String securityDescription = fields[4];
		transaction.setSecurityDescription(securityDescription.trim());
		
		String quantity = fields[6];
		// remove pre and post quotes
		transaction.setQuantity(Double.valueOf(quantity.trim()));
		
		String priceStr = "0.00";
		if(fields.length > 11) {
			priceStr = fields[11];
		}
		Double price = Double.valueOf(priceStr.trim());
		transaction.setAmount(price);
		
		return transaction;
		
	}

}
