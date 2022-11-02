package com.brummer.investmenttracker.options;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

import com.brummer.investmenttracker.accounts.Account;

import lombok.Data;

@Data
@Entity
public class Option {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull(message = "You must select an Account.")
	@ManyToOne()
	@JoinColumn(name = "account_id")
	private Account account;

	@NotEmpty(message = "Symbol cannot be empty.")
	@Column(name = "symbol")
	private String symbol;
	
	@NotEmpty(message = "Option Symbol cannot be empty.")
	@Column(name = "option_symbol")
	private String optionSymbol;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@NotNull(message = "Strike Price cannot be empty.")
	@Column(name = "strike_price")
	private Double strikePrice;
	
	@NotNull(message = "Expiration Date cannot be empty.")
	@Column(name = "expiration_date")
	private Date expirationDate;
	
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
	
}
