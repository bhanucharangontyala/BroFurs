package com.brofurs.brofurs.enums;

public enum StockStatus {
    IN_STOCK("In Stock"),
    OUT_OF_STOCK("Out of Stock"),
    MADE_TO_ORDER("Made to Order");

    private final String displayName;

    StockStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
