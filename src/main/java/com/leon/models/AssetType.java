package com.leon.models;

public enum AssetType
{
    STOCK ("Stock"),
    OPTION ("Option"),
    FUTURE ("Future"),
    ETF ("Exchange Traded Fund"),
    WARRANT ("Warrant"),
    SWAPS ("Swaps");

    private final String assetTypeName;

    AssetType(String assetTypeName)
    {
        this.assetTypeName = assetTypeName;
    }
}
