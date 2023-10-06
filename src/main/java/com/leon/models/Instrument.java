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
    private String description;
    private AssetType assetType;
    private String blgCode;

    public Instrument()
    {
        stockCode = "";
        description = "";
        assetType = AssetType.STOCK;
        blgCode = "";
    }

    public Instrument(String stockCode, String description, AssetType assetType, String blgCode)
    {
        this.stockCode = stockCode;
        this.description = description;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
                ", description='" + description + '\'' +
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
        return stockCode.equals(instrument.stockCode) && description.equals(instrument.description) && assetType.equals(instrument.assetType) && blgCode.equals(instrument.blgCode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(stockCode, description, assetType, blgCode);
    }
}
