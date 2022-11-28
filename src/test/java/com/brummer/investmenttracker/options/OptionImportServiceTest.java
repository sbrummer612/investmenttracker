package com.brummer.investmenttracker.options;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.transactions.imports.TransactionImportUtilTest;

@SpringBootTest
public class OptionImportServiceTest extends TransactionImportUtilTest {

	private final String lineHeader = 
			"Symbol(CUSIP),Security Description,Quantity,Date Acquired,Date Sold,Proceeds,Cost Basis,Short Term Gain/Loss,Long Term Gain/Loss";
	private final String lineStock = 
			"ABNB(009066101),\"AIRBNB INC COM CL A\",30.00000,08/04/2022,08/17/2022,\"$3621.38 \",\"$3562.51 \",\"$58.87 \",-";
//	private final String lineCallOption = 
//			"BRKB220414C357.5(5835689CU),\"CALL (BRKB) BERKSHIRE HATHAWAY APR 14 22 $357.5 (100 SHS)\",2.00000,04/05/2022,04/12/2022,\"$160.63 \",\"$32.06 \",\"$128.57 \",-";
//	private final String linePutOption1 = 
//			"SPY220518P390(5866779RL),\"PUT (SPY) SPDR S&P500 ETF MAY 18 22 $390 (100 SHS)\",2.00000,05/13/2022,05/13/2022,\"$650.62 \",\"$477.36 \",\"$173.26 \",-";
	
//	@Autowired
//	private OptionImportService optionImportService;
	
	@InjectMocks
	private OptionImportService optionImportService = new OptionImportService(accountRepositoryMock);
	
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
	
	private final String lineHeaderHistory1 = "History: Joint WROS - TOD (X123456789)";
	private final String lineHeaderHistory2 = "From: 11/04/2022";
	private final String lineHeaderHistory3 = "To: 11/14/2022";
	private final String lineHeaderHistory4 = "All Activity";
	private final String lineHeaderHistory5 = "11/14/2022 11:56:09 AM ET";
	private final String lineHeaderHistory6 = "";
	private final String lineHeaderHistory7 = "\"Date\",\"Symbol\",\"Security Description\",\"Quantity\",\"Description\",\"Price\",\"Amount\",\"Commission\",\"Fees\",\"Type\",\"Account\"";
//	private final String lineHistoryCallClosingAssigned = "\"11/14/2022\",\"SPY221111C395\",\"CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"1\",\"ASSIGNED\",\"$0.00\",\"$0.00\",\"$0.00\",\"$0.00\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
//	private final String lineHistoryCallOpeningSell = "\"11/10/2022\",\"SPY221111C395\",\"CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"-1\",\"YOU SOLD OPENING TRANSACTION\",\"$0.72\",\"$71.32\",\"$0.65\",\"$0.03\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
//	private final String lineHistoryCallBoughtClosingPurchase = "\"11/09/2022\",\"SPY221109C383\",\"CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22313G3R0H\",\"$0.64\",\"$-64.02\",\"$0.00\",\"$0.02\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
//	private final String lineHistoryCallExpiredWorthless = "\"11/11/2022\",\"SPY221111C395\",\"CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"1\",\"EXPIRED CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"$0.00\",\"$0.00\",\"$0.00\",\"$0.00\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
	
	@Test
	void testHistoryHeader1Through7() throws ParseException {
		
		
		Option option1 = optionImportService.handleLineHistory(lineHeaderHistory1);
		assertThat(option1).isNull();
		
		Option option2 = optionImportService.handleLineHistory(lineHeaderHistory2);
		assertThat(option2).isNull();
		
		Option option3 = optionImportService.handleLineHistory(lineHeaderHistory3);
		assertThat(option3).isNull();
		
		Option option4 = optionImportService.handleLineHistory(lineHeaderHistory4);
		assertThat(option4).isNull();
		
		Option option5 = optionImportService.handleLineHistory(lineHeaderHistory5);
		assertThat(option5).isNull();
		
		Option option6 = optionImportService.handleLineHistory(lineHeaderHistory6);
		assertThat(option6).isNull();
		
		Option option7 = optionImportService.handleLineHistory(lineHeaderHistory7);
		assertThat(option7).isNull();
		
//		testSetUp();
//		
//		Option option8 = optionImportService.handleLineHistory(lineHistoryCallClosingAssigned);
//		assertThat(option8).isNotNull();
//		
//		Option option9 = optionImportService.handleLineHistory(lineHistoryCallOpeningSell);
//		assertThat(option9).isNotNull();
//		
//		Option option10 = optionImportService.handleLineHistory(lineHistoryCallBoughtClosingPurchase);
//		assertThat(option10).isNotNull();
//		
//		Option option11 = optionImportService.handleLineHistory(lineHistoryCallExpiredWorthless);
//		assertThat(option11).isNotNull();
//		
	}
	
//	@Test
//	void testHistoryCallOpeningSell() throws ParseException {
//		
//		testSetUp();
//		
//		Option option = optionImportService.handleLineHistory(lineHistoryCallOpeningSell);
//		assertThat(option).isNotNull();
//		assertThat(option.getDateAcquired().toString()).isEqualTo("2022-11-10");
//		assertThat(option.getQuantity()).isEqualTo(-1.0);
//		assertThat(option.getOptionSymbol()).isEqualTo("SPY221111C395");
//		assertThat(option.getExpirationDate()).isNotNull();
//		assertThat(option.getExpirationDate().toString()).isEqualTo("2022-11-11");
//		assertThat(option.getStrikePrice()).isNotNull();
//		assertThat(option.getStrikePrice()).isEqualTo(395);
//		assertThat(option.getSymbol()).isEqualTo("SPY");
//		assertThat(option.getCostBasis()).isEqualTo(71.32);
//		assertThat(option.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
//		
//	}
//	
//	@Test
//	void testHistoryCallClosingAssignment() throws ParseException {
//	
//		testSetUp();
//		
//		Option option = optionImportService.handleLineHistory(lineHistoryCallClosingAssigned);
//		assertThat(option).isNotNull();
//		assertThat(option.getDateSold().toString()).isEqualTo("2022-11-14");
//		assertThat(option.getQuantity()).isEqualTo(1.0);
//		assertThat(option.getOptionSymbol()).isEqualTo("SPY221111C395");
//		assertThat(option.getExpirationDate()).isNotNull();
//		assertThat(option.getExpirationDate().toString()).isEqualTo("2022-11-11");
//		assertThat(option.getStrikePrice()).isNotNull();
//		assertThat(option.getStrikePrice()).isEqualTo(395);
//		assertThat(option.getSymbol()).isEqualTo("SPY");
//		assertThat(option.getProceeds()).isEqualTo(0.00);
//		assertThat(option.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
//		assertThat(option.getQuantity()).isEqualTo(1.0);
//		
//	}
//	
//	@Test
//	void testHistoryCallClosingPurchase() throws ParseException {
//		
//		testSetUp();
//		
//		Option option = optionImportService.handleLineHistory(lineHistoryCallBoughtClosingPurchase);
//		assertThat(option).isNotNull();
//		assertThat(option.getDateSold().toString()).isEqualTo("2022-11-09");
//		assertThat(option.getOptionSymbol()).isEqualTo("SPY221109C383");
//		assertThat(option.getExpirationDate()).isNotNull();
//		assertThat(option.getExpirationDate().toString()).isEqualTo("2022-11-09");
//		assertThat(option.getStrikePrice()).isNotNull();
//		assertThat(option.getStrikePrice()).isEqualTo(383);
//		assertThat(option.getSymbol()).isEqualTo("SPY");
//		assertThat(option.getProceeds()).isEqualTo(-64.02);
//		assertThat(option.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
//		assertThat(option.getQuantity()).isEqualTo(1.0);
//		
//	}
//	
//	@Test
//	void testHistoryCallExpiredWorthless() throws ParseException {
//		
//		testSetUp();
//		
//		Option option = optionImportService.handleLineHistory(lineHistoryCallExpiredWorthless);
//		assertThat(option).isNotNull();
//		assertThat(option.getDateSold().toString()).isEqualTo("2022-11-11");
//		assertThat(option.getOptionSymbol()).isEqualTo("SPY221111C395");
//		assertThat(option.getExpirationDate()).isNotNull();
//		assertThat(option.getExpirationDate().toString()).isEqualTo("2022-11-11");
//		assertThat(option.getStrikePrice()).isNotNull();
//		assertThat(option.getStrikePrice()).isEqualTo(395);
//		assertThat(option.getSymbol()).isEqualTo("SPY");
//		assertThat(option.getProceeds()).isEqualTo(0.00);
//		assertThat(option.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
//		assertThat(option.getQuantity()).isEqualTo(1.0);
//		
//	}
	
}
