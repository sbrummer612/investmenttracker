package com.brummer.investmenttracker.constants;

public enum EquityType {

	STOCK("S", "Stock"),
	OPTION("O", "Option"),;
	
	private String value;
	private String name;
	
	EquityType(String value, String name) {
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
