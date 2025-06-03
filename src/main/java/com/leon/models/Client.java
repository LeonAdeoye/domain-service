package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Client")
public class Client
{
    @Id
    private UUID clientId;
    private String clientName;

    public UUID getClientId()
    {
        return clientId;
    }

    public void setClientId(UUID clientId)
    {
        this.clientId = clientId;
    }

    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return clientId.equals(client.clientId) && clientName.equals(client.clientName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(clientId, clientName);
    }

    public Client(UUID clientId, String clientName)
    {
        this.clientId = clientId;
        this.clientName = clientName;
    }

    public Client()
    {
        this.clientId = UUID.randomUUID();
        this.clientName = "";
    }
}
