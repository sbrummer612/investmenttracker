package com.brummer.investmenttracker.constants;

public enum BrokerageTypes {

	FIDELITY("Fidelity", "Fidelity")
	;
	
	private String type;
	private String name;
	
	BrokerageTypes(String type, String name){
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
