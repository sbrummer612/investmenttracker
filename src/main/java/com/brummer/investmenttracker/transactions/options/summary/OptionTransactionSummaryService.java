package com.brummer.investmenttracker.transactions.options.summary;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.brummer.investmenttracker.constants.TransactionStatusType;
import com.brummer.investmenttracker.transactions.Transaction;

@Service
public class OptionTransactionSummaryService {
	
	public List<String> getStockSymbols(List<OptionTransactionSummary> transactions){
		return transactions.stream().map(ots -> ots.getStockSymbol()).distinct().sorted().collect(Collectors.toCollection(ArrayList<String>::new));// .toList();
	}
	
	public List<OptionTransactionSummary> summarizeTransactions(List<Transaction> transactions, TransactionStatusType transactionStatusType, String stockSymbol) throws ParseException {
		
		Map<String, OptionTransactionSummary> map = new HashMap<String, OptionTransactionSummary>();
		
		for(Transaction transaction : transactions) {
			if(!map.containsKey(transaction.getSymbol())) {
				OptionTransactionSummary ts = getOptionTransactionSummary(transaction);
				map.put(ts.getOptionSymbol(), ts);
			}
			else {
				updateOptionTransactionSummary(map.get(transaction.getSymbol()), transaction);
			}
		}
		 
		ArrayList<OptionTransactionSummary> t = new ArrayList<OptionTransactionSummary>(map.values());
		
		if(transactionStatusType != null) {
			if(transactionStatusType.getValue() == TransactionStatusType.CLOSED.getValue()) {
				if(stockSymbol == null || "".equals(stockSymbol)) {
					t = t.stream().filter(ots -> ots.getQuantity() == 0).collect(Collectors.toCollection(ArrayList<OptionTransactionSummary>::new) );
				}
				else {
					t = t.stream().filter(ots -> ots.getQuantity() == 0).filter(ots -> ots.getStockSymbol().equals(stockSymbol)).collect(Collectors.toCollection(ArrayList<OptionTransactionSummary>::new) );
				}
			}
			else if(transactionStatusType.getValue() == TransactionStatusType.OPEN.getValue()) {
				if(stockSymbol == null || "".equals(stockSymbol)) {
					t = t.stream().filter(ots -> ots.getQuantity() < 0).collect(Collectors.toCollection(ArrayList<OptionTransactionSummary>::new) );
				}
				else {
					t = t.stream().filter(ots -> ots.getQuantity() < 0).filter(ots -> ots.getStockSymbol().equals(stockSymbol)).collect(Collectors.toCollection(ArrayList<OptionTransactionSummary>::new) );
				}
			}
			else {
				if(stockSymbol == null || "".equals(stockSymbol)) {
					t = t.stream().collect(Collectors.toCollection(ArrayList<OptionTransactionSummary>::new));
				}
				else {
					t = t.stream().filter(ots -> ots.getStockSymbol().equals(stockSymbol)).collect(Collectors.toCollection(ArrayList<OptionTransactionSummary>::new));
				}

			}				
		}
		
		Collections.sort(t);
		
		return t;
		
	}
	
	private OptionTransactionSummary getOptionTransactionSummary(Transaction transaction) throws ParseException {
	
		return updateOptionTransactionSummary(null, transaction);
	}
	
	private OptionTransactionSummary updateOptionTransactionSummary(OptionTransactionSummary optionTransactionSummary, Transaction transaction) throws ParseException {
		
		DecimalFormat df = new DecimalFormat("####0.00");
		
		if(optionTransactionSummary == null) {
			optionTransactionSummary = new OptionTransactionSummary();	
		}
		
		if(optionTransactionSummary.getStockSymbol() == null || 
				optionTransactionSummary.getOptionSymbol() == null || 
				optionTransactionSummary.getExpirationDate() == null  ||
				optionTransactionSummary.getStrikePrice() == null ||
				optionTransactionSummary.getAccountName() == null ||
				optionTransactionSummary.getSecurityDescription() == null) {
			String symbol = transaction.getSymbol();
			StringBuffer stockSymbol = new StringBuffer();
			StringBuffer optionExpDate = new StringBuffer();
			StringBuffer strikePrice = new StringBuffer();
			boolean startsWithLetter = false;
			boolean startsWithLetterAndHasNumber = false;
			boolean foundSymbolAndExpDate = false;
			char[] characters = symbol.toCharArray();
			for(int i = 0 ; i < characters.length ; i++) {
				char character = characters[i];
				if(i == 0 && Character.isAlphabetic(character)) {
					startsWithLetter = true;
					stockSymbol.append(character);
				}
				else if(startsWithLetter && Character.isDigit(character)) {
					startsWithLetterAndHasNumber = true;
					if(!foundSymbolAndExpDate) {
						optionExpDate.append(character);
					}
					else {
						strikePrice.append(character);
					}
				}
				else if(startsWithLetter && !startsWithLetterAndHasNumber && Character.isAlphabetic(character)) {
					stockSymbol.append(character);
				}
				else if(startsWithLetter && startsWithLetterAndHasNumber && Character.isAlphabetic(character)) {
					foundSymbolAndExpDate = true;
				}
				
			}
			
			optionTransactionSummary.setStockSymbol(stockSymbol.toString());
			optionTransactionSummary.setOptionSymbol(transaction.getSymbol());
			if(optionExpDate != null && !"".equals(optionExpDate.toString())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
				java.util.Date expirationDate = sdf.parse(optionExpDate.toString());
				optionTransactionSummary.setExpirationDate( new Date(expirationDate.getTime()) );
			}
			if(strikePrice != null && !"".equals(strikePrice.toString())) {
				optionTransactionSummary.setStrikePrice(Double.valueOf(strikePrice.toString()));
			}
			optionTransactionSummary.setAccountName(transaction.getAccount().getName());
			optionTransactionSummary.setSecurityDescription(transaction.getSecurityDescription());
		}
		
		optionTransactionSummary.setQuantity( (optionTransactionSummary.getQuantity() == null) ? transaction.getQuantity() : optionTransactionSummary.getQuantity() + transaction.getQuantity() );
		
		if(transaction.getAction().toUpperCase().indexOf("YOU SOLD OPENING TRANSACTION") > -1 ||
				transaction.getAction().toUpperCase().indexOf("YOU BOUGHT OPENING TRANSACTION") > -1) {
			optionTransactionSummary.setDateAcquired(transaction.getTransactionDate());
			optionTransactionSummary.setCostBasis((optionTransactionSummary.getCostBasis() == null) ? transaction.getAmount() : optionTransactionSummary.getCostBasis() + transaction.getAmount() );
			optionTransactionSummary.setInitialTotalQuantity( (optionTransactionSummary.getInitialTotalQuantity() == null) ? transaction.getQuantity() : optionTransactionSummary.getInitialTotalQuantity() + transaction.getQuantity() );
		}
		else if(transaction.getAction().toUpperCase().indexOf("YOU BOUGHT CLOSING TRANSACTION") > -1 || 
				transaction.getAction().toUpperCase().indexOf("ASSIGNED") > -1 ||
				transaction.getAction().toUpperCase().indexOf("EXPIRED") > -1 ||
				transaction.getAction().toUpperCase().indexOf("YOU SOLD CLOSING TRANSACTION") > -1) {
			Date dateClosed = optionTransactionSummary.getDateClosed();
			if(dateClosed != null) {
				if(transaction.getTransactionDate().compareTo(optionTransactionSummary.getDateClosed()) > 0  ) {
					dateClosed = optionTransactionSummary.getDateClosed();
				}
			}
			else {
				dateClosed = transaction.getTransactionDate();
			}
			optionTransactionSummary.setDateClosed( dateClosed );
			
			optionTransactionSummary.setProceeds((optionTransactionSummary.getProceeds() == null) ? transaction.getAmount() : optionTransactionSummary.getProceeds() + transaction.getAmount() );
		}
		optionTransactionSummary.setGainLoss( Double.valueOf(df.format( ((optionTransactionSummary.getCostBasis() == null) ? 0.00 : optionTransactionSummary.getCostBasis()) + ((optionTransactionSummary.getProceeds() == null) ? 0.00 : optionTransactionSummary.getProceeds().doubleValue()) )) );
		
		return optionTransactionSummary;
	}
	
}
