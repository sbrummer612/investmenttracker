package com.brummer.investmenttracker.accounts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "number")
	private String number;
	
	@NotEmpty(message = "You must select a brokerage type or import won't work.")
	@Column(name = "brokerage", nullable = false)
	private String brokerage;
	
}
