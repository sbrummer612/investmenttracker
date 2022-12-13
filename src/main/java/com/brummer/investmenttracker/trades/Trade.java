package com.brummer.investmenttracker.trades;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.brummer.investmenttracker.accounts.Account;
import com.brummer.investmenttracker.options.Option;
import com.brummer.investmenttracker.transactions.options.summary.OptionTransactionSummary;

import lombok.Data;

@Data
@Entity
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull(message = "You must select an Account.")
	@ManyToOne()
	@JoinColumn(name = "account_id")
	private Account account;

	@NotEmpty(message = "Symbol cannot be empty.")
	@NotNull(message = "Symbol cannot be null.")
	@Column(name = "symbol")
	private String symbol;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "quantity")
	private Float quantity;
	
	@Column(name = "date_acquired")
	private Date dateAcquired;
	
	@Column(name = "date_sold", nullable = true )
	private Date dateSold;
	
	@Column(name = "proceeds")
	private Double proceeds;
	
	@Column(name = "cost_basis")
	private Double costBasis;
	
	@Column(name = "short_term_gain")
	private Double shortTermGain;
	
	@Column(name = "long_term_gain")
	private Double longTermGain;
	
	@Transient
	private Double mostRecentPrice;
	
	@Transient
	private Double adjustedCostBasis;
	
	@Transient
	private Double totalGainLoss;
	
	@Transient
	private Double currentGainLoss;
	
	@Transient
	private double currentAdjustedGainLoss;
	
	@Transient
	private Double averageOptionReturn;
	
	@Transient
	private Double averageOptionAnnualizedReturn;
	
	@Transient
	private List<Option> options;
	
	@Transient
	private List<OptionTransactionSummary> optionTransactionSummaries;
	
}
