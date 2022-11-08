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
	
	@Test
	void testAllFields() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Option option1 = new Option();
		option1.setProceeds(Double.valueOf("100.00"));
		option1.setCostBasis(Double.valueOf("25.00"));
		option1.setQuantity(1.00000);
		option1.setStrikePrice(390.0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateSold = sdf.parse("11/01/2022");
		option1.setDateSold(new Date(dateSold.getTime()));
		
		java.util.Date expirationDate = sdf.parse("11/04/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("10/31/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.19230769230769232);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.701923076923077);
		assertThat(option1.getTotalProfit()).isEqualTo(75.00);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
	}
	
	@Test
	void testLessDateSoldUseExpirationForAnnualizedReturn() throws ParseException {
		
		Trade trade = new Trade();
		trade.setCostBasis(350.0);
		List<Option> options = new ArrayList<Option>();
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(trade);
		
		Option option1 = new Option();
		option1.setProceeds(Double.valueOf("100.00"));
		option1.setCostBasis(Double.valueOf("25.00"));
		option1.setQuantity(1.00000);
		option1.setStrikePrice(390.0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		java.util.Date expirationDate = sdf.parse("11/04/2022");
		option1.setExpirationDate(new Date(expirationDate.getTime()));
		
		java.util.Date dateAcquired = sdf.parse("10/31/2022");
		option1.setDateAcquired(new Date(dateAcquired.getTime()));
		
		options.add(option1);
		trade.setOptions(options);
		tradeTransientFields.computeTransientFields(trades);
		
		assertThat(option1.getReturnPercentage()).isEqualTo(0.19230769230769232);
		assertThat(option1.getReturnPercentageAnnualized()).isEqualTo(0.175);
		assertThat(option1.getTotalProfit()).isEqualTo(75.00);
		assertThat(trade.getAdjustedCostBasis()).isEqualTo(trade.getCostBasis() - ( option1.getTotalProfit() / 100 ));
		
		
	}
	
}
