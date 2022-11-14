package com.brummer.investmenttracker.options;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.accounts.Account;

@SpringBootTest
public class OptionImportServiceTest {

	private final String lineHeader = 
			"Symbol(CUSIP),Security Description,Quantity,Date Acquired,Date Sold,Proceeds,Cost Basis,Short Term Gain/Loss,Long Term Gain/Loss";
	private final String lineStock = 
			"ABNB(009066101),\"AIRBNB INC COM CL A\",30.00000,08/04/2022,08/17/2022,\"$3621.38 \",\"$3562.51 \",\"$58.87 \",-";
	private final String lineCallOption = 
			"BRKB220414C357.5(5835689CU),\"CALL (BRKB) BERKSHIRE HATHAWAY APR 14 22 $357.5 (100 SHS)\",2.00000,04/05/2022,04/12/2022,\"$160.63 \",\"$32.06 \",\"$128.57 \",-";
	private final String linePutOption1 = 
			"SPY220518P390(5866779RL),\"PUT (SPY) SPDR S&P500 ETF MAY 18 22 $390 (100 SHS)\",2.00000,05/13/2022,05/13/2022,\"$650.62 \",\"$477.36 \",\"$173.26 \",-";
	
	@Autowired
	private OptionImportService optionImportService;
	
	@Test
	void testNullImport() throws ParseException {
		
		Option option = optionImportService.handleLine(null, null);
		assertThat(option).isNull();
		
	}
	
	@Test
	void testEmptyString() throws ParseException {
		
		Option option = optionImportService.handleLine("", null);
		assertThat(option).isNull();
		
	}
	
	@Test
	void testHeaderString() throws ParseException {
		
		Option option = optionImportService.handleLine(lineHeader, null);
		assertThat(option).isNull();
		
		Account account = new Account();
		Option option2 = optionImportService.handleLine(lineHeader, account);
		assertThat(option2).isNull();
		
	}
	
	@Test
	void testStockString() throws ParseException {
		
		Option option = optionImportService.handleLine(lineStock, null);
		assertThat(option).isNull();
		
		Account account = new Account();
		Option option2 = optionImportService.handleLine(lineStock, account);
		assertThat(option2).isNull();
		
	}
	
	
	
}
