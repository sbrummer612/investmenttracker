package com.brummer.investmenttracker.transactions;

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

import com.brummer.investmenttracker.accounts.Account;

import lombok.Data;

@Data
@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull(message = "Transaction Date cannot be empty.")
	@Column(name = "transaction_date")
	private Date transactionDate;
	
	@NotNull(message = "You must select an Account.")
	@ManyToOne()
	@JoinColumn(name = "account_id")
	private Account account;
	
	@Column(name = "action")
	private String action;
	
	@NotEmpty(message = "Symbol cannot be empty.")
	@Column(name = "symbol")
	private String symbol;
	
	@Column(name ="equityType")
	private String equityType; 
	
	@Column(name ="optionType")
	private String optionType;
	
	@Column(name = "security_description")
	private String securityDescription;
	
//	@Column(name = "description")
//	private String description;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@Column(name = "amount")
	private Double amount;
	
}
