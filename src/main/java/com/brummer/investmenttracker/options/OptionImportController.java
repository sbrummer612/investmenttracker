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
	private final String redirect = "redirect:/optionImportController/importOptions";
	
	public OptionImportController(AccountRepository accountRepository, OptionImportService optionImportService, OptionRepository optionRepository) {
		this.accountRepository = accountRepository;
		this.optionImportService = optionImportService;
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
