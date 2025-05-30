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

    private String instrumentCode;
    private String instrumentDescription;
    private AssetType assetType;
    private String blgCode;

    public Instrument()
    {
        instrumentCode = "";
        instrumentDescription = "";
        assetType = AssetType.STOCK;
        blgCode = "";
    }

    public Instrument(String instrumentCode, String instrumentDescription, AssetType assetType, String blgCode)
    {
        this.instrumentCode = instrumentCode;
        this.instrumentDescription = instrumentDescription;
        this.assetType = assetType;
        this.blgCode = blgCode;
    }

    public String getInstrumentCode()
    {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode)
    {
        this.instrumentCode = instrumentCode;
    }

    public String getInstrumentDescription()
    {
        return instrumentDescription;
    }

    public void setInstrumentDescription(String instrumentDescription)
    {
        this.instrumentDescription = instrumentDescription;
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
                "instrumentCode='" + instrumentCode + '\'' +
                ", instrumentDescription='" + instrumentDescription + '\'' +
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
        return instrumentCode.equals(instrument.instrumentCode) && instrumentDescription.equals(instrument.instrumentDescription) && assetType.equals(instrument.assetType) && blgCode.equals(instrument.blgCode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(instrumentCode, instrumentDescription, assetType, blgCode);
    }
}
