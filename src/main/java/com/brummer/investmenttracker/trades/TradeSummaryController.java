package com.brummer.investmenttracker.trades;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.constants.OptionType;
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
		populate(model, null, null, request);
		return "tradesSummary";
	}
	
	@PostMapping("/tradesByAccount")
	public String tradesByAccount(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		
		populate(model, account, null, request);
		return "tradesSummary";
	}
	
	@PostMapping("/tradesByOptionType")
	public String tradesByOptionType(@ModelAttribute("selectedOptionType") OptionType optionType, Model model, HttpServletRequest request) {
		
		populate(model, null, optionType, request);
		return "tradesSummary";
	}
	
	private void populate(Model model, Account account, OptionType optionType, HttpServletRequest request) {
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
		}
		
		EnumSet.allOf(OptionType.class);
		model.addAttribute("optionTypes", EnumSet.allOf(OptionType.class));
		if(optionType == null) {
			optionType = (OptionType) request.getSession().getAttribute("selectedOptionType");
		}
		
		if(optionType != null) {
			request.getSession().setAttribute("selectedOptionType", optionType);
			model.addAttribute("selectedOptionType", "optionType");
		}
		
		if(account != null && account.getId() != 0) {
			
			request.getSession().setAttribute("selectedAccount", account);
			model.addAttribute("selectedAccount", account);
			List<Trade> trades = tradeRepository.findByAccountOrderBySymbol(account);
			final Account tAccount = account;
			final OptionType tOptionType = (optionType != null) ? optionType : OptionType.ALL;
			trades
				.forEach(
						trade -> 
							trade.setOptions(
									optionRepository.findByAccountAndSymbolAndOptionTypeOrderByDateSoldDesc(tAccount, trade.getSymbol(), tOptionType.getType())
							) 
				);
			tradeTransientFields.computeTransientFields(trades);
			model.addAttribute("trades", trades);
		}
	}
	
}
