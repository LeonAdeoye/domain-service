package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private int clientId;
    private String notes;
    private Side side;
    private String symbol;

    public int getClientId()
    {
        return clientId;
    }

    public void setClientId(int clientId)
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

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    @Override
    public String toString()
    {
        return "ClientInterest{" +
                "clientId=" + clientId +
                ", notes='" + notes + '\'' +
                ", side=" + side +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ClientInterest)) return false;
        ClientInterest that = (ClientInterest) o;
        return clientId == that.clientId && notes.equals(that.notes) && side == that.side && symbol.equals(that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, notes, side, symbol);
    }
}
