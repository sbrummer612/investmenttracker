package com.brummer.investmenttracker.transactions.options.summary;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.transactions.TransactionRepository;

@Controller
@RequestMapping("/optionTransactionSummaryController")
public class OptionTransactionSummaryController {

	private AccountRepository accountRepository;
	private TransactionRepository transactionRepository;
	private OptionTransactionSummaryService optionTransactionSummaryService;
//	private final String redirect = "redirect:/transactionSummaryController/transactions";

	public OptionTransactionSummaryController(AccountRepository accountRepository, TransactionRepository transactionRepository, OptionTransactionSummaryService optionTransactionSummaryService) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.optionTransactionSummaryService = optionTransactionSummaryService;
	}
	
	@RequestMapping("/transactions")
	public String transactions(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) throws ParseException {
		populate(account, model, request);
		return "optionTransactionSummaryList";
	}
	
	private void populate(Account account, Model model, HttpServletRequest request) throws ParseException {
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getAttribute("selectedAccount");
		}
		
		if(account != null && account.getId() != 0) {
			model.addAttribute("selectedAccount", account);
			model.addAttribute("transactionSummaries", 
					optionTransactionSummaryService.summarizeTransactions(
							transactionRepository.findByAccount(account))
					);
		}
	}
	
}
