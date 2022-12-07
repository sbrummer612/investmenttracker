package com.brummer.investmenttracker.transactions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brummer.investmenttracker.accounts.Account;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	List<Transaction> findBySymbol(String symbol);
	
	List<Transaction> findByAccount(Account account);
	
	List<Transaction> findByAccountAndOptionType(Account account, String optionType);
	
	List<Transaction> findByAccountOrderByTransactionDateDesc(Account account);
	
}
