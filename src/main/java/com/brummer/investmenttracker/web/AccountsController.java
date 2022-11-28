package com.brummer.investmenttracker.web;

import java.util.EnumSet;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.constants.BrokerageTypes;

@Controller
@RequestMapping("/accountsController")
public class AccountsController {

	private final AccountRepository accountRepository;
	private final String redirect = "redirect:/accountsController/accounts";
	
	public AccountsController(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@RequestMapping("/accounts")
	public String accounts(Model model) {
		model.addAttribute("accounts", accountRepository.findAll());
		populate(model);
		return "accounts";
	}
	
	@GetMapping("/addAccount")
	public String addAccount(Model model) {
		model.addAttribute("account", new Account());
		populate(model);
		
		return "accountAddEdit";
	}
	
	@PostMapping("/addUpdateAccount")
	public String addAccount(@Valid @ModelAttribute Account account, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			populate(model);
			return "accountAddEdit";
		}
		accountRepository.save(account);
		return redirect;
	}
	
	@GetMapping("/deleteAccount")
	public String deleteAccount(@RequestParam String id) {
		if(id != null) {
			Long idLong = Long.parseLong(id);
			accountRepository.deleteById(idLong);
		}
		return redirect;
	}
	
	@GetMapping("/editAccount")
	public String editAccount(@RequestParam String id, Model model) {
		if(id != null) {
			Long idLong = Long.parseLong(id);
			Optional<Account> obj = accountRepository.findById(idLong);
			model.addAttribute("account", obj.get());
			populate(model);
//			model.addAttribute("accounts", accountRepository.findAll());
		}
		return "accountAddEdit";
	}
	
	private void populate(Model model) {
		model.addAttribute("brokerages", EnumSet.allOf(BrokerageTypes.class));
	}
	
}
