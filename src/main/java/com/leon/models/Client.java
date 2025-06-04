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
    private String clientCode;

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

    public String getClientCode()
    {
        return clientCode;
    }

    public void setClientCode(String clientCode)
    {
        this.clientCode = clientCode;
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", clientCode='" + clientCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return clientId.equals(client.clientId) && clientName.equals(client.clientName) && Objects.equals(clientCode, client.clientCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientName, clientCode);
    }

    public Client(UUID clientId, String clientName)
    {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientCode = "";
    }

    public Client()
    {
        this.clientId = UUID.randomUUID();
        this.clientName = "";
        this.clientCode = "";
    }
}
