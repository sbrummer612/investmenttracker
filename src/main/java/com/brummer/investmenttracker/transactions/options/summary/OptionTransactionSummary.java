package com.brummer.investmenttracker.transactions.options.summary;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class OptionTransactionSummary implements Comparable<OptionTransactionSummary>{

	private DecimalFormat df = new DecimalFormat("####0.00");
	private Date transactionDate;
	private String accountName;
	private String stockSymbol;
	private String optionSymbol;
	private String securityDescription;
	private Double quantity;
	private Double initialTotalQuantity;
	private Double costBasis;
	private Double proceeds;
	private Double gainLoss;
	private Date dateAcquired;
	private Date dateClosed;
	private Date expirationDate;
	private Double strikePrice;
	
	public Double getReturnPercentage() {
		
		double gl = 0.00;
		double sp = 0.00;
		double itq = 0.00;
		gl = (gainLoss != null) ? gainLoss.doubleValue() : 0.00;
		sp = (strikePrice != null) ? strikePrice.doubleValue() : 0.00;
		itq = (initialTotalQuantity != null) ? initialTotalQuantity.doubleValue() : 0.00;
		if(gl != 0.00 && sp != 0.00 && itq != 0.00 ) {
			return Double.valueOf( df.format(Double.valueOf(gl / Math.abs(sp * itq) )) );
		}
		return null;
	}
	
	public Double getReturnPercentageAnnualized() {
		
		if(this.getReturnPercentage() != null) {
			Date endDate = null;
			if(this.getDateClosed() != null) {
				endDate = this.getDateClosed();
			}
			else {
				endDate = this.getExpirationDate();
			}
			long diffInMillies = Math.abs(endDate.getTime() - this.getDateAcquired().getTime());
			long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			if(diffDays == 0) {
				diffDays = 1;
			}
			return Double.valueOf( df.format(this.getReturnPercentage() * (( 365 / diffDays) )) );
		}
		
		return null;
	}

	@Override
	public int compareTo(OptionTransactionSummary o) {
		
		if(getDateAcquired() == null || o.getDateAcquired() == null) {
			if(getDateAcquired() == null) {
				return -1;
			}
			if(o.getDateAcquired() == null) {
				return 1;
			}
			return -1;	
//			return 0;	
		}
		
		return o.getDateAcquired().compareTo(getDateAcquired());
	}

}
