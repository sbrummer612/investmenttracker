package com.brummer.investmenttracker.transactions.trades.summary;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.constants.OptionType;
import com.brummer.investmenttracker.constants.TransactionStatusType;
import com.brummer.investmenttracker.trades.Trade;
import com.brummer.investmenttracker.trades.TradeRepository;
import com.brummer.investmenttracker.trades.TradeTransactionSummaryService;
import com.brummer.investmenttracker.transactions.TransactionRepository;
import com.brummer.investmenttracker.transactions.options.summary.OptionTransactionSummaryService;

@Controller
@RequestMapping("/tradeTransactionSummaryController")
public class TradeTransactionSummaryController {

	private final TradeRepository tradeRepository;
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final OptionTransactionSummaryService optionTransactionSummaryService;
	private final TradeTransactionSummaryService tradeTransactionSummaryService;
	
	public TradeTransactionSummaryController(TradeRepository tradeRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, OptionTransactionSummaryService optionTransactionSummaryService, TradeTransactionSummaryService tradeTransactionSummaryService) {
		this.tradeRepository = tradeRepository;
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.optionTransactionSummaryService = optionTransactionSummaryService;
		this.tradeTransactionSummaryService = tradeTransactionSummaryService;
	}
	
//	@RequestMapping("/trades")
	@GetMapping("/trades")
	public String trades(@RequestParam(required = false) String selectedAccountId, Model model, HttpServletRequest request) throws ParseException {
		
		populate(selectedAccountId, null, null, null, null, null, model, request);
		return "tradeTransactionSummaryList";
	}
	
	@RequestMapping("/tradesByStatus")
	public String tradesByStatus(@ModelAttribute("selectedTransactionStatusType") TransactionStatusType transactionStatusType, Model model, HttpServletRequest request) throws ParseException {
		populate(null, transactionStatusType, null, null, null, null, model, request);
		return "tradeTransactionSummaryList";
	}
	
	@RequestMapping("/tradesBySymbol")
	public String tradesBySymbol(@ModelAttribute("selectedTransactionSymbol") String stockSymbol, Model model, HttpServletRequest request) throws ParseException {
		populate(null, null, stockSymbol, null, null, null, model, request);
		return "tradeTransactionSummaryList";
	}
	
	@RequestMapping("/tradesByOptionType")
	public String tradesByOptionType(@ModelAttribute("selectedTransactionOptionType") String optionType, Model model, HttpServletRequest request) throws ParseException {
		populate(null, null, null, optionType, null, null, model, request);
		return "tradeTransactionSummaryList";
	}
	
	@RequestMapping("/tradesByDate")
	public String tradesByDate(@ModelAttribute("selectedStartDate") String selectedStartDate, @ModelAttribute("selectedEndDate") String selectedEndDate,  Model model, HttpServletRequest request) throws ParseException {
		populate(null, null, null, null, selectedStartDate, selectedEndDate, model, request);
		return "tradeTransactionSummaryList";
	}

	private void populate(String selectedAccountId, TransactionStatusType transactionStatusType, String stockSymbol, String optionType, String selectedStartDate, String selectedEndDate, Model model, HttpServletRequest request) throws ParseException {
		
		List<Account> accounts = new ArrayList<Account>();
		Account allAccount = new Account();
		allAccount.setId(-1l);
		allAccount.setName("All Accounts");
		accounts.add(allAccount);
		accounts.addAll(accountRepository.findAll());
		
		model.addAttribute("accounts", accounts);
		model.addAttribute("transactionStatusTypes", EnumSet.allOf(TransactionStatusType.class));
		model.addAttribute("optionTypes", EnumSet.allOf(OptionType.class));

		
		Account account = null;
		if(selectedAccountId != null && !"-1".equals(selectedAccountId) && !"".equals(selectedAccountId)) {
			Optional<Account> accnt = accountRepository.findById(Long.valueOf(selectedAccountId));
			account = (accnt != null && !accnt.isEmpty()) ? accnt.get() : null;
		}
		else if("-1".equals(selectedAccountId)) {
			account = allAccount;
		}
		else if("".equals(selectedAccountId)) {
			account = null;
			request.getSession().removeAttribute("selectedAccount");
		}
		
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
		
		if(optionType == null) {
			optionType = (String) request.getSession().getAttribute("selectedTransactionOptionType");
		}
		
		if(stockSymbol == null) {
			stockSymbol = (String) request.getSession().getAttribute("selectedTransactionSymbol");
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // "MM/dd/yyyy");
		if(selectedStartDate == null) {
			selectedStartDate = (String)request.getSession().getAttribute("selectedStartDate");
		}
		if(selectedStartDate == null || "".equals(selectedStartDate)) {
			selectedStartDate = LocalDate.now().minusDays(30).toString();// .withDayOfMonth(1).toString();
			request.getSession().setAttribute("selectedStartDate", selectedStartDate);
		}
		model.addAttribute("selectedStartDate", selectedStartDate);
		request.getSession().setAttribute("selectedStartDate", selectedStartDate);
		java.util.Date sDate = dateFormat.parse(selectedStartDate);
		Date startDate = new Date(sDate.getTime());
		
		if(selectedEndDate == null) {
			selectedEndDate = (String)request.getSession().getAttribute("selectedEndDate");
		}
		if(selectedEndDate == null || "".equals(selectedEndDate)) {
			selectedEndDate = LocalDate.now().toString();
			request.getSession().setAttribute("selectedEndDate", selectedEndDate);
		}
		model.addAttribute("selectedEndDate", selectedEndDate);
		request.getSession().setAttribute("selectedEndDate", selectedEndDate);
		java.util.Date eDate = dateFormat.parse(selectedEndDate);
		Date endDate = new Date(eDate.getTime());
		
		if(account != null && account.getId() != 0) {
			model.addAttribute("selectedAccount", account);
			request.getSession().setAttribute("selectedAccount", account);
			
			if(transactionStatusType != null) {
				model.addAttribute("selectedTransactionStatusType", transactionStatusType);
				request.getSession().setAttribute("selectedTransactionStatusType", transactionStatusType);
			}
			
			model.addAttribute("selectedTransactionOptionType", (optionType == null) ? OptionType.ALL.getValue() : optionType);
			request.getSession().setAttribute("selectedTransactionOptionType", (optionType == null) ? OptionType.ALL.getValue() : optionType);
			
			model.addAttribute("selectedTransactionSymbol", (stockSymbol==null) ? "" : stockSymbol );
			request.getSession().setAttribute("selectedTransactionSymbol", (stockSymbol==null) ? "" : stockSymbol );
			
			List<Trade> trades = new ArrayList<Trade>();
			List<Trade> symbolTrades = new ArrayList<Trade>();
			if(account.getId() == -1) {
				if(stockSymbol == null || "".equals(stockSymbol)) {
					trades = tradeRepository.findAll();
					symbolTrades = trades;
				}
				else {
					trades = tradeRepository.findBySymbol(stockSymbol);
					symbolTrades = tradeRepository.findAll();
				}
			}
			else {
				if(stockSymbol == null || "".equals(stockSymbol)) {
					trades = tradeRepository.findByAccountOrderBySymbol(account);
					symbolTrades = trades;
				}
				else {
					trades = tradeRepository.findByAccountAndSymbol(account, stockSymbol);
					symbolTrades = tradeRepository.findByAccountOrderBySymbol(account);
				}
			}
			
			for(Trade trade : trades) {
				
				if(optionType == null || OptionType.ALL.getValue().equals(optionType)) {
					if(transactionStatusType != null && TransactionStatusType.OPEN.getValue().equals(transactionStatusType.getValue())) {
						trade.setOptionTransactionSummaries(
							optionTransactionSummaryService.summarizeTransactions(
								transactionRepository.findByAccount(trade.getAccount()), 
								transactionStatusType, 
								trade.getSymbol()
							)
						);
					}
					else {
						trade.setOptionTransactionSummaries(
							optionTransactionSummaryService.summarizeTransactions(
								transactionRepository.findByAccount(trade.getAccount()), 
								transactionStatusType, 
								trade.getSymbol()
							).stream()
							.filter(
									ots -> 
									( 
										ots.getDateClosed() == null 
									)
									||
									(
										ots.getDateClosed() != null && 
										( startDate.before(ots.getDateClosed()) || startDate.equals(ots.getDateClosed()) ) && 
										( endDate.after(ots.getDateClosed()) || endDate.equals(ots.getDateClosed()) )
									)
							)
							.collect(Collectors.toList())
						);
					}
				}
				else {
					if(transactionStatusType != null && TransactionStatusType.OPEN.getValue().equals(transactionStatusType.getValue())) {
						trade.setOptionTransactionSummaries(
							optionTransactionSummaryService.summarizeTransactions(
								transactionRepository.findByAccountAndOptionType(trade.getAccount(), optionType), 
								transactionStatusType, 
								trade.getSymbol()
							)
						);
					}
					else {
						trade.setOptionTransactionSummaries(
							optionTransactionSummaryService.summarizeTransactions(
								transactionRepository.findByAccountAndOptionType(trade.getAccount(), optionType), 
								transactionStatusType, 
								trade.getSymbol()
							).stream()
							.filter(
									ots -> 
									( 
										ots.getDateClosed() == null 
									)
									||
									(
										ots.getDateClosed() != null && 
										( startDate.before(ots.getDateClosed()) || startDate.equals(ots.getDateClosed()) ) && 
										( endDate.after(ots.getDateClosed()) || endDate.equals(ots.getDateClosed()) )
									)
							)
							.collect(Collectors.toList())
						);
					}
				}
				
				
			}
			
			model.addAttribute("symbols", symbolTrades.stream().map(trade -> trade.getSymbol()).distinct().sorted().collect(Collectors.toCollection(ArrayList<String>::new)) );
					
			tradeTransactionSummaryService.summarizeTransactions((trades));
			
			model.addAttribute( "trades", trades );
			
			model.addAttribute( "tradeSummary", tradeTransactionSummaryService.buildTradeSummaryTransaction(trades) );
		}
		
	}
	
}
