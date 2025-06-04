package com.leon.models;

public enum Side
{
    BUY ("Buy"),
    SELL("Sell"),
    SHORT_SELL("Short Sell");

    private final String sideName;

    Side(String sideName)
    {
        this.sideName = sideName;
    }
}
