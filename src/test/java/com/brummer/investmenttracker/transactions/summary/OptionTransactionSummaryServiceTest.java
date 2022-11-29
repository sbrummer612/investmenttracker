package com.brummer.investmenttracker.transactions.summary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.transactions.Transaction;
import com.brummer.investmenttracker.transactions.imports.TransactionImportUtilTest;
import com.brummer.investmenttracker.transactions.imports.TransactionImporterFidelityATPFile;
import com.brummer.investmenttracker.transactions.options.summary.OptionTransactionSummary;
import com.brummer.investmenttracker.transactions.options.summary.OptionTransactionSummaryService;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

@SpringBootTest
public class OptionTransactionSummaryServiceTest extends TransactionImportUtilTest {
	
	@InjectMocks
	private TransactionImporterFidelityATPFile transactionImporterFidelityATPFile = new TransactionImporterFidelityATPFile(accountRepositoryMock);
	
	@Autowired
	private OptionTransactionSummaryService transactionSummaryService;

	@Test
	public void testOneSummary() throws ParseException {
		testSetUp();
		
		String lineClosing = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"3\",\"YOU BOUGHT CLOSING TRANSACTION 22304HTW04\",\"$1.91\",\"$-575.04\",\"$1.95\",\"$0.09\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String lineOpening = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"-3\",\"YOU SOLD OPENING TRANSACTION\",\"$2.74\",\"$819.94\",\"$1.95\",\"$0.11\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		
		Transaction t1 = transactionImporterFidelityATPFile.parseLine(lineClosing);
		Transaction t2 = transactionImporterFidelityATPFile.parseLine(lineOpening);
		List<Transaction> transactions = Arrays.asList(t1, t2);
		List<OptionTransactionSummary> summaries = transactionSummaryService.summarizeTransactions(transactions, null, null);
		OptionTransactionSummary test = summaries.get(0);
		
		assertThat(test.getOptionSymbol()).isEqualTo("SPY221102P382");
		assertThat(test.getStockSymbol()).isEqualTo("SPY");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateAcquired().toString()).isEqualTo("2022-10-31");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateClosed()).isEqualTo("2022-10-31");
		assertThat(test.getQuantity()).isEqualTo(0.00);
		assertThat(test.getExpirationDate()).isEqualTo("2022-11-02");
		assertThat(test.getStrikePrice()).isEqualTo(382);
		assertThat(test.getCostBasis()).isEqualTo(819.94);
		assertThat(test.getProceeds()).isEqualTo(-575.04);
		assertThat(test.getGainLoss()).isEqualTo(244.90);
		assertThat(test.getReturnPercentage()).isEqualTo(0.21);
		assertThat(test.getReturnPercentageAnnualized()).isEqualTo(76.65);
		
	}
	
	@Test
	public void testTwoSummary() throws ParseException {
		
		testSetUp();
	
		String lineOpening = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"-3\",\"YOU SOLD OPENING TRANSACTION\",\"$2.74\",\"$819.94\",\"$1.95\",\"$0.11\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String lineClosing1 = "\"11/01/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22305G4F92\",\"$1.19\",\"$-119.67\",\"$0.65\",\"$0.02\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String lineClosing2 = "\"11/01/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22305G4F92\",\"$1.19\",\"$-119.67\",\"$0.65\",\"$0.02\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String lineClosing3 = "\"11/01/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22305G4F92\",\"$1.19\",\"$-119.67\",\"$0.65\",\"$0.02\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		
		
		Transaction t1 = transactionImporterFidelityATPFile.parseLine(lineOpening);
		Transaction t2 = transactionImporterFidelityATPFile.parseLine(lineClosing1);
		Transaction t3 = transactionImporterFidelityATPFile.parseLine(lineClosing2);
		Transaction t4 = transactionImporterFidelityATPFile.parseLine(lineClosing3);
		
		List<Transaction> transactions = Arrays.asList(t1, t2, t3, t4);
		
		List<OptionTransactionSummary> summaries = transactionSummaryService.summarizeTransactions(transactions, null, null);
		
		OptionTransactionSummary test = summaries.get(0);

		assertThat(test.getOptionSymbol()).isEqualTo("SPY221102P382");
		assertThat(test.getStockSymbol()).isEqualTo("SPY");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateAcquired().toString()).isEqualTo("2022-10-31");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateClosed()).isEqualTo("2022-11-01");
		assertThat(test.getQuantity()).isEqualTo(0.00);
		assertThat(test.getExpirationDate()).isEqualTo("2022-11-02");
		assertThat(test.getStrikePrice()).isEqualTo(382);
		assertThat(test.getCostBasis()).isEqualTo(819.94);
		assertThat(test.getProceeds()).isEqualTo(-359.01);
		assertThat(test.getGainLoss()).isEqualTo(460.93);
		assertThat(test.getReturnPercentage()).isEqualTo(0.40);
		assertThat(test.getReturnPercentageAnnualized()).isEqualTo(146.0);
		
	}

	@Test
	public void testOpenCall() throws ParseException {
		
		testSetUp();
		String lineOpenCall = "\"10/17/2022\",\"SPY221021P355\",\"PUT (SPY) SPDR S&P500 ETF OCT 21 22 $355 (100 SHS)\",\"-1\",\"YOU SOLD OPENING TRANSACTION\",\"$1.45\",\"$145.68\",\"$0.65\",\"$0.03\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";

		Transaction t1 = transactionImporterFidelityATPFile.parseLine(lineOpenCall);
		List<Transaction> transactions = Arrays.asList(t1);
		List<OptionTransactionSummary> summaries = transactionSummaryService.summarizeTransactions(transactions, null, null);
		
		OptionTransactionSummary test = summaries.get(0);

		assertThat(test.getOptionSymbol()).isEqualTo("SPY221021P355");
		assertThat(test.getStockSymbol()).isEqualTo("SPY");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateAcquired().toString()).isEqualTo("2022-10-17");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateClosed()).isNull();;
		assertThat(test.getQuantity()).isEqualTo(-1.00);
		assertThat(test.getExpirationDate()).isEqualTo("2022-10-21");
		assertThat(test.getStrikePrice()).isEqualTo(355);
		assertThat(test.getCostBasis()).isEqualTo(145.68);
		assertThat(test.getProceeds()).isNull();
		assertThat(test.getGainLoss()).isEqualTo(145.68);
		assertThat(test.getReturnPercentage()).isEqualTo(0.41);
		assertThat(test.getReturnPercentageAnnualized()).isEqualTo(37.31);
		
		
	}
	
	@Test
	public void testMutliOpen() throws ParseException {
		testSetUp();
		String line1 = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"-3\",\"YOU SOLD OPENING TRANSACTION\",\"$1.94\",\"$579.94\",\"$1.95\",\"$0.11\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String line2 = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"-3\",\"YOU SOLD OPENING TRANSACTION\",\"$2.74\",\"$819.94\",\"$1.95\",\"$0.11\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		
		Transaction t1 = transactionImporterFidelityATPFile.parseLine(line1);
		Transaction t2 = transactionImporterFidelityATPFile.parseLine(line2);
		List<Transaction> transactions = Arrays.asList(t1, t2);
		List<OptionTransactionSummary> summaries = transactionSummaryService.summarizeTransactions(transactions, null, null);
		
		OptionTransactionSummary test = summaries.get(0);

		assertThat(test.getOptionSymbol()).isEqualTo("SPY221102P382");
		assertThat(test.getStockSymbol()).isEqualTo("SPY");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateAcquired().toString()).isEqualTo("2022-10-31");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateClosed()).isNull();
		assertThat(test.getQuantity()).isEqualTo(-6.00);
		assertThat(test.getExpirationDate()).isEqualTo("2022-11-02");
		assertThat(test.getStrikePrice()).isEqualTo(382);
		assertThat(test.getCostBasis()).isEqualTo(1399.88);
		assertThat(test.getProceeds()).isNull();
		assertThat(test.getGainLoss()).isEqualTo(1399.88);
		assertThat(test.getReturnPercentage()).isEqualTo(0.61);
		assertThat(test.getReturnPercentageAnnualized()).isEqualTo(111.02);
		
	}
	
	@Test
	public void testMutliClosed() throws ParseException {
		testSetUp();
		String lineA = "\"11/01/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22305G4F92\",\"$1.19\",\"$-119.67\",\"$0.65\",\"$0.02\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String lineB = "\"11/01/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22305G4F92\",\"$1.19\",\"$-119.67\",\"$0.65\",\"$0.02\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String lineC = "\"11/01/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"1\",\"YOU BOUGHT CLOSING TRANSACTION 22305G4F92\",\"$1.19\",\"$-119.67\",\"$0.65\",\"$0.02\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String lineD = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"3\",\"YOU BOUGHT CLOSING TRANSACTION 22304HTW04\",\"$1.91\",\"$-575.04\",\"$1.95\",\"$0.09\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String line1 = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"-3\",\"YOU SOLD OPENING TRANSACTION\",\"$1.94\",\"$579.94\",\"$1.95\",\"$0.11\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		String line2 = "\"10/31/2022\",\"SPY221102P382\",\"PUT (SPY) SPDR S&P500 ETF NOV 02 22 $382 (100 SHS)\",\"-3\",\"YOU SOLD OPENING TRANSACTION\",\"$2.74\",\"$819.94\",\"$1.95\",\"$0.11\",\"Cash\",\"Joint WROS - TOD (X30097152)\"";
		
		Transaction t1 = transactionImporterFidelityATPFile.parseLine(lineA);
		Transaction t2= transactionImporterFidelityATPFile.parseLine(lineB);
		Transaction t3 = transactionImporterFidelityATPFile.parseLine(lineC);
		Transaction t4 = transactionImporterFidelityATPFile.parseLine(lineD);
		
		Transaction t5 = transactionImporterFidelityATPFile.parseLine(line1);
		Transaction t6 = transactionImporterFidelityATPFile.parseLine(line2);
		List<Transaction> transactions = Arrays.asList(t1, t2, t3, t4, t5, t6);
		List<OptionTransactionSummary> summaries = transactionSummaryService.summarizeTransactions(transactions, null, null);
		
		OptionTransactionSummary test = summaries.get(0);

		assertThat(test.getOptionSymbol()).isEqualTo("SPY221102P382");
		assertThat(test.getStockSymbol()).isEqualTo("SPY");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateAcquired().toString()).isEqualTo("2022-10-31");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateClosed()).isNotNull();
		assertThat(test.getDateClosed().toString()).isEqualTo("2022-11-01");
		assertThat(test.getQuantity()).isEqualTo(0.00);
		assertThat(test.getExpirationDate()).isEqualTo("2022-11-02");
		assertThat(test.getStrikePrice()).isEqualTo(382);
		assertThat(test.getCostBasis()).isEqualTo(1399.88);
		assertThat(test.getProceeds()).isEqualTo(-934.05);
		assertThat(test.getGainLoss()).isEqualTo(465.83);
		assertThat(test.getReturnPercentage()).isEqualTo(0.2);
		assertThat(test.getReturnPercentageAnnualized()).isEqualTo(73.0);
		
	}
	
	@Test
	public void testLongPutTwoLineClosed() throws ParseException {
		testSetUp();
		String lineA = "\"06/01/2022\",\"SPY220608P403\",\"PUT (SPY) SPDR S&P500 ETF JUN 08 22 $403 (100 SHS)\",\"2\",\"YOU BOUGHT OPENING TRANSACTION\",\"$3.05\",\"$-611.36\",\"$1.30\",\"$0.06\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
		String lineB = "\"06/01/2022\",\"SPY220608P403\",\"PUT (SPY) SPDR S&P500 ETF JUN 08 22 $403 (100 SHS)\",\"-2\",\"YOU SOLD CLOSING TRANSACTION 22152HNR6L\",\"$3.72\",\"$742.62\",\"$1.30\",\"$0.08\",\"Margin\",\"Joint WROS - TOD (X30097152)\"";
		
		Transaction t1 = transactionImporterFidelityATPFile.parseLine(lineA);
		Transaction t2 = transactionImporterFidelityATPFile.parseLine(lineB);
		
		List<Transaction> transactions = Arrays.asList(t1, t2);
		List<OptionTransactionSummary> summaries = transactionSummaryService.summarizeTransactions(transactions, null, null);
		
		OptionTransactionSummary test = summaries.get(0);

		assertThat(test.getOptionSymbol()).isEqualTo("SPY220608P403");
		assertThat(test.getStockSymbol()).isEqualTo("SPY");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateAcquired().toString()).isEqualTo("2022-06-01");
		assertThat(test.getDateAcquired()).isNotNull();
		assertThat(test.getDateClosed()).isNotNull();
		assertThat(test.getDateClosed().toString()).isEqualTo("2022-06-01");
		assertThat(test.getQuantity()).isEqualTo(0.00);
		assertThat(test.getExpirationDate()).isEqualTo("2022-06-08");
		assertThat(test.getStrikePrice()).isEqualTo(403);
		assertThat(test.getCostBasis()).isEqualTo(-611.36);
		assertThat(test.getProceeds()).isEqualTo(742.62);
		assertThat(test.getGainLoss()).isEqualTo(131.26);
		assertThat(test.getReturnPercentage()).isEqualTo(0.16);
		assertThat(test.getReturnPercentageAnnualized()).isEqualTo(58.4);
		
	}
	
	
}
