package com.brummer.investmenttracker.trades;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.options.OptionRepository;

@Controller
@RequestMapping("/tradeSummaryController")
public class TradeSummaryController {

	private final AccountRepository accountRepository;
	private final TradeRepository tradeRepository;
	private final OptionRepository optionRepository;
//	private final String redirect = "redirect:/tradeSummaryController/tradesByAccount";
	
	public TradeSummaryController(AccountRepository accountRepository, TradeRepository tradeRepository, OptionRepository optionRepository) {
		this.accountRepository = accountRepository;
		this.tradeRepository = tradeRepository;
		this.optionRepository = optionRepository;
	}
	
	@PostMapping("/tradesByAccount")
	public String tradesByAccount(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
		}
		
		if(account != null && account.getId() != 0) {
			request.getSession().setAttribute("selectedAccount", account);
			model.addAttribute("trades", tradeRepository.findByAccount(account));
		}
		
		return "tradesSummary";
	}
	
}
