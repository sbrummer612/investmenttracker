package com.brummer.investmenttracker.transactions.imports;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.text.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.constants.EquityType;
import com.brummer.investmenttracker.transactions.Transaction;

@SpringBootTest
public class TransactionImporterFidelityATPFileTest extends TransactionImportUtilTest {

	@InjectMocks
	private TransactionImporterFidelityATPFile transactionImporterFidelityATPFile = new TransactionImporterFidelityATPFile(accountRepositoryMock);
	
	private final String lineHeaderHistory1 = "History: Joint WROS - TOD (X123456789)";
	@Test
	void lineHeaderHistory1() throws ParseException, IOException {
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHeaderHistory1);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory2 = "From: 11/04/2022";
	@Test
	void lineHeaderHistory2() throws ParseException, IOException {
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHeaderHistory2);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory3 = "To: 11/14/2022";
	@Test
	void lineHeaderHistory3() throws ParseException, IOException {
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHeaderHistory3);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory4 = "All Activity";
	@Test
	void lineHeaderHistory4() throws ParseException, IOException {
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHeaderHistory4);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory5 = "11/14/2022 11:56:09 AM ET";
	@Test
	void lineHeaderHistory5() throws ParseException, IOException {
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHeaderHistory5);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory6 = "";
	@Test
	void lineHeaderHistory6() throws ParseException, IOException {
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHeaderHistory6);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory7 = "\"Date\",\"Symbol\",\"Security Description\",\"Quantity\",\"Description\",\"Price\",\"Amount\",\"Commission\",\"Fees\",\"Type\",\"Account\"";
	@Test
	void lineHeaderHistory7() throws ParseException, IOException {
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHeaderHistory7);
		assertThat(transaction).isNull();
	}
	
	private final String lineHistoryCallClosingAssigned = "\"11/14/2022\",\"SPY221111C395\",\"CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"1\",\"ASSIGNED\",\"$0.00\",\"$0.00\",\"$0.00\",\"$0.00\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
	@Test
	void lineHistoryCallClosingAssigned() throws ParseException, IOException {

		testSetUp();
		
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHistoryCallClosingAssigned);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
		assertThat(transaction.getAction()).isEqualTo("ASSIGNED");
		assertThat(transaction.getAmount()).isEqualTo(0.00);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221111C395");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-14");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
		
			
	}
	
	private final String lineHistoryCallOpeningSell = "\"11/10/2022\",\"SPY221111C395\",\"CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"-1\",\"YOU SOLD OPENING TRANSACTION\",\"$0.72\",\"$71.32\",\"$0.65\",\"$0.03\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
	@Test
	void lineHistoryCallOpeningSell() throws ParseException, IOException {
		
		testSetUp();
		
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHistoryCallOpeningSell);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
		assertThat(transaction.getAction()).isEqualTo("YOU SOLD OPENING TRANSACTION");
		assertThat(transaction.getAmount()).isEqualTo(71.32);
		assertThat(transaction.getQuantity()).isEqualTo(-1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221111C395");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-10");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineHistoryCallBoughtClosingPurchase = "\"11/09/2022\",\"SPY221109C383\",\"CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22313G3R0H\",\"$0.64\",\"$-64.02\",\"$0.00\",\"$0.02\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
	@Test
	void lineHistoryCallBoughtClosingPurchase() throws ParseException, IOException {
		
		testSetUp();
		
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHistoryCallBoughtClosingPurchase);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT CLOSING TRANSACTION 22313G3R0H");
		assertThat(transaction.getAmount()).isEqualTo(-64.02);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221109C383");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-09");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineHistoryCallExpiredWorthless = "\"11/11/2022\",\"SPY221111C395\",\"CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"1\",\"EXPIRED CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)\",\"$0.00\",\"$0.00\",\"$0.00\",\"$0.00\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
	@Test
	void lineHistoryCallExpiredWorthless() throws ParseException, IOException {
		
		testSetUp();
		
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineHistoryCallExpiredWorthless);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
		assertThat(transaction.getAction()).isEqualTo("EXPIRED CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)");
		assertThat(transaction.getAmount()).isEqualTo(0.00);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221111C395");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-11");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
		
	}
	
	private final String lineStockWithCommaInAmount = "\"11/15/2022\",\"TS\",\"TENARIS S.A. SPONS ADS EACH REP 2 ORD SHS\",\"100\",\"YOU BOUGHT\",\"$34.655\",\"$-3,465.50\",\"$0.00\",\"$0.00\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
	@Test
	void lineStockWithCommaInAmount() {
		
		testSetUp();
		
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineStockWithCommaInAmount);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAccount().getName()).isEqualTo("Joint WROS - TOD X30097152");
		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT");
		assertThat(transaction.getAmount()).isEqualTo(-3465.50);
		assertThat(transaction.getQuantity()).isEqualTo(100.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("TENARIS S.A. SPONS ADS EACH REP 2 ORD SHS");
		assertThat(transaction.getSymbol()).isEqualTo("TS");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-15");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.STOCK.getValue());
		
	}
	
	private final String lineWithOutSymbol = "\"11/02/2022\",\"\",\"\",\"0\",\"MONEY LINE PAID EFT FUNDS PAID ED82243781 /WEB\",\"$0.00\",\"$-4,000.00\",\"$0.00\",\"$0.00\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
	@Test
	void lineWithOutSymbol() {
		
		testSetUp();
		Transaction transaction = transactionImporterFidelityATPFile.parseLine(lineWithOutSymbol);
		assertThat(transaction).isNull();
		
	}
	
	
}
