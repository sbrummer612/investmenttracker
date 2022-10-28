package com.brummer.investmenttracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;

@Controller
@RequestMapping("/accountsController")
public class AccountsController {

	private final AccountRepository accountRepository;
	
	public AccountsController(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@RequestMapping("/accounts")
	public String accounts(Model model) {
		model.addAttribute("accounts", accountRepository.findAll());
		return "accounts";
	}
	
	@PostMapping("/addUpdateAccount")
	public String addAccount(@ModelAttribute Account account) {
		accountRepository.save(account);
		return "redirect:/accountsController/accounts";
	}
	
	@GetMapping("/deleteAccount")
	public String deleteAccount(@RequestParam String id) {
		if(id != null) {
			Long idLong = Long.parseLong(id);
			accountRepository.deleteById(idLong);
		}
		return "redirect:/accountsController/accounts";
	}
	
}
