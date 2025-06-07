package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("ClientInterest")
public class ClientInterest
{
    private static final Logger logger = LoggerFactory.getLogger(ClientInterest.class);
    @Id
    private UUID clientInterestId;
    private UUID clientId;
    private String notes;
    private Side side;
    private String instrumentCode;
    private String ownerId;

    public ClientInterest()
    {
        this.clientInterestId = UUID.randomUUID();
        clientId = UUID.randomUUID();
        ownerId = "";
        notes = "";
        side = Side.BUY;
        instrumentCode = "";
    }

    public ClientInterest(String ownerId, UUID clientId, String notes, Side side, String stockCode)
    {
        this.clientId = clientId;
        this.ownerId = ownerId;
        this.notes = notes;
        this.side = side;
        this.instrumentCode = stockCode;
    }

    public UUID getClientInterestId()
    {
        return clientInterestId;
    }

    public void setClientInterestId(UUID clientInterestId)
    {
        this.clientInterestId = clientInterestId;
    }

    public UUID getClientId()
    {
        return clientId;
    }

    public void setClientId(UUID clientId)
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

    public String getInstrumentCode()
    {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode)
    {
        this.instrumentCode = instrumentCode;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public static boolean isValid(ClientInterest clientInterest)
    {
        if(clientInterest == null)
        {
            logger.error("ClientInterest is null. Invalid client interest.");
            return false;
        }
        if(clientInterest.getClientId() == null || clientInterest.getClientId().toString().isEmpty())
        {
            logger.error("Client ID is null or empty. Invalid client interest.");
            return false;
        }
        if(clientInterest.getOwnerId() == null || clientInterest.getOwnerId().isEmpty())
        {
            logger.error("Owner ID is null or empty. Invalid client interest.");
            return false;
        }
        if(clientInterest.getSide() == null)
        {
            logger.error("Side is null. Invalid client interest.");
            return false;
        }
        if(clientInterest.getInstrumentCode() == null || clientInterest.getInstrumentCode().isEmpty())
        {
            logger.error("Instrument code is null or empty. Invalid client interest.");
            return false;
        }
        if(clientInterest.getClientInterestId() == null || clientInterest.getClientInterestId().toString().isEmpty())
        {
            logger.error("Client interest ID is null or empty. Invalid client interest.");
            return false;
        }
        return true;
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
                ", instrumentCode=" + instrumentCode + "}";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ClientInterest)) return false;
        ClientInterest that = (ClientInterest) o;
        return ownerId.equals(that.ownerId) && clientInterestId.equals(that.clientInterestId) && clientId.equals(that.clientId) && notes.equals(that.notes) && side == that.side && instrumentCode.equals(that.instrumentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, clientId, notes, side, instrumentCode, clientInterestId);
    }
}
