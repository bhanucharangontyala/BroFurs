package com.brofurs.brofurs.enums;

public enum OrderStatus {
	NEW("New"), 
	CONFIRMED("Confirmed"), 
	IN_PRODUCTION("In Production"), 
	READY("Ready"), 
	DELIVERED("Delivered"),
	CANCELLED("Cancelled");

	private final String displayName;

	OrderStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
