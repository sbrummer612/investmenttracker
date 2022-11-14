package com.brummer.investmenttracker.options;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brummer.investmenttracker.accounts.Account;

public interface OptionRepository extends JpaRepository<Option, Long>{

	List<Option> findByAccountAndSymbolAndOptionTypeOrderByDateSoldDesc(Account account, String symbol, String optionType);
	
	List<Option> findByAccount(Account account);
	
}
