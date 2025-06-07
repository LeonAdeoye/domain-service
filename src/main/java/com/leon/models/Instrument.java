package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Instrument")
public class Instrument
{
    private static final Logger logger = LoggerFactory.getLogger(Instrument.class);
    @Id
    private UUID instrumentId;
    private String instrumentCode;
    private String instrumentDescription;
    private AssetType assetType;
    private String blgCode;
    private String ric;
    private Currency settlementCurrency;
    private SettlementType settlementType;
    private String exchangeAcronym;


    public Instrument()
    {
        this.instrumentId = UUID.randomUUID();
        instrumentCode = "";
        instrumentDescription = "";
        assetType = AssetType.STOCK;
        blgCode = "";
        ric = "";
        settlementCurrency = Currency.HKD;
        settlementType = SettlementType.T_PLUS_ONE;
        exchangeAcronym = "HKSE";
    }

    public Instrument(String instrumentCode, String instrumentDescription, AssetType assetType, String blgCode, String ric, Currency settlementCurrency, SettlementType settlementType, String exchangeAcronym)
    {
        this.instrumentCode = instrumentCode;
        this.instrumentDescription = instrumentDescription;
        this.assetType = assetType;
        this.blgCode = blgCode;
        this.ric = ric;
        this.settlementCurrency = settlementCurrency;
        this.settlementType = settlementType;
        this.instrumentId = UUID.randomUUID();
        this.exchangeAcronym = exchangeAcronym;
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

    public UUID getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(UUID instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getRic() {
        return ric;
    }

    public void setRic(String ric) {
        this.ric = ric;
    }

    public Currency getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(Currency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public SettlementType getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(SettlementType settlementType) {
        this.settlementType = settlementType;
    }

    public String getExchangeAcronym() {
        return exchangeAcronym;
    }

    public void setExchangeAcronym(String exchangeAcronym) {
        this.exchangeAcronym = exchangeAcronym;
    }

    public static boolean isValidInstrument(Instrument instrument)
    {
        if (instrument == null || instrument.getInstrumentCode() == null || instrument.getInstrumentCode().isEmpty())
        {
            logger.error("Instrument is null or has an empty code. Invalid instrument.");
            return false;
        }
        if (instrument.getInstrumentDescription() == null || instrument.getInstrumentDescription().isEmpty())
        {
            logger.error("Instrument description is null or empty. Invalid instrument.");
            return false;
        }
        if (instrument.getAssetType() == null && instrument.getAssetType().toString().isEmpty())
        {
            logger.error("Asset type is null or empty. Invalid instrument.");
            return false;
        }
        if(instrument.getExchangeAcronym() == null || instrument.getExchangeAcronym().isEmpty())
        {
            logger.error("Exchange acronym is null or empty. Invalid instrument.");
            return false;
        }

        if (instrument.getSettlementCurrency() == null || instrument.getSettlementCurrency().toString().isEmpty())
        {
            logger.error("Settlement currency is null or empty. Invalid instrument.");
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Instrument{" +
                "instrumentCode='" + instrumentCode + '\'' +
                ", instrumentDescription='" + instrumentDescription + '\'' +
                ", assetType=" + assetType +
                ", blgCode='" + blgCode + '\'' +
                ", RIC='" + ric + '\'' +
                ", settlementCurrency=" + settlementCurrency +
                ", settlementType=" + settlementType +
                ", instrumentId=" + instrumentId +
                ", exchangeAcronym='" + exchangeAcronym + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        Instrument that = (Instrument) o;
        return getInstrumentId().equals(that.getInstrumentId()) && getInstrumentCode().equals(that.getInstrumentCode())
                && getInstrumentDescription().equals(that.getInstrumentDescription()) && getAssetType() == that.getAssetType()
                && Objects.equals(getBlgCode(), that.getBlgCode()) && Objects.equals(getRic(), that.getRic()) && getSettlementCurrency() == that.getSettlementCurrency()
                && getSettlementType() == that.getSettlementType() && Objects.equals(getExchangeAcronym(), that.getExchangeAcronym());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInstrumentId(), getInstrumentCode(), getInstrumentDescription(),
                getAssetType(), getBlgCode(), getRic(), getSettlementCurrency(), getSettlementType(), getExchangeAcronym());
    }
}
