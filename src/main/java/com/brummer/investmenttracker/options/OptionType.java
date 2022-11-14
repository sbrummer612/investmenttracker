package com.brummer.investmenttracker.options;

//import java.util.EnumSet;
//import java.util.HashMap;
//import java.util.Map;

public enum OptionType {

	ALL("ALL", "All Options", "A"),
	CALL("CALL", "CALL Option", "C"),
	PUT("PUT", "PUT Option", "P");
	
	
	private String type;
	private String typeName;
	private String value;
	
	OptionType(String type, String typeName, String value){
		this.type = type;
		this.typeName = typeName;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

//	/**
//	 * Lookup functionality. 
//	 */
//	private static final Map<String, OptionType> lookup 
//            = new HashMap<String, OptionType>();
//
//	static {
//		for(OptionType g : EnumSet.allOf(OptionType.class)) {
//		    lookup.put(g.getType(), g);
//		}
//	}
//
//	public static OptionType get(String value) { 
//		return lookup.get(value); 
//	}
	
}