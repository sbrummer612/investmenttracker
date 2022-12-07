package com.brummer.investmenttracker.trades;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brummer.investmenttracker.accounts.Account;

public interface TradeRepository extends JpaRepository<Trade, Long>{

//	List<Trade> findByAccount(Account account);
	List<Trade> findByAccountOrderBySymbol(Account account);
	List<Trade> findByAccountAndSymbol(Account account, String symbol);
	List<Trade> findBySymbol(String symbol);
}
