package com.brummer.investmenttracker.trades;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brummer.investmenttracker.options.Option;

@SpringBootTest
public class TradeTransientFieldsTests {

	@Autowired
	private TradeTransientFields tradeTransientFields;  
	
	@Test
	void contextLoads() {
	}
	
	
	/**
	 * Test of one call that that is bought back at sold date.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testOneCallBoughtBackAtSoldDateSameDay() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("197.31");
		Double costBasis = Double.valueOf("146.68");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(1.00000);
		option1.setStrikePrice(370.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("10/18/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("10/21/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("10/18/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.13683783783783782);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.49945810810810803);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of one call that that is bought back at sold date.
	 * 
	 * @throws ParseException
	 */
//	@Test
//	void testQtyOneLessDateSoldUseExpirationForAnnualizedReturn() throws ParseException {
//		
//		Trade trade = new Trade();
//		trade.setCostBasis(350.0);
//		List<Option> options = new ArrayList<Option>();
//		List<Trade> trades = new ArrayList<Trade>();
//		trades.add(trade);
//		
//		Option option1 = new Option();
//		
//		Double proceeds = Double.valueOf("100.00");
//		Double costBasis = Double.valueOf("25.00");
//		
//		option1.setProceeds(proceeds);
//		option1.setCostBasis(costBasis);
//		option1.setQuantity(1.00000);
//		option1.setStrikePrice(390.0);
//		option1.setOptionType("C");
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//		
//		java.util.Date expirationDate = sdf.parse("11/04/2022");
//		option1.setExpirationDate(new Date(expirationDate.getTime()));
//		
//		java.util.Date dateAcquired = sdf.parse("10/31/2022");
//		option1.setDateAcquired(new Date(dateAcquired.getTime()));
//		
//		options.add(option1);
//		trade.setOptions(options);
//		tradeTransientFields.computeTransientFields(trades);
//		
//		assertThat(option1.getReturnPercentage()).isEqualTo(0.19230769230769232);
//		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.175);
//		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
//		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
//		
//		
//	}
	
	/**
	 * Test of one Call that expires worthless.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testOneCallThatExpiresWorthless() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("197.31");
		Double costBasis = Double.valueOf("0.00");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(1.00000);
		option1.setStrikePrice(377.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("10/18/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("10/21/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("10/21/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.523368700265252);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.6332761273209548);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of multiple Calls (two) that were bought back.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testMutipleCallsTwoBoughtBackAtSoldDate() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("154.63");
		Double costBasis = Double.valueOf("74.06");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(2.00000);
		option1.setStrikePrice(370.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("10/01/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("10/03/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("9/30/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.10887837837837837);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.39740608108108105);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of multiple Calls (two) that expired worthless.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testMutipleCallsTwoExpiredWorthless() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("154.63");
		Double costBasis = Double.valueOf("0.00");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(2.00000);
		option1.setStrikePrice(370.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("10/03/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("10/03/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("9/30/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.20895945945945946);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.25284094594594597);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of multiple Calls (three) that expire worthless.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testMutipleCallsThreeBoughtBackAtSoldDate() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("147.68");
		Double costBasis = Double.valueOf("9.00");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(3.00000);
		option1.setStrikePrice(396.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("09/22/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("09/23/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("9/21/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.11673400673400675);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.4260791245791246);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of multiple Calls (three) that expire worthless.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testMutipleCallThreeThatExpireWorthless() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("147.68");
		Double costBasis = Double.valueOf("0.00");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(3.00000);
		option1.setStrikePrice(396.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("09/23/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("09/23/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("9/21/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.12430976430976431);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.22624377104377108);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of one Call that expires worthless.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testCallQtyOneUseExpiredDateAsNullToUseExpirationForAnnualizedReturn() throws ParseException {
		
		// one 
		
		Trade trade = new Trade();
		trade.setCostBasis(390.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Option option1 = new Option();
		
		Double proceeds = Double.valueOf("197.31");
		Double costBasis = Double.valueOf("0.00");
		
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(1.00000);
		option1.setStrikePrice(377.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		java.util.Date expirationDate = sdf.parse("10/21/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("10/18/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
//		java.util.Date dateSold = sdf.parse("10/19/2022");
//		option1.setDateSold(new Date(dateSold.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.523368700265252);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.6332761273209548);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	
	/**
	 * Test of one Call that is bought back.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testCallQtyOneUseDateSoldNotUseExpirationForAnnualizedReturn() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(390.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Option option1 = new Option();
		
		Double proceeds = Double.valueOf("197.31");
		Double costBasis = Double.valueOf("146.68");
		
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(1.00000);
		option1.setStrikePrice(377.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		java.util.Date expirationDate = sdf.parse("10/21/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("10/18/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		java.util.Date dateSold = sdf.parse("10/19/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.1342970822281167);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.49018435013262596);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
		
	}
	
	/**
	 * Test of multiple Calls (two) that are bought back.
	 * 
	 * @throws ParseException
	 */
	
	@Test
	void testAllQtyTwoFields() throws ParseException {

		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("310.00");
		Double costBasis = Double.valueOf("55.35");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(2.00000);
		option1.setStrikePrice(411.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("08/29/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("09/02/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("8/30/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.3097931873479319);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(1.1307451338199512);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of one PUT that that is bought back at sold date.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testOnePutBoughtBackAtSoldDate() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("193.31");
		Double costBasis = Double.valueOf("119.67");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(1.00000);
		option1.setStrikePrice(382.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("10/31/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("11/02/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("11/01/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.19277486910994765);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.7036282722513089);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of one PUT that that expires worthless.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testOnePutThatExpiresWorthless() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("193.31");
		Double costBasis = Double.valueOf("0.00");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(1.00000);
		option1.setStrikePrice(382.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("11/02/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("11/02/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("10/31/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.5060471204188481);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.9210057591623035);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of mutiple PUTs (three) that are bought back.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testThreePutBoughtBackAtSoldDate() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(380.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("939.97");
		Double costBasis = Double.valueOf("713.00");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(3.00000);
		option1.setStrikePrice(380.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("11/01/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("11/04/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("11/01/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.1990964912280702);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.7267021929824561);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	/**
	 * Test of mutiple PUTs (three) that expire worthless.
	 * 
	 * @throws ParseException
	 */
	@Test
	void testThreePutThatExpireWorthless() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(380.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Double proceeds = Double.valueOf("939.97");
		Double costBasis = Double.valueOf("0.00");
		
		Option option1 = new Option();
		option1.setProceeds(proceeds);
		option1.setCostBasis(costBasis);
		option1.setQuantity(3.00000);
		option1.setStrikePrice(380.0);
		option1.setOptionType("C");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("11/04/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("11/04/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("11/01/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.8245350877192983);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.9976874561403509);
		assertThat(option1.getTotalProfit()).isEqualTo(proceeds - costBasis);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
}
