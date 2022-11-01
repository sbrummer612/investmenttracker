package com.brummer.investmenttracker.trades;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.brummer.investmenttracker.accounts.Account;

import lombok.Data;

@Data
@Entity
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull(message = "You must select an Account.")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private Account account;

	@NotEmpty(message = "Symbol cannot be empty.")
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
	private Float proceeds;
	
	@Column(name = "cost_basis")
	private Float costBasis;
	
	@Column(name = "short_term_gain")
	private Float shortTermGain;
	
	@Column(name = "long_term_gain")
	private Float longTermGain;
	
}
