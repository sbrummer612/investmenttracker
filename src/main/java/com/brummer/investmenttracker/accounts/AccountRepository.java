package com.brummer.investmenttracker.accounts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{

	List<Account> findByName(String name);
	
}
