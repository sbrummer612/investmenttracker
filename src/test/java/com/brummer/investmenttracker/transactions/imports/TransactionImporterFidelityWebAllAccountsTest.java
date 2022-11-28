package com.brummer.investmenttracker.transactions.imports;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.constants.EquityType;
import com.brummer.investmenttracker.transactions.Transaction;

@SpringBootTest
public class TransactionImporterFidelityWebAllAccountsTest extends TransactionImportUtilTest {

	@InjectMocks
	private TransactionImporterFidelityWebAllAccounts transactionImportFidelityWebAllAccounts = new TransactionImporterFidelityWebAllAccounts(accountRepositoryMock);
	
	private final String lineHeaderHistory1 = "";
	@Test
	void lineHeaderHistory1() throws ParseException {
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineHeaderHistory1);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory4 = "Brokerage";
	@Test
	void lineHeaderHistory4() throws ParseException {
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineHeaderHistory4);
		assertThat(transaction).isNull();
	}

	private final String lineHeaderHistory6 = "Run Date,Account,Action,Symbol,Security Description,Security Type,Quantity,Price ($),Commission ($),Fees ($),Accrued Interest ($),Amount ($),Settlement Date";
	@Test
	void lineHeaderHistory6() throws ParseException {
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineHeaderHistory6);
		assertThat(transaction).isNull();
	}
	
	private final String linesoldOpenningCallTransaction = " 11/16/2022,Joint WROS - TOD X30097152, YOU SOLD OPENING TRANSACTION CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS) (Margin), -SPY221118C400, CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS),Margin,-1,1.68,0.65,0.03,,167.32,11/17/2022";
	@Test
	void linesoldOpenningCallTransaction() throws ParseException {
		
		testSetUp();
		
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(linesoldOpenningCallTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU SOLD OPENING TRANSACTION CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS) (Margin)");
		assertThat(transaction.getAmount()).isEqualTo(167.32);
		assertThat(transaction.getQuantity()).isEqualTo(-1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221118C400");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-16");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineSoldOpenningPutTransaction = " 11/16/2022,Joint WROS - TOD X30097152, YOU SOLD OPENING TRANSACTION PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS) (Cash), -SPY221118P395, PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS),Cash,-3,2.59,1.95,0.07,,774.98,11/17/2022";
	@Test
	void lineSoldOpenningPutTransaction() throws ParseException {
		testSetUp();
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineSoldOpenningPutTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU SOLD OPENING TRANSACTION PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS) (Cash)");
		assertThat(transaction.getAmount()).isEqualTo(774.98);
		assertThat(transaction.getQuantity()).isEqualTo(-3.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221118P395");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-16");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineBoughtClosingPutTransaction = " 11/16/2022,Joint WROS - TOD X30097152, YOU BOUGHT CLOSING TRANSACTION 22320H4GOD PUT (SPY) SPDR S&P500 ETF NOV 18 22 $394 (100 SHS) (Cash), -SPY221118P394, PUT (SPY) SPDR S&P500 ETF NOV 18 22 $394 (100 SHS),Cash,1,2.32,0.65,0.02,,-232.67,11/17/2022";
	@Test
	void lineBoughtClosingPutTransaction() throws ParseException {
		testSetUp();
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineBoughtClosingPutTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT CLOSING TRANSACTION 22320H4GOD PUT (SPY) SPDR S&P500 ETF NOV 18 22 $394 (100 SHS) (Cash)");
		assertThat(transaction.getAmount()).isEqualTo(-232.67);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("PUT (SPY) SPDR S&P500 ETF NOV 18 22 $394 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221118P394");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-16");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	
	private final String lineBoughtClosingCallTransaction = " 11/16/2022,Joint WROS - TOD X30097152, YOU BOUGHT CLOSING TRANSACTION 22320G6VE5 CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS) (Margin), -SPY221118C400, CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS),Margin,1,1.77,0.65,0.02,,-177.67,11/17/2022";
	@Test
	void lineBoughtClosingCallTransaction() throws ParseException {
		testSetUp();
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineBoughtClosingCallTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT CLOSING TRANSACTION 22320G6VE5 CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS) (Margin)");
		assertThat(transaction.getAmount()).isEqualTo(-177.67);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221118C400");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-16");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
//	private final String lineAssignedClosingPutTransaction = "";
	private final String lineAssignedClosingCallTransaction = " 11/14/2022,Joint WROS - TOD X30097152, ASSIGNED as of 11/11/2022 CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS) (Margin), -SPY221111C395, CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS),Margin,1,,,,,,";
	@Test
	void lineAssignedClosingCallTransaction() throws ParseException {
		testSetUp();
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineAssignedClosingCallTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("ASSIGNED as of 11/11/2022 CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS) (Margin)");
		assertThat(transaction.getAmount()).isEqualTo(0.00);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221111C395");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-14");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
//	private final String lineExpiredWorthlessClosingPutTransaction = "";
	private final String lineExpiredWorthlessClosingCallTransaction = " 10/24/2022,Joint WROS - TOD X30097152, EXPIRED CALL (DVY) ISHARES SELECT OCT 21 22 $115 as of 10/21/2022 CALL (DVY) ISHARES SELECT OCT 21 22 $115 (100 SHS) (Cash), -DVY221021C115, CALL (DVY) ISHARES SELECT OCT 21 22 $115 (100 SHS),Cash,1,,,,,,";
	@Test
	void lineExpiredWorthlessClosingCallTransaction() throws ParseException {
		testSetUp();
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineExpiredWorthlessClosingCallTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("EXPIRED CALL (DVY) ISHARES SELECT OCT 21 22 $115 as of 10/21/2022 CALL (DVY) ISHARES SELECT OCT 21 22 $115 (100 SHS) (Cash)");
		assertThat(transaction.getAmount()).isEqualTo(0.00);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (DVY) ISHARES SELECT OCT 21 22 $115 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("DVY221021C115");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-10-24");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineStockPurchase = " 11/15/2022,Joint WROS - TOD X30097152, YOU BOUGHT TENARIS S.A. SPONS ADS EACH REP 2 OR... (TS) (Margin), TS, TENARIS S.A. SPONS ADS EACH REP 2 ORD S,Margin,40,34.66,,,,-1386.23,11/17/2022";
	@Test
	void lineStockPurchase() throws ParseException {
		testSetUp();
		Transaction transaction = transactionImportFidelityWebAllAccounts.parseLine(lineStockPurchase);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT TENARIS S.A. SPONS ADS EACH REP 2 OR... (TS) (Margin)");
		assertThat(transaction.getAmount()).isEqualTo(-1386.23);
		assertThat(transaction.getQuantity()).isEqualTo(40.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("TENARIS S.A. SPONS ADS EACH REP 2 ORD S");
		assertThat(transaction.getSymbol()).isEqualTo("TS");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-15");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.STOCK.getValue());
	}
	
}
