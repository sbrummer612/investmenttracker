package com.brummer.investmenttracker.trades;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.options.OptionRepository;

@Controller
@RequestMapping("/tradeSummaryController")
public class TradeSummaryController {

	private final AccountRepository accountRepository;
	private final TradeRepository tradeRepository;
	private final OptionRepository optionRepository;
	private final TradeTransientFields tradeTransientFields;
//	private final String redirect = "redirect:/tradeSummaryController/tradesByAccount";
	
	public TradeSummaryController(AccountRepository accountRepository, TradeRepository tradeRepository, OptionRepository optionRepository, TradeTransientFields tradeTransientFields) {
		this.accountRepository = accountRepository;
		this.tradeRepository = tradeRepository;
		this.optionRepository = optionRepository;
		this.tradeTransientFields = tradeTransientFields;
	}
	
	@RequestMapping("/tradeSummary")
	public String tradeSummary(Model model, HttpServletRequest request) {
		populate(model, null, request);
		return "tradesSummary";
	}
	
//	@GetMapping("tradesByAccountGet")
//	public String tradesByAccountGet(@RequestParam("selectedAccount") String id, Model model, HttpServletRequest request) {
//		Optional<Account> account = null;
//		if(id != null) {
//			account = accountRepository.findById(Long.valueOf(id));
//		}
//		populate(model, account.get(), request);
//		
//		return "tradesSummary";
//	}
	
	@PostMapping("/tradesByAccount")
	public String tradesByAccount(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		
		populate(model, account, request);
		return "tradesSummary";
	}
	
	private void populate(Model model, Account account, HttpServletRequest request) {
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
		}
		
		if(account != null && account.getId() != 0) {
			request.getSession().setAttribute("selectedAccount", account);
			model.addAttribute("selectedAccount", account);
			List<Trade> trades = tradeRepository.findByAccount(account);
			final Account tAccount = account;
			trades
				.forEach(
						trade -> 
							trade.setOptions(
									optionRepository.findByAccountAndSymbolAndOptionTypeOrderByExpirationDateDesc(tAccount, trade.getSymbol(), "C")
							) 
				);
			tradeTransientFields.computeTransientFields(trades);
			model.addAttribute("trades", trades);
		}
	}
	
}
