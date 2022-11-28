package com.brummer.investmenttracker.transactions.imports;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.constants.EquityType;
import com.brummer.investmenttracker.transactions.Transaction;


@SpringBootTest
public class TransactionImportFidelityWebSingleAccountTest {

	@Autowired
	private TransactionImportFidelityWebSingleAccount transactionImportFidelityWebSingleAccount;
	
	private final String lineHeaderHistory1 = "";
	@Test
	void lineHeaderHistory1() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineHeaderHistory1);
		assertThat(transaction).isNull();
	}
	
	private final String lineHeaderHistory4 = "Brokerage";
	@Test
	void lineHeaderHistory4() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineHeaderHistory4);
		assertThat(transaction).isNull();
	}

	private final String lineHeaderHistory6 = "Run Date,Action,Symbol,Security Description,Security Type,Quantity,Price ($),Commission ($),Fees ($),Accrued Interest ($),Amount ($),Settlement Date";
	@Test
	void lineHeaderHistory6() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineHeaderHistory6);
		assertThat(transaction).isNull();
	}
	
	private final String lineSoldOpenningPutTransaction = " 11/16/2022, YOU SOLD OPENING TRANSACTION PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS) (Cash), -SPY221118P395, PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS),Cash,-3,2.59,1.95,0.07,,774.98,11/17/2022";
	@Test
	void lineSoldOpenningPutTransaction() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineSoldOpenningPutTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU SOLD OPENING TRANSACTION PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS) (Cash)");
		assertThat(transaction.getAmount()).isEqualTo(774.98);
		assertThat(transaction.getQuantity()).isEqualTo(-3.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("PUT (SPY) SPDR S&P500 ETF NOV 18 22 $395 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221118P395");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-16");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineSoldOpenningCallTransaction = " 11/16/2022, YOU SOLD OPENING TRANSACTION CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS) (Margin), -SPY221118C400, CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS),Margin,-1,1.63,0.65,0.03,,162.32,11/17/2022";
	@Test
	void lineSoldOpenningCallTransaction() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineSoldOpenningCallTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU SOLD OPENING TRANSACTION CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS) (Margin)");
		assertThat(transaction.getAmount()).isEqualTo(162.32);
		assertThat(transaction.getQuantity()).isEqualTo(-1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 18 22 $400 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221118C400");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-16");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineBoughtClosingPutTransaction = " 11/15/2022, YOU BOUGHT CLOSING TRANSACTION 22319G50EI PUT (SPY) SPDR S&P500 ETF NOV 16 22 $395 (100 SHS) (Cash), -SPY221116P395, PUT (SPY) SPDR S&P500 ETF NOV 16 22 $395 (100 SHS),Cash,3,0.7,1.95,0.05,,-212,11/16/2022";
	@Test
	void lineBoughtClosingPutTransaction() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineBoughtClosingPutTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT CLOSING TRANSACTION 22319G50EI PUT (SPY) SPDR S&P500 ETF NOV 16 22 $395 (100 SHS) (Cash)");
		assertThat(transaction.getAmount()).isEqualTo(-212);
		assertThat(transaction.getQuantity()).isEqualTo(3.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("PUT (SPY) SPDR S&P500 ETF NOV 16 22 $395 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221116P395");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-15");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineBoughtClosingCallTransaction = " 11/09/2022, YOU BOUGHT CLOSING TRANSACTION 22313G3R0H CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS) (Margin), -SPY221109C383, CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS),Margin,1,0.64,,0.02,,-64.02,11/10/2022";
	@Test
	void lineBoughtClosingCallTransaction() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineBoughtClosingCallTransaction);
		assertThat(transaction).isNotNull();
		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT CLOSING TRANSACTION 22313G3R0H CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS) (Margin)");
		assertThat(transaction.getAmount()).isEqualTo(-64.02);
		assertThat(transaction.getQuantity()).isEqualTo(1.0);
		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS)");
		assertThat(transaction.getSymbol()).isEqualTo("SPY221109C383");
		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-09");
		assertThat(transaction.getEquityType()).isEqualTo(EquityType.OPTION.getValue());
	}
	
	private final String lineAssignedClosingPutTransaction = " 09/06/2022, YOU BOUGHT ASSIGNED PUTS AS OF 09-02-22 SPDR S&P500 ETF TRUST TRUST UNIT DEP... (SPY) (Margin), SPY, SPDR S&P500 ETF TRUST TRUST UNIT DEPOSI,Margin,100,393,,,,-39300,09/07/2022";
	@Test
	void lineAssignedClosingPutTransaction() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineAssignedClosingPutTransaction);
		assertThat(transaction).isNull();
//		assertThat(transaction.getAction()).isEqualTo("YOU BOUGHT ASSIGNED PUTS AS OF 09-02-22 SPDR S&P500 ETF TRUST TRUST UNIT DEP... (SPY) (Margin)");
//		assertThat(transaction.getAmount()).isEqualTo(-64.02);
//		assertThat(transaction.getQuantity()).isEqualTo(1.0);
//		assertThat(transaction.getSecurityDescription()).isEqualTo("CALL (SPY) SPDR S&P500 ETF NOV 09 22 $383 (100 SHS)");
//		assertThat(transaction.getSymbol()).isEqualTo("SPY221109C383");
//		assertThat(transaction.getTransactionDate().toString()).isEqualTo("2022-11-09");
	}
	
	private final String lineAssignedClosingCallTransaction = " 11/14/2022, ASSIGNED as of 11/11/2022 CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS) (Margin), -SPY221111C395, CALL (SPY) SPDR S&P500 ETF NOV 11 22 $395 (100 SHS),Margin,1,,,,,,";
	@Test
	void lineAssignedClosingCallTransaction() throws ParseException {
		Transaction transaction = transactionImportFidelityWebSingleAccount.parseLine(lineAssignedClosingCallTransaction);
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
//	private final String lineExpiredWorthlessClosingCallTransaction = " 09/26/2022,Joint WROS - TOD X30097152, EXPIRED CALL (SPY) SPDR S&P500 ETF SEP 23 22 $396 as of 09/23/2022 CALL (SPY) SPDR S&P500 ETF SEP 23 22 $396 (100 SHS) (Margin), -SPY220923C396, CALL (SPY) SPDR S&P500 ETF SEP 23 22 $396 (100 SHS),Margin,-1,,,,,,";
	
	
	
}

