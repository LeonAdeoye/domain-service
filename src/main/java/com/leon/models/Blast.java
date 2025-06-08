package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalTime;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Blast")
public class Blast
{
    public enum ContentType
    {
        IOIs,
        NEWS,
        FLOWS,
        HOLDINGS
    }

    @Id
    private UUID blastId;
    private String ownerId;
    private String blastName;
    private UUID clientId;
    private List<String> markets;
    private List<ContentType> contents = new ArrayList<>();
    private LocalTime triggerTime;
    private Map<String, Double> advFilter;
    private Map<String, Integer> notionalValueFilter;

    public Blast()
    {
        this.blastName = "";
        this.clientId = UUID.randomUUID();
        this.ownerId = "";
        this.blastId = UUID.randomUUID();
        this.markets = new ArrayList<>();
        this.contents = new ArrayList<>();
        this.advFilter = new HashMap<>();
        this.notionalValueFilter = new HashMap<>();
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public UUID getBlastId()
    {
        return blastId;
    }

    public void setBlastId(UUID blastId)
    {
        this.blastId = blastId;
    }

    public String getBlastName()
    {
        return blastName;
    }

    public void setBlastName(String blastName)
    {
        this.blastName = blastName;
    }

    public UUID getClientId()
    {
        return clientId;
    }

    public void setClientId(UUID clientId)
    {
        this.clientId = clientId;
    }

    public List<String> getMarkets()
    {
        return markets;
    }

    public void setMarkets(List<String> markets)
    {
        this.markets = markets;
    }

    public List<ContentType> getContents()
    {
        return contents;
    }

    public void setContents(List<ContentType> contents)
    {
        this.contents = contents;
    }

    public LocalTime getTriggerTime()
    {
        return triggerTime;
    }

    public void setTriggerTime(LocalTime triggerTime)
    {
        this.triggerTime = triggerTime;
    }

    public Map<String, Double> getAdvFilter()
    {
        return advFilter;
    }

    public void setAdvFilter(Map<String, Double> advFilter)
    {
        this.advFilter = advFilter;
    }

    public Map<String, Integer> getNotionalValueFilter()
    {
        return notionalValueFilter;
    }

    public void setNotionalValueFilter(Map<String, Integer> notionalValueFilter)
    {
        this.notionalValueFilter = notionalValueFilter;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Blast)) return false;
        Blast blast = (Blast) o;
        return ownerId.equals(blast.ownerId) && blastId.equals(blast.blastId) && clientId.equals(blast.clientId) && blastName.equals(blast.blastName) && markets.equals(blast.markets) && contents.equals(blast.contents) && triggerTime.equals(blast.triggerTime) && advFilter.equals(blast.advFilter) && notionalValueFilter.equals(blast.notionalValueFilter);
    }

    @Override
    public String toString() {
        return "Blast{" +
                "blastId=" + blastId +
                ", blastName=" + blastName +
                ", clientId=" + clientId +
                ", ownerId=" + ownerId +
                ", markets=" + markets +
                ", contents=" + contents +
                ", triggerTime=" + triggerTime +
                ", advFilter=" + advFilter +
                ", notionalValueFilter=" + notionalValueFilter +
                '}';
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(blastId, ownerId, blastName, clientId, markets, contents, triggerTime, advFilter, notionalValueFilter);
    }
}
