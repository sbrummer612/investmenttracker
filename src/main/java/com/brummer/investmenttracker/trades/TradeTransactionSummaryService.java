package com.brummer.investmenttracker.trades;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.brummer.investmenttracker.transactions.options.summary.OptionTransactionSummary;

@Service
public class TradeTransactionSummaryService {

	public void summarizeTransactions(List<Trade> trades){
		
		DecimalFormat df = new DecimalFormat("####0.00");
		
		for(Trade trade : trades) {
			Double averageReturn = 0d;
			Double averageAnnualizedReturn = 0d;
			for(OptionTransactionSummary ots : trade.getOptionTransactionSummaries()) {
				if(trade.getTotalGainLoss() == null) {
					trade.setTotalGainLoss(ots.getGainLoss());
				}
				else {
					trade.setTotalGainLoss( trade.getTotalGainLoss() + ots.getGainLoss());
				}
				averageReturn = averageReturn + ( (ots.getReturnPercentage() != null) ? ots.getReturnPercentage() : 0d );
				averageAnnualizedReturn = averageAnnualizedReturn + ( (ots.getReturnPercentageAnnualized() != null ) ? ots.getReturnPercentageAnnualized() : 0d  );
				
			}
			if(trade.getTotalGainLoss() != null) {
				trade.setTotalGainLoss( Double.valueOf( df.format(trade.getTotalGainLoss()) ) );
				trade.setAdjustedCostBasis( Double.valueOf( df.format( trade.getCostBasis() - (trade.getTotalGainLoss() / trade.getQuantity()) ) ) );
			}
			if(averageReturn != 0d) {
				trade.setAverageOptionReturn( Double.valueOf( df.format( averageReturn / trade.getOptionTransactionSummaries().size() ) ) );
			}
			if(averageAnnualizedReturn != 0d) {
				trade.setAverageOptionAnnualizedReturn( Double.valueOf( df.format( averageAnnualizedReturn / trade.getOptionTransactionSummaries().size() ) ) ); 
			}
		}
		
	}
	
	public Trade buildTradeSummaryTransaction(List<Trade> trades) {
		
		DecimalFormat df = new DecimalFormat("####0.00");
		Trade summaryTrade = new Trade();
		Double averageReturn = 0d;
		int averageReturnCount = 0;
		Double averageAnnualizedReturn = 0d;
		int averageAnnualizedReturnCount = 0;
		for(Trade trade : trades) {
			Double totalGainLoss = 0d;
			if(summaryTrade.getTotalGainLoss() != null) {
				totalGainLoss = summaryTrade.getTotalGainLoss();
			}
			if(trade.getTotalGainLoss() != null) {
				totalGainLoss = totalGainLoss + trade.getTotalGainLoss();
			}
			summaryTrade.setTotalGainLoss( totalGainLoss );
			
			averageReturn = averageReturn + ((trade.getAverageOptionReturn() != null) ? trade.getAverageOptionReturn() : 0d);
			if(trade.getAverageOptionReturn() != null) {
				averageReturnCount++;	
			}
			
			averageAnnualizedReturn = averageAnnualizedReturn + ((trade.getAverageOptionAnnualizedReturn() != null) ? trade.getAverageOptionAnnualizedReturn() : 0d);
			if(trade.getAverageOptionAnnualizedReturn() != null) {
				averageAnnualizedReturnCount++;	
			}
			 
		}
		
		summaryTrade.setAverageOptionReturn( (averageReturnCount !=0 ) ? Double.valueOf( df.format(averageReturn / averageReturnCount) ) : null );
		summaryTrade.setAverageOptionAnnualizedReturn( (averageAnnualizedReturnCount != 0) ? Double.valueOf( df.format(averageAnnualizedReturn / averageAnnualizedReturnCount) ) : null);
		
		return summaryTrade;
	}
	
}
