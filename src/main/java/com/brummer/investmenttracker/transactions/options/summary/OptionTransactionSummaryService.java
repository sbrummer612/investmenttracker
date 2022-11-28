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

import org.springframework.stereotype.Service;

import com.brummer.investmenttracker.transactions.Transaction;

@Service
public class OptionTransactionSummaryService {
	
	public List<OptionTransactionSummary> summarizeTransactions(List<Transaction> transactions) throws ParseException {
		
		Map<String, OptionTransactionSummary> map = new HashMap<String, OptionTransactionSummary>();
		
//		DecimalFormat df = new DecimalFormat("####0.00");
		
		for(Transaction transaction : transactions) {
			if(!map.containsKey(transaction.getSymbol())) {
				OptionTransactionSummary ts = getOptionTransactionSummary(transaction);
//				ts.setAccountName(t.getAccount().getName());
//				ts.setSymbol(t.getSymbol());
//				ts.setOptionSymbol(t.getSymbol());
//				ts.setSecurityDescription(t.getSecurityDescription());
//				ts.setQuantity(t.getQuantity());
//				if(t.getQuantity() > 0) {
//					ts.setProceeds( Double.valueOf(df.format(t.getAmount())) ) ;
//				}
//				else {
//					ts.setCostBasis(t.getAmount());
//				}
//				ts.setGainLoss( Double.valueOf(df.format( ((ts.getCostBasis() == null) ? 0.00 : ts.getCostBasis()) + ((ts.getProceeds() == null) ? 0.00 : ts.getProceeds()) )) );
//				
				map.put(ts.getOptionSymbol(), ts);
			}
			else {
				updateOptionTransactionSummary(map.get(transaction.getSymbol()), transaction);
//				OptionTransactionSummary ts = map.get(t.getSymbol());
//				ts.setQuantity(ts.getQuantity() + t.getQuantity());
//				if(t.getQuantity() > 0) {
//					ts.setProceeds( Double.valueOf(df.format( ((ts.getProceeds() == null) ? 0.00 : ts.getProceeds()) + t.getAmount()) ) );
//				}
//				else {
//					ts.setCostBasis( Double.valueOf(df.format( ((ts.getCostBasis() == null ? 0.00 : ts.getCostBasis())) + t.getAmount() )) );
//				}
//				ts.setGainLoss( Double.valueOf(df.format( ((ts.getCostBasis() == null) ? 0.00 : ts.getCostBasis()) + ((ts.getProceeds() == null) ? 0.00 : ts.getProceeds()) )) );
					
			}
		}
		 
		ArrayList<OptionTransactionSummary> t = new ArrayList<OptionTransactionSummary>(map.values());
		Collections.sort(t);
//		Collections.reverse(t);
//		t.sort(Comparator.comparing(OptionTransactionSummary::getDateAcquired).reversed());
//		t.sort(Comparator.comparing(OptionTransactionSummary::getDateAcquired));
//		Collections.sort(t, Comparator.comparing(OptionTransactionSummary::getDateAcquired));
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
		
		if(transaction.getAction().toUpperCase().indexOf("YOU SOLD OPENING TRANSACTION") > -1 ) {
			optionTransactionSummary.setDateAcquired(transaction.getTransactionDate());
			optionTransactionSummary.setCostBasis((optionTransactionSummary.getCostBasis() == null) ? transaction.getAmount() : optionTransactionSummary.getCostBasis() + transaction.getAmount() );
			optionTransactionSummary.setInitialTotalQuantity( (optionTransactionSummary.getInitialTotalQuantity() == null) ? transaction.getQuantity() : optionTransactionSummary.getInitialTotalQuantity() + transaction.getQuantity() );
		}
		else if(transaction.getAction().toUpperCase().indexOf("YOU BOUGHT CLOSING TRANSACTION") > -1 || 
				transaction.getAction().toUpperCase().indexOf("ASSIGNED") > -1 ||
				transaction.getAction().toUpperCase().indexOf("EXPIRED") > -1) {
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
