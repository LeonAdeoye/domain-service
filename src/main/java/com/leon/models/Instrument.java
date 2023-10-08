package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Instrument")
public class Instrument
{
    enum AssetType
    {
        STOCK,
        OPTION,
        FUTURE
    }

    private String stockCode;
    private String stockDescription;
    private AssetType assetType;
    private String blgCode;

    public Instrument()
    {
        stockCode = "";
        stockDescription = "";
        assetType = AssetType.STOCK;
        blgCode = "";
    }

    public Instrument(String stockCode, String stockDescription, AssetType assetType, String blgCode)
    {
        this.stockCode = stockCode;
        this.stockDescription = stockDescription;
        this.assetType = assetType;
        this.blgCode = blgCode;
    }

    public String getStockCode()
    {
        return stockCode;
    }

    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }

    public String getStockDescription()
    {
        return stockDescription;
    }

    public void setStockDescription(String stockDescription)
    {
        this.stockDescription = stockDescription;
    }

    public AssetType getAssetType()
    {
        return assetType;
    }

    public void setAssetType(AssetType assetType)
    {
        this.assetType = assetType;
    }

    public String getBlgCode()
    {
        return blgCode;
    }

    public void setBlgCode(String blgCode)
    {
        this.blgCode = blgCode;
    }

    @Override
    public String toString()
    {
        return "Instrument{" +
                "stockCode='" + stockCode + '\'' +
                ", stockDescription='" + stockDescription + '\'' +
                ", assetType=" + assetType +
                ", blgCode='" + blgCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        Instrument instrument = (Instrument) o;
        return stockCode.equals(instrument.stockCode) && stockDescription.equals(instrument.stockDescription) && assetType.equals(instrument.assetType) && blgCode.equals(instrument.blgCode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(stockCode, stockDescription, assetType, blgCode);
    }
}
