package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Broker")
public class Broker
{
    @Id
    UUID brokerId;
    String brokerAcronym;
    String brokerDescription;

    public Broker() {
        this.brokerId = UUID.randomUUID();
        this.brokerAcronym = "";
        this.brokerDescription = "";
    }

    public Broker(String brokerAcronym, String brokerDescription) {
        this.brokerId = UUID.randomUUID();
        this.brokerAcronym = brokerAcronym;
        this.brokerDescription = brokerDescription;
    }

    public UUID getBrokerId() {
        return brokerId;
    }
    public void setBrokerId(UUID brokerId) {
        this.brokerId = brokerId;
    }
    public String getBrokerAcronym() {
        return brokerAcronym;
    }
    public void setBrokerAcronym(String brokerAcronym) {
        this.brokerAcronym = brokerAcronym;
    }
    public String getBrokerDescription() {
        return brokerDescription;
    }
    public void setBrokerDescription(String brokerDescription) {
        this.brokerDescription = brokerDescription;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Broker)) return false;
        Broker broker = (Broker) o;
        return brokerId.equals(broker.brokerId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getBrokerId(), getBrokerAcronym(), getBrokerDescription());
    }
    @Override
    public String toString() {
        return "Broker{" +
                "brokerId=" + brokerId +
                ", brokerAcronym='" + brokerAcronym + '\'' +
                ", brokerDescription='" + brokerDescription + '\'' +
                '}';
    }
}
