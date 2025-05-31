package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Exchange")
public class Exchange {
    private String exchangeName;
    private String exchangeId;

    private String exchangeAcronym;

    public Exchange() {
        exchangeName = "";
        exchangeId = "";
        exchangeAcronym = "";
    }

    public Exchange(String exchangeName, String exchangeId, String exchangeAcronym) {
        this.exchangeName = exchangeName;
        this.exchangeId = exchangeId;
        this.exchangeAcronym = exchangeAcronym;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getExchangeAcronym() {
        return exchangeAcronym;
    }

    public void setExchangeAcronym(String exchangeAcronym) {
        this.exchangeAcronym = exchangeAcronym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exchange)) return false;
        Exchange exchange = (Exchange) o;
        return exchangeName.equals(exchange.exchangeName) &&
               exchangeId.equals(exchange.exchangeId) &&
               exchangeAcronym.equals(exchange.exchangeAcronym);
    }

    @Override
    public int hashCode() {
        return 31 * exchangeName.hashCode() + exchangeId.hashCode() + exchangeAcronym.hashCode();
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "exchangeName='" + exchangeName + '\'' +
                ", exchangeId='" + exchangeId + '\'' +
                ", exchangeAcronym='" + exchangeAcronym + '\'' +
                '}';
    }
}




