package com.brummer.investmenttracker.trades;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "symbol")
	private String symbol;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "quantity")
	private Float quantity;
	
	@Column(name = "date_acquired")
	private Date dateAcquired;
	
	@Column(name = "date_sold")
	private Date dateSold;
	
	@Column(name = "proceeds")
	private Float proceeds;
	
	@Column(name = "cost_basis")
	private Float costBasis;
	
	@Column(name = "short_term_gain")
	private Float shortTermGain;
	
	@Column(name = "long_term_gain")
	private Float longTermGain;
	
}
