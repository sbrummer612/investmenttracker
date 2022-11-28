package com.brummer.investmenttracker.options;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.constants.OptionType;

@Service
public class OptionImportService {

//	private final OptionRepository optionRepository;
	private final AccountRepository accountRepository;
	
	public OptionImportService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
//		this.optionRepository = optionRepository;
	}
	
	public Option handleLineHistory(String line) throws ParseException {
		
		Option option = null;
		if(line == null) {
			return null;
		}
		
		
		String[] fields = line.split(",");
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
		// remove pre and post quotes
		symbol = symbol.substring(1, symbol.length() - 1);
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
		
		if(startsWithLetterAndHasNumber) {
			// is an option
			option = new Option();
			option.setSymbol(stockSymbol.toString());
			
			String accountName = fields[10];
			// remove pre and post quotes
			accountName = accountName.substring(1, accountName.length() - 1);
			accountName = accountName.replace("(", "");
			accountName = accountName.replace(")", "");
			List<Account> accounts = accountRepository.findByName(accountName);
			if(accounts.size() == 1) {
				option.setAccount(accounts.get(0));
			}
			else {
				return null;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
			java.util.Date expirationDate = sdf.parse(optionExpDate.toString());
			option.setExpirationDate( new Date(expirationDate.getTime()) );
			
			option.setStrikePrice(Double.valueOf(strikePrice.toString()));
			
			option.setOptionSymbol(symbol);
			String securityDescription = fields[2];
			if(securityDescription.indexOf("CALL") > -1) {
				option.setOptionType(OptionType.CALL.getType());
			}
			else {
				option.setOptionType(OptionType.PUT.getType());
			}
			
			String quantity = fields[3];
			// remove pre and post quotes
			quantity = quantity.substring(1, quantity.length() - 1);
			option.setQuantity(Double.valueOf(quantity));
			
			String priceStr = fields[6];
			// remove pre and post quotes,
			priceStr = priceStr.substring(2, priceStr.length() - 1);
			Double price = Double.valueOf(priceStr);
			
			String description = fields[4];
			// remove pre and post quotes,
			description = description.substring(1, description.length() - 1);
			if(description.toUpperCase().indexOf("YOU SOLD OPENING TRANSACTION") > -1 ) {
				option.setDateAcquired(transactionDate);
				option.setCostBasis(price);
			}
			else if(description.toUpperCase().indexOf("YOU BOUGHT CLOSING TRANSACTION") > -1 || 
					description.toUpperCase().indexOf("ASSIGNED") > -1 ||
					description.toUpperCase().indexOf("EXPIRED") > -1) {
				option.setDateSold(transactionDate);
				option.setProceeds(price);
			}
			option.setExtendedDescription(description);
			 
		}
		
		return option;
	}
	
	
	public Option handleLine(String line, Account account) throws ParseException {
		
		Option option = null;
		if(line == null || account == null ) {
			return option;
		}
		
		String[] fields = line.split(",");
		String symbol = "";
		StringBuffer stockSymbol = new StringBuffer();
		StringBuffer optionDate = new StringBuffer();
		StringBuffer optionType = new StringBuffer();
		StringBuffer optionStrike = new StringBuffer();
		boolean isOption = false;
		
		String firstField = fields[0];
		if(firstField.startsWith("Symbol") || firstField.equals("")) {
			// is first line with headers so don't try and import it.
			return option;
		}
		symbol = firstField.substring(0, firstField.indexOf("(") );
		char[] characters = symbol.toCharArray();
		boolean foundNumber = false;
		boolean foundOptionType = false;
		for(char character : characters) {
			if(Character.isDigit(character) || character == '.') {
				// we have a symbol with numbers before the '(' so this must be an option symbol.
				isOption = true;
				foundNumber = true;
				if(foundOptionType) {
					optionStrike.append(character);
				}
				else {
					optionDate.append(character);
				}
				
			}
			else {
				if(!foundNumber) {
					stockSymbol.append(character);
				}
				else {
					optionType.append(character);
					foundOptionType = true;
				}
				
			}
		}
		
		String rawDescription = fields[1];
		StringBuffer description = new StringBuffer();
		if(!rawDescription.equals("-")) {
			//remove pre and post quotes, 
			rawDescription = rawDescription.substring(1, rawDescription.length() - 1);
			description.append(rawDescription);
		}
		
		String quantity = fields[2];
		String dateAcquired = fields[3];
		String dateSold = fields[4];
		
		String rawProceeds = fields[5];
		StringBuffer proceeds = new StringBuffer();
		if(!rawProceeds.equals("-")) {
			//remove pre and post quotes, pre $ and trailing white space
			rawProceeds = rawProceeds.substring(2, rawProceeds.length() - 1).trim();
			proceeds.append(rawProceeds);
		}
		
		String rawCostBasis = fields[6];
		StringBuffer costBasis = new StringBuffer();
		if(!rawCostBasis.equals("-")) {
			//remove pre and post quotes, pre $ and trailing white space
			rawCostBasis = rawCostBasis.substring(2, rawCostBasis.length() - 1).trim();
			costBasis.append(rawCostBasis);
		}
		
		String rawShortTermGain = fields[7];
		StringBuffer shortTermGain = new StringBuffer();
		if(!rawShortTermGain.equals("-")) {
			//remove pre and post quotes, pre $ and trailing white space
			rawShortTermGain = rawShortTermGain.substring(1, rawShortTermGain.length() - 1).trim();
			if(rawShortTermGain.indexOf("(") != -1) {
				// add -
				shortTermGain.append("-");
				// remove pre ( and $ and post )
				shortTermGain.append(rawShortTermGain.substring(2, rawShortTermGain.lastIndexOf(")") )); 
			}
			else {
				shortTermGain.append(rawShortTermGain.substring(1));
			}
		}
		
		String rawLongTermGain = fields[8];
		StringBuffer longTermGain = new StringBuffer();
		if(!rawLongTermGain.equals("-")) {
			//remove pre and post quotes, pre $ and trailing white space
			rawLongTermGain = rawLongTermGain.substring(1, rawLongTermGain.length() - 1).trim();
			if(rawLongTermGain.indexOf("(") != -1) {
				// add -
				longTermGain.append("-");
				// remove pre ( and $ and post )
				longTermGain.append(rawLongTermGain.substring(2, rawLongTermGain.lastIndexOf(")") )); 
			}
			else {
				longTermGain.append(rawLongTermGain.substring(1));
			}
		}
		
		if(isOption) {
			option = new Option();
			option.setAccount(account);
//			System.out.println("symbol = " + symbol);
			
			option.setOptionSymbol(symbol);
//			System.out.println("stockSymbol = "+ stockSymbol.toString());
			
			option.setSymbol(stockSymbol.toString());
//			System.out.println("optionDate = " + optionDate.toString());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
			java.util.Date expirationDate = sdf.parse(optionDate.toString());
			option.setExpirationDate( new Date(expirationDate.getTime()) );
			
//			System.out.println("optionType = " + optionType.toString());
			option.setOptionType(optionType.toString());
			
//			System.out.println("optionStrike = " + optionStrike.toString());
			option.setStrikePrice(Double.valueOf(optionStrike.toString()));
			
//			System.out.println("description = " + description.toString());
			option.setDescription(description.toString());
			
//			System.out.println("quantity = " + quantity);
			option.setQuantity(Double.valueOf(quantity));
			
//			System.out.println("dateAcquired = " + dateAcquired);
			SimpleDateFormat dAcquiredSoldFormat = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date dAcquired = dAcquiredSoldFormat.parse(dateAcquired);
			option.setDateAcquired( new Date(dAcquired.getTime()) );
			
//			System.out.println("dateSold = " + dateSold);
			java.util.Date dSold = dAcquiredSoldFormat.parse(dateSold);
			option.setDateSold( new Date(dSold.getTime()) );
			
//			System.out.println("proceeds = " + proceeds.toString());
			option.setProceeds(Double.valueOf(proceeds.toString()));
			
//			System.out.println("costBasis = " + costBasis.toString());
			option.setCostBasis(Double.valueOf(costBasis.toString()));
			
//			System.out.println("shortTermGain = " + shortTermGain.toString());
			if(!"".equals(shortTermGain.toString())) {
				option.setShortTermGain(Double.valueOf(shortTermGain.toString()));
			}
				
//			System.out.println("longTermGain = " + longTermGain.toString());
			if(!"".equals(longTermGain.toString())) {
				option.setLongTermGain(Double.valueOf(longTermGain.toString()));
			}
			
//			ExampleMatcher matcher = 
//					ExampleMatcher.matching()
//						.withIgnorePaths("id");
//			
//			Example<Option> example = Example.of(option, matcher);
//			
//			if(optionRepository.findAll(example).isEmpty()) {
//				// not found so create it
//				optionRepository.save(option);	
//			}
			// else don't recreate it
			
		}
		return option;
	}
	
}
