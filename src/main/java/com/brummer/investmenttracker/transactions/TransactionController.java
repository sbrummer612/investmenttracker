package com.brummer.investmenttracker.transactions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;
import com.brummer.investmenttracker.constants.EquityType;
import com.brummer.investmenttracker.transactions.imports.TransactionImportFactory;
import com.brummer.investmenttracker.transactions.imports.TransactionImporter;

@Controller
@RequestMapping("/transactionController")
public class TransactionController {

	private AccountRepository accountRepository;
	private TransactionRepository transactionRepository;
	private TransactionImportFactory transactionImportFactory;
	private final String redirect = "redirect:/transactionController/transactions";
	
	public TransactionController(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionImportFactory transactionImportFactory) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.transactionImportFactory = transactionImportFactory;
	}
	
	@RequestMapping("/transactions")
	public String transactions(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		populate(account, model, request);
		return "transactionList";
	}
	
	@RequestMapping("/importTransactions")
	public String importTransactions(@ModelAttribute("selectedAccount") Account account, Model model, HttpServletRequest request) {
		populate(account, model, request);	
		return "transactionImport";
	}
	
	@PostMapping("/importTransactionsFile")
	public String importTransactionsFile(@ModelAttribute("selectedAccount") Account account, Model model, @RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
		
		InputStream inputStream = multipartFile.getInputStream();
		InputStream i = multipartFile.getInputStream();
		TransactionImporter transactionImporter = transactionImportFactory.getImporter(inputStream);
		int count = 0;
		Map<String, ArrayList<Transaction>> transactionMap = new HashMap<String, ArrayList<Transaction>>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(i, StandardCharsets.UTF_8));
		String line = reader.readLine();
		while(line != null) {
			
			line = reader.readLine();
			Transaction transaction = transactionImporter.parseLine(line);
			
			if(transaction != null) {
				
				String t = transaction.toString();
				if( transactionMap.containsKey(t) ){
					transactionMap.get(t).add(transaction);
				}
				else {
					ArrayList<Transaction> transactions = new ArrayList<Transaction>();
					transactions.add(transaction);
					transactionMap.put(t, transactions);
				}
				
			}
		}
		reader.close();
		
		for(Map.Entry<String, ArrayList<Transaction>> entry : transactionMap.entrySet() ) {
			if(entry.getValue() != null && entry.getValue().size() > 0) {
				for(Transaction transaction : entry.getValue()) {
					if(transaction.getEquityType().equals(EquityType.OPTION.getValue())) {
						// currently only importing options
						ExampleMatcher matcher = 
								ExampleMatcher.matching()
									.withIgnorePaths("id");
						Example<Transaction> example = Example.of(transaction, matcher);
						if(transactionRepository.findAll(example).size() < entry.getValue().size()) {
							transactionRepository.save(transaction);
							count++;
						}	
					}
				}
			}
		}
		
		populate(account, model, request);
		model.addAttribute("message", "Imported: " + count + " rows.");
		return "transactionImport";
	}
	
	@GetMapping("/deleteAllTransactions")
	public String deleteAllTransactions() {
		transactionRepository.deleteAll();
		return redirect;
	}
	
	private void populate(Account account, Model model, HttpServletRequest request) {
		model.addAttribute("accounts", accountRepository.findAll());
		if(account == null || account.getId() == 0) {
			account = (Account)request.getAttribute("selectedAccount");
		}
		
		if(account != null && account.getId() != 0) {
			model.addAttribute("selectedAccount", account);
			model.addAttribute("transactions", transactionRepository.findByAccountOrderByTransactionDateDesc(account));
		}
		
	}
	
}
