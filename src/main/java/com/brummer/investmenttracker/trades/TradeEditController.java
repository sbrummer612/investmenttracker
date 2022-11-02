package com.brummer.investmenttracker.trades;

import java.util.List;

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
@RequestMapping("/tradeEditController")
public class TradeEditController {

	private AccountRepository accountRepository;
	private TradeRepository tradeRepository;
	private final String redirect = "redirect:/tradeEditController/trades";
	
	public TradeEditController(AccountRepository accountRepository, TradeRepository tradeRepository) {
		this.accountRepository = accountRepository;
		this.tradeRepository = tradeRepository;
	}
	
	@RequestMapping("/trades")
	public String trades(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		populate(model, account, request);
		return "tradesList";
	}
	
	@GetMapping("/editTrade")
	public String editTrade(@RequestParam("id") String tradeId, Model model, HttpServletRequest request) { 
		if(tradeId != null) {
			Long tradeIdLong = Long.valueOf(tradeId);
			model.addAttribute("trade", tradeRepository.findById(tradeIdLong).get());
		}
		return "tradeAddEdit";
	}
	
	@GetMapping("/addTrade")
	public String addTrade(Model model, HttpServletRequest request) { 
		model.addAttribute("trade", new Trade());
		return "tradeAddEdit";
	}
	
	@PostMapping("/addUpdateTrade")
	public String addTrade(@Valid @ModelAttribute Trade trade, BindingResult bindingResult, @ModelAttribute("selectedAccount") Account account, Model model) {
		if(bindingResult.hasErrors()) {
			return "tradeAddEdit";
		}
		tradeRepository.save(trade);
		return redirect;
	}
	
	@GetMapping("/deleteTrade")
	public String deleteTrade(@RequestParam String id) {
		if(id != null) {
			Long idLong = Long.parseLong(id);
			tradeRepository.deleteById(idLong);
		}
		return redirect;
	}
	
	private void populate(Model model, Account account, HttpServletRequest request) {
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
		}
		
		if(account != null && account.getId() != 0) {
			request.getSession().setAttribute("selectedAccount", account);
			model.addAttribute("trades", tradeRepository.findByAccount(account));
		}
	}
	
}
