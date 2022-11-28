package com.brummer.investmenttracker.trades;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.brummer.investmenttracker.options.Option;

@Service
public class TradeTransientFields {

	public TradeTransientFields() {
		
	}
	
	public void computeTransientFields(List<Trade> trades) {
	
		for(Trade trade : trades) {
			
			double totalOptionProfit = 0;
			for(Option option : trade.getOptions()) {
				
				if(option.getQuantity() > 0) {
					if(option.getProceeds() != null && option.getProceeds().doubleValue() != 0.00) {
						option.setTotalProfit( option.getProceeds().doubleValue() - option.getCostBasis().doubleValue() );
					}
					else {
						option.setTotalProfit( option.getCostBasis().doubleValue() );
					}	
				
//					Double totalProfit = option.getTotalProfit();
//					Double totalQuantity = option.getQuantity();
//					Double strikePrice = option.getStrikePrice();
					
					Double returnPercentage = ( (option.getTotalProfit() ) / (option.getStrikePrice() * option.getQuantity() ) );
					option.setReturnPercentage(returnPercentage);
					
					Date endDate = null;
					if(option.getDateSold() != null) {
						endDate = option.getDateSold();
					}
					else {
						endDate = option.getExpirationDate();
					}
					long diffInMillies = Math.abs(endDate.getTime() - option.getDateAcquired().getTime());
					long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
					if(diffDays == 0) {
						diffDays = 1;
					}
					option.setReturnPercentageAnnualized( option.getReturnPercentage() * ( 365 / diffDays ) / 100 );		
				}
				if(option.getOptionType().equals("C")) {
					// only decrease cost on shares if we sold a Call
					totalOptionProfit = totalOptionProfit + ((option.getTotalProfit() == null)? 0 : option.getTotalProfit().doubleValue());
				}
			}
			
			trade.setAdjustedCostBasis( Double.valueOf(trade.getCostBasis().doubleValue() - (totalOptionProfit / 100) ));
		}
		
	}
	
}
