package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Exchange")
public class Exchange
{
    private String exchangeName;
    @Id
    private UUID exchangeId;
    private String exchangeAcronym;
    private String timezone;
    private String openTime;
    private String closeTime;
    private String lunchStart;
    private String lunchEnd;
    private String currency;

    public Exchange()
    {
        exchangeName = "";
        exchangeId = UUID.randomUUID();
        exchangeAcronym = "";
        timezone = "UTC";
        openTime = "";
        closeTime = "";
        lunchStart = "";
        lunchEnd = "";
        currency = "USD";
    }

    public Exchange(String exchangeName, UUID exchangeId, String exchangeAcronym)
    {
        this();
        this.exchangeName = exchangeName;
        this.exchangeId = exchangeId;
        this.exchangeAcronym = exchangeAcronym;
    }

    public String getExchangeName()
    {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName)
    {
        this.exchangeName = exchangeName;
    }

    public UUID getExchangeId()
    {
        return exchangeId;
    }

    public void setExchangeId(UUID exchangeId)
    {
        this.exchangeId = exchangeId;
    }

    public String getExchangeAcronym()
    {
        return exchangeAcronym;
    }

    public void setExchangeAcronym(String exchangeAcronym)
    {
        this.exchangeAcronym = exchangeAcronym;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }

    public String getOpenTime()
    {
        return openTime;
    }

    public void setOpenTime(String openTime)
    {
        this.openTime = openTime;
    }

    public String getCloseTime()
    {
        return closeTime;
    }

    public void setCloseTime(String closeTime)
    {
        this.closeTime = closeTime;
    }

    public String getLunchStart()
    {
        return lunchStart;
    }

    public void setLunchStart(String lunchStart)
    {
        this.lunchStart = lunchStart;
    }

    public String getLunchEnd()
    {
        return lunchEnd;
    }

    public void setLunchEnd(String lunchEnd)
    {
        this.lunchEnd = lunchEnd;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Exchange))
            return false;
        Exchange exchange = (Exchange) o;
        return Objects.equals(exchangeName, exchange.exchangeName) &&
               Objects.equals(exchangeId, exchange.exchangeId) &&
               Objects.equals(exchangeAcronym, exchange.exchangeAcronym);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(exchangeName, exchangeId, exchangeAcronym);
    }

    @Override
    public String toString()
    {
        return "Exchange{" +
                "exchangeName='" + exchangeName + '\'' +
                ", exchangeId='" + exchangeId + '\'' +
                ", exchangeAcronym='" + exchangeAcronym + '\'' +
                ", timezone='" + timezone + '\'' +
                ", openTime='" + openTime + '\'' +
                ", closeTime='" + closeTime + '\'' +
                ", lunchStart='" + lunchStart + '\'' +
                ", lunchEnd='" + lunchEnd + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
