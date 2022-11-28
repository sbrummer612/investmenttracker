package com.brummer.investmenttracker.options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
	private final OptionImportService optionImportService;
	private final OptionRepository optionRepository;
	private final String redirect = "redirect:/optionImportController/importOptionsHistory";
	
	public OptionImportController(AccountRepository accountRepository, OptionImportService optionImportService, OptionRepository optionRepository) {
		this.accountRepository = accountRepository;
		this.optionImportService = optionImportService;
		this.optionRepository = optionRepository;
	}

	@RequestMapping("/importOptionsHistory")
	public String importOptionsHistory(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		populate(model, account, request);
		return "optionImportHistory";
	}
	
	@PostMapping("/importOptionsHistoryFile")
	public String importOptionsHistoryFile(@ModelAttribute("selectedAccount") Account account, @RequestParam("file") MultipartFile multipartFile, 
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException, ParseException {
		
		InputStream inputStream = multipartFile.getInputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		String line = reader.readLine();
		while(line != null) {
			Option option = optionImportService.handleLineHistory(line);
			if(option != null) {
				if(option.getExtendedDescription().indexOf("YOU SOLD OPENING TRANSACTION") > -1) {
					ExampleMatcher matcher = 
							ExampleMatcher.matching()
								.withIgnorePaths("id", "description", "quantity", "dateAcquired", "dateSold", "proceeds", "shortTermGain", "longTermGain");
					Example<Option> example = Example.of(option, matcher);
					if(optionRepository.findAll(example).isEmpty()) {
						// not found so create it
						optionRepository.save(option);	
					}
				}
				else if(option.getExtendedDescription().toUpperCase().indexOf("YOU BOUGHT CLOSING TRANSACTION") > -1 || 
						option.getExtendedDescription().toUpperCase().indexOf("ASSIGNED") > -1 ||
						option.getExtendedDescription().toUpperCase().indexOf("EXPIRED") > -1){
					
					ExampleMatcher matcher = 
							ExampleMatcher.matching()
								.withIgnorePaths("id", "description", "quantity", "dateAcquired", "dateSold", "proceeds", "costBasis", "shortTermGain", "longTermGain");
					Example<Option> example = Example.of(option, matcher);
					if(!optionRepository.findAll(example).isEmpty()) {
						if(optionRepository.findAll(example).size() > 0) {
							Option existingOption = optionRepository.findAll(example).get(0);
							existingOption.setDateSold(option.getDateSold());
							existingOption.setQuantity(existingOption.getQuantity() + option.getQuantity());
							existingOption.setProceeds((existingOption.getProceeds() == null) ? option.getProceeds() : existingOption.getProceeds() + option.getProceeds());
							existingOption.setShortTermGain(existingOption.getCostBasis() + option.getProceeds());
							optionRepository.save(existingOption);
						}
					}
					
				}
			}
			line = reader.readLine();
		}
		reader.close();
		
		redirectAttributes.addFlashAttribute("message", "Import successful!");
		
		return redirect;
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
		ExampleMatcher matcher = 
				ExampleMatcher.matching()
					.withIgnorePaths("id");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		String line = reader.readLine();
		while(line != null) {
			Option option = optionImportService.handleLine(line, account);
			if(option != null) {
				Example<Option> example = Example.of(option, matcher);
				if(optionRepository.findAll(example).isEmpty()) {
					// not found so create it
					optionRepository.save(option);	
				}
			}
			line = reader.readLine();
		}
		reader.close();
		
		redirectAttributes.addFlashAttribute("message", "Import successful!");
		
		return redirect;
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
