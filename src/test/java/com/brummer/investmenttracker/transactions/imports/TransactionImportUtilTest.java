package com.brummer.investmenttracker.transactions.imports;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

//import org.junit.BeforeClass;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.accounts.AccountRepository;

@SpringBootTest
public class TransactionImportUtilTest {

	@Mock
	public AccountRepository accountRepositoryMock;
	
//	@BeforeClass
	public void testSetUp() {
		List<Account> accounts = new ArrayList<Account>();
		Account account = new Account();
		account.setName("Joint WROS - TOD (X30097152)");
		accounts.add(account);
		when(accountRepositoryMock.findByName("Joint WROS - TOD X30097152")).thenReturn(accounts);
	}
	
}
