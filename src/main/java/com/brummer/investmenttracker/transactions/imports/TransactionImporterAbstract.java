package com.brummer.investmenttracker.transactions.imports;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brummer.investmenttracker.constants.EquityType;
import com.brummer.investmenttracker.constants.OptionType;
import com.brummer.investmenttracker.transactions.Transaction;

public abstract class TransactionImporterAbstract {

	public Transaction createTransaction(String symbol) {
		
		if(symbol == null) {
			return null;
		}
		
		Transaction transaction = new Transaction();;
		
		StringBuffer stockSymbol = new StringBuffer();
		StringBuffer optionExpDate = new StringBuffer();
		StringBuffer strikePrice = new StringBuffer();
		StringBuffer optionSymbol = new StringBuffer();
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
				optionSymbol.append(character);
			}
		}

		transaction.setSymbol(symbol);
		if(startsWithLetterAndHasNumber && foundSymbolAndExpDate) {
			// is an option
			// ensure the expiration date is valid
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
				java.util.Date expirationDate = sdf.parse(optionExpDate.toString());
				new Date(expirationDate.getTime());
			} catch (ParseException e) {
				// not valid expiration date to don't return object
				return null;
			}
			
			
			transaction.setEquityType(EquityType.OPTION.getValue());
			if(optionSymbol != null) {
				if(OptionType.CALL.getValue().equals(optionSymbol.toString())) {
					transaction.setOptionType(OptionType.CALL.getValue());
				}
				else if(OptionType.PUT.getValue().equals(optionSymbol.toString())) {
					transaction.setOptionType(OptionType.PUT.getValue());
				}
			}
		}
		else {
			if(startsWithLetterAndHasNumber) {
				return null;
			}
			transaction.setEquityType(EquityType.STOCK.getValue());
		}
		return transaction;
	}
	
}
