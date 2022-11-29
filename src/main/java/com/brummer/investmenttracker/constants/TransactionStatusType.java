package com.brummer.investmenttracker.constants;

public enum TransactionStatusType {

	ALL("ALL", "All Transactions"),
	OPEN("OPEN", "Open Transactions"),
	CLOSED("CLOSED", "Closed Transactions"),;
	
	private String value;
	private String name;
	
	TransactionStatusType(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
