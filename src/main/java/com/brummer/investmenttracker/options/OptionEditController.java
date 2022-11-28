package com.brummer.investmenttracker.options;

import javax.servlet.http.HttpServletRequest;
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

@Controller
@RequestMapping("/optionEditController")
public class OptionEditController {

	private AccountRepository accountRepository;
	private OptionRepository optionRepository;
	private final String redirect = "redirect:/optionEditController/options";
	
	public OptionEditController(AccountRepository accountRepository, OptionRepository optionRepository) {
		this.accountRepository = accountRepository;
		this.optionRepository = optionRepository;
	}
	
	@RequestMapping("/options")
	public String options(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		populate(model, account, request);
		return "optionsList";
	}
	
	@GetMapping("/addOption")
	public String addOption(Model model, HttpServletRequest request) { 
		if(request.getSession().getAttribute("selectedAccount") == null) {
			model.addAttribute("accountSelectionErrort", "You must select an Account from the dropdown.");
			populate(model, null, request);
			return "optionsList";
		}
		else {
			model.addAttribute("option", new Option());
			return "optionAddEdit";	
		}
	}
	
	@GetMapping("/editOption")
	public String editTrade(@RequestParam("id") String optionId, Model model, HttpServletRequest request) { 
		if(optionId != null) {
			Long optionIdLong = Long.valueOf(optionId);
			model.addAttribute("option", optionRepository.findById(optionIdLong).get());
		}
		return "optionAddEdit";
	}
	
	@PostMapping("/addUpdateOption")
	public String addUpdateOption(@Valid @ModelAttribute Option option, BindingResult bindingResult, @ModelAttribute("selectedAccount") Account account, Model model) {
		if(bindingResult.hasErrors()) {
			return "optionAddEdit";
		}
		optionRepository.save(option);
		return redirect;
	}
	
	@GetMapping("/deleteOption")
	public String deleteOption(@RequestParam String id) {
		if(id!=null) {
			Long idLong = Long.parseLong(id);
			optionRepository.deleteById(idLong);
		}
		return redirect;
	}
	
	@GetMapping("/deleteAllOptions")
	public String deleteAllOptions() {
		
		optionRepository.deleteAll();
		
		return redirect;
	}
	
	private void populate(Model model, Account account, HttpServletRequest request) {
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
		}
		
		if(account != null && account.getId() != 0) {
			request.getSession().setAttribute("selectedAccount", account);
			model.addAttribute("options", optionRepository.findByAccountOrderByExpirationDateDesc(account));
		}
	}
	
}
