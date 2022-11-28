package com.brummer.investmenttracker.transactions.imports;

import com.brummer.investmenttracker.constants.EquityType;
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

		transaction.setSymbol(symbol);
		if(startsWithLetterAndHasNumber) {
			// is an option
			transaction.setEquityType(EquityType.OPTION.getValue());
		}
		else {
			transaction.setEquityType(EquityType.STOCK.getValue());
		}
		return transaction;
	}
	
}
