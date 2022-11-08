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
import com.brummer.investmenttracker.options.Option;
import com.brummer.investmenttracker.options.OptionRepository;

@Controller
@RequestMapping("/tradeController")
public class TradeController {

	private final TradeRepository tradeRepository;
	private final AccountRepository accountRepository;
	private final OptionRepository optionRepository;
	private final String redirect = "redirect:/tradeController/tradeSummary";
	
	public TradeController(TradeRepository tradeRepository, AccountRepository accountRepository, OptionRepository optionRepository) {
		this.tradeRepository = tradeRepository;
		this.accountRepository = accountRepository;
		this.optionRepository = optionRepository;
	}
	
	@PostMapping("/tradesByAccount")
	public String tradesByAccount(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		
		model.addAttribute("accounts", accountRepository.findAll());
		request.getSession().setAttribute("selectedAccount", account );
		List<Trade> trades = tradeRepository.findByAccount(account);
		for(Trade trade : trades) {
			trade.setOptions(optionRepository.findByAccountAndSymbolAndOptionTypeOrderByExpirationDateDesc(account, trade.getSymbol(), "C"));
		}
		model.addAttribute("trades", trades);
		
		return "trades";
	}
	
	@RequestMapping("/tradeSummary")
	public String trades(Model model, HttpServletRequest request) {
		model.addAttribute("accounts", accountRepository.findAll());
		Account account = (Account)request.getSession().getAttribute("selectedAccount");
		if(account != null) {
			model.addAttribute("trades", tradeRepository.findByAccount(account));
		}
		return "trades";
	}
	
	@PostMapping("/addUpdateTrade")
	public String addTrade(@Valid Trade trade, BindingResult bindingResult, @ModelAttribute("selectedAccount") Account account, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("trades", tradeRepository.findAll());
			model.addAttribute("accounts", accountRepository.findAll());
			model.addAttribute("selectedAccount", account);
			model.addAttribute("options", optionRepository.findAll());
			return "trades";
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
	
	@GetMapping("/editTrade")
	public String editTrade(@RequestParam("id") String tradeId, Model model, HttpServletRequest request) { //  @ModelAttribute("selectedAccount") Account account, 
		if(tradeId != null) {
			Long tradeIdLong = Long.valueOf(tradeId);
			model.addAttribute("trade", tradeRepository.findById(tradeIdLong).get());
			Account account = (Account)request.getSession().getAttribute("selectedAccount");
			if(account != null) {
				model.addAttribute("trades", tradeRepository.findByAccount(account));
			}
			List<Account> accounts = accountRepository.findAll();
			model.addAttribute("accounts", accounts);
			model.addAttribute( "selectedAccount", request.getSession().getAttribute("selectedAccount") );
			model.addAttribute("options", optionRepository.findAll());
		}
		return "trades";
	}
	
	@PostMapping("/addUpdateOption")
	public String addOption(@Valid @ModelAttribute Option option, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("trades", tradeRepository.findAll());
			model.addAttribute("options", optionRepository.findAll());
			return "trades";
		}
		optionRepository.save(option);
		return redirect;
	}
	
	@GetMapping("/deleteOption")
	public String deleteOption(@RequestParam String id) {
		if(id != null) {
			Long idLong = Long.parseLong(id);
			optionRepository.deleteById(idLong);
		}
		return redirect;
	}
	
	@GetMapping("/editOption")
	public String editOption(@RequestParam String id, Model model) {
		if(id != null) {
			Long idLong = Long.valueOf(id);
			model.addAttribute("option", optionRepository.findById(idLong).get());
			model.addAttribute("trades", tradeRepository.findAll());
			model.addAttribute("accounts", accountRepository.findAll());
			model.addAttribute("options", optionRepository.findAll());
		}
		return "trades";
	}
	
}
