package com.brummer.investmenttracker.transactions.options.summary;

import java.text.ParseException;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.constants.TransactionStatusType;
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
		populate(account, null, null, model, request);
		return "optionTransactionSummaryList";
	}
	
	@RequestMapping("/transactionsByStatus")
	public String transactionsByStatus(@ModelAttribute("selectedTransactionStatusType") TransactionStatusType transactionStatusType, Model model, HttpServletRequest request) throws ParseException {
		populate(null, transactionStatusType, null, model, request);
		return "optionTransactionSummaryList";
	}
	
	@RequestMapping("/transactionsBySymbol")
	public String transactionsBySymbol(@ModelAttribute("selectedTransactionSymbol") String stockSymbol, Model model, HttpServletRequest request) throws ParseException {
		populate(null, null, stockSymbol, model, request);
		return "optionTransactionSummaryList";
	}
	
	private void populate(Account account, TransactionStatusType transactionStatusType, String stockSymbol, Model model, HttpServletRequest request) throws ParseException {
		model.addAttribute("accounts", accountRepository.findAll());
		model.addAttribute("transactionStatusTypes", EnumSet.allOf(TransactionStatusType.class));
		
		if(account == null) {
			account = (Account)request.getSession().getAttribute("selectedAccount");
		}
		else if(account.getId() == 0) {
			request.getSession().removeAttribute("selectedTransactionStatusType");
			request.getSession().removeAttribute("selectedTransactionSymbol");
		}
		
		if(transactionStatusType == null) {
			transactionStatusType = (TransactionStatusType)request.getSession().getAttribute("selectedTransactionStatusType");
		}
		
		if(stockSymbol == null) {
			stockSymbol = (String) request.getSession().getAttribute("selectedTransactionSymbol");
		}
		
		if(account != null && account.getId() != 0) {
			model.addAttribute("selectedAccount", account);
			request.getSession().setAttribute("selectedAccount", account);
			
			if(transactionStatusType != null) {
				model.addAttribute("selectedTransactionStatusType", transactionStatusType);
				request.getSession().setAttribute("selectedTransactionStatusType", transactionStatusType);
			}
			
			model.addAttribute("selectedTransactionSymbol", (stockSymbol==null) ? "" : stockSymbol );
			request.getSession().setAttribute("selectedTransactionSymbol", (stockSymbol==null) ? "" : stockSymbol );
			
			List<OptionTransactionSummary> tos = optionTransactionSummaryService.summarizeTransactions(
					transactionRepository.findByAccount(account), transactionStatusType, stockSymbol);
			
			model.addAttribute("transactionSummaries", tos);
			model.addAttribute("symbols", 
					optionTransactionSummaryService.getStockSymbols(
							optionTransactionSummaryService.summarizeTransactions(transactionRepository.findByAccount(account), transactionStatusType, null)
							)
					);
			
		}
	}
	
}
