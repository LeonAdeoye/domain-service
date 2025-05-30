package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Exchange")
public class Exchange {
    private String ExchangeName;
    private String ExchangeId;

    public Exchange() {
        ExchangeName = "";
        ExchangeId = "";
    }

    public Exchange(String exchangeName, String exchangeId) {
        this.ExchangeName = exchangeName;
        this.ExchangeId = exchangeId;
    }

    public String getExchangeName() {
        return ExchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.ExchangeName = exchangeName;
    }

    public String getExchangeId() {
        return ExchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.ExchangeId = exchangeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exchange)) return false;
        Exchange exchange = (Exchange) o;
        return ExchangeName.equals(exchange.ExchangeName) && ExchangeId.equals(exchange.ExchangeId);
    }

    @Override
    public int hashCode() {
        return 31 * ExchangeName.hashCode() + ExchangeId.hashCode();
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "ExchangeName='" + ExchangeName + '\'' +
                ", ExchangeId='" + ExchangeId + '\'' +
                '}';
    }
}




