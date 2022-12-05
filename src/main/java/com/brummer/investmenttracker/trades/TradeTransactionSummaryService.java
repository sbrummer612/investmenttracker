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
			Double averageAnnulizedReturn = 0d;
			for(OptionTransactionSummary ots : trade.getOptionTransactionSummaries()) {
				if(trade.getTotalGainLoss() == null) {
					trade.setTotalGainLoss(ots.getGainLoss());
				}
				else {
					trade.setTotalGainLoss( trade.getTotalGainLoss() + ots.getGainLoss());
				}
				averageReturn = averageReturn + ( (ots.getReturnPercentage() != null) ? ots.getReturnPercentage() : 0d );
				averageAnnulizedReturn = averageAnnulizedReturn + ( (ots.getReturnPercentageAnnualized() != null ) ? ots.getReturnPercentageAnnualized() : 0d  );
				
			}
			trade.setTotalGainLoss( Double.valueOf( df.format(trade.getTotalGainLoss()) ) );
			if(trade.getTotalGainLoss() != null) {
				trade.setAdjustedCostBasis( Double.valueOf( df.format( trade.getCostBasis() - (trade.getTotalGainLoss() / trade.getQuantity()) ) ) );	
			}
			if(averageReturn != 0d) {
				trade.setAverageOptionReturn( Double.valueOf( df.format( averageReturn / trade.getOptionTransactionSummaries().size() ) ) );
			}
			if(averageAnnulizedReturn != 0d) {
				trade.setAverageOptionAnnualizedReturn( Double.valueOf( df.format( averageAnnulizedReturn / trade.getOptionTransactionSummaries().size() ) ) ); 
			}
		}
		
	}
	
}
