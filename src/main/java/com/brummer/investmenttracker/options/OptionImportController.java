package com.brummer.investmenttracker.options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;

@Controller
@RequestMapping("/optionImportController")
public class OptionImportController {

	private final AccountRepository accountRepository;
	private final OptionRepository optionRepository;
	private final String redirect = "redirect:/optionImportController/importOptions";
	
	public OptionImportController(AccountRepository accountRepository, OptionRepository optionRepository) {
		this.accountRepository = accountRepository;
		this.optionRepository = optionRepository;
	}
	
	@RequestMapping("/importOptions")
	public String importOptions(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		populate(model, account, request);
		return "optionImport";
	}

	@PostMapping("/importOptionsFile")
	public String importOptionsFile(@ModelAttribute("selectedAccount") Account account, @RequestParam("file") MultipartFile multipartFile, 
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException, ParseException {
		
		if(account == null || account.getId() == 0) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
			if(account == null) {
				redirectAttributes.addFlashAttribute("errorMessage", "You must select an account to to import into.");
				return redirect;
			}
		}
		
		InputStream inputStream = multipartFile.getInputStream();
		
//		new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)
//		                    .lines()
//		                    .forEach(this::handleLine);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		String line = reader.readLine();
		while(line != null) {
			handleLine(line, account);
			line = reader.readLine();
		}
		reader.close();
		
		redirectAttributes.addFlashAttribute("message", "Import successful!");
		
		return redirect;
	}
	
	private void handleLine(String line, Account account) throws ParseException {
		
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
			return;
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
			Option option = new Option();
			option.setAccount(account);
			System.out.println("symbol = " + symbol);
			
			option.setOptionSymbol(symbol);
			System.out.println("stockSymbol = "+ stockSymbol.toString());
			
			option.setSymbol(stockSymbol.toString());
			System.out.println("optionDate = " + optionDate.toString());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
			java.util.Date expirationDate = sdf.parse(optionDate.toString());
			option.setExpirationDate( new Date(expirationDate.getTime()) );
			
			System.out.println("optionType = " + optionType.toString());
			option.setOptionType(optionType.toString());
			
			System.out.println("optionStrike = " + optionStrike.toString());
			option.setStrikePrice(Double.valueOf(optionStrike.toString()));
			
			System.out.println("description = " + description.toString());
			option.setDescription(description.toString());
			
			System.out.println("quantity = " + quantity);
			option.setQuantity(Double.valueOf(quantity));
			
			System.out.println("dateAcquired = " + dateAcquired);
			SimpleDateFormat dAcquiredSoldFormat = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date dAcquired = dAcquiredSoldFormat.parse(dateAcquired);
			option.setDateAcquired( new Date(dAcquired.getTime()) );
			
			System.out.println("dateSold = " + dateSold);
			java.util.Date dSold = dAcquiredSoldFormat.parse(dateSold);
			option.setDateSold( new Date(dSold.getTime()) );
			
			System.out.println("proceeds = " + proceeds.toString());
			option.setProceeds(Double.valueOf(proceeds.toString()));
			
			System.out.println("costBasis = " + costBasis.toString());
			option.setCostBasis(Double.valueOf(costBasis.toString()));
			
			System.out.println("shortTermGain = " + shortTermGain.toString());
			if(!"".equals(shortTermGain.toString())) {
				option.setShortTermGain(Double.valueOf(shortTermGain.toString()));
			}
				
			System.out.println("longTermGain = " + longTermGain.toString());
			if(!"".equals(longTermGain.toString())) {
				option.setLongTermGain(Double.valueOf(longTermGain.toString()));
			}
			
			optionRepository.save(option);
			
		}
		
	}
	
	private void populate(Model model, Account account, HttpServletRequest request) {
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
		}
		
		if(account != null && account.getId() != 0) {
			request.getSession().setAttribute("selectedAccount", account);
		}
	}
	
}
