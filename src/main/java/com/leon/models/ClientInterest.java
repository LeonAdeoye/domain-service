package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("ClientInterest")
public class ClientInterest
{
    enum Side
    {
        BUY,
        SELL
    }

    @Id
    private String clientInterestId;
    private String clientId;
    private String notes;
    private Side side;
    private String stockCode;
    private String ownerId;

    public ClientInterest()
    {
        clientId = "";
        ownerId = "";
        notes = "";
        side = Side.BUY;
        stockCode = "";
    }

    public ClientInterest(String ownerId, String clientId, String notes, Side side, String stockCode)
    {
        this.clientId = clientId;
        this.ownerId = ownerId;
        this.notes = notes;
        this.side = side;
        this.stockCode = stockCode;
    }

    public String getClientInterestId()
    {
        return clientInterestId;
    }

    public void setClientInterestId(String clientInterestId)
    {
        this.clientInterestId = clientInterestId;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }

    public String getStockCode()
    {
        return stockCode;
    }

    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    @Override
    public String toString()
    {
        return "ClientInterest{" +
                "clientId=" + clientId +
                ", notes=" + notes +
                ", side=" + side +
                ", ownerId=" + ownerId +
                ", clientInterestId=" + clientInterestId +
                ", stockCode=" + stockCode + "}";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ClientInterest)) return false;
        ClientInterest that = (ClientInterest) o;
        return ownerId.equals(that.ownerId) && clientInterestId.equals(that.clientInterestId) && clientId.equals(that.clientId) && notes.equals(that.notes) && side == that.side && stockCode.equals(that.stockCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, clientId, notes, side, stockCode, clientInterestId);
    }
}
