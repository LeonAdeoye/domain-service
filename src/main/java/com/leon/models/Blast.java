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
    enum ContentType
    {
        IOIs,
        NEWS,
        FLOWS,
        HOLDINGS
    }

    @Id
    private String blastId;
    private String blastName;
    private String clientId;
    private List<String> markets;
    private List<ContentType> contents = new ArrayList<>();
    private LocalTime triggerTime;
    private Map<String, Double> advFilter;
    private Map<String, Integer> notionalValueFilter;

    public Blast()
    {
        this.blastName = "";
        this.clientId = "";
        this.markets = new ArrayList<>();
        this.contents = new ArrayList<>();
        this.triggerTime = LocalTime.now();
        this.advFilter = new HashMap<>();
        this.notionalValueFilter = new HashMap<>();
    }

    public Blast(String blastName, String clientId, List<String> markets, List<ContentType> contents, LocalTime triggerTime, Map<String, Double> advFilter, Map<String, Integer> notionalValueFilter)
    {
        this.blastName = blastName;
        this.clientId = clientId;
        this.markets = markets;
        this.contents = contents;
        this.triggerTime = triggerTime;
        this.advFilter = advFilter;
        this.notionalValueFilter = notionalValueFilter;
    }

    public String getBlastId()
    {
        return blastId;
    }

    public void setBlastId(String blastId)
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

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
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
        return blastId.equals(blast.blastId) && clientId.equals(blast.clientId) && blastName.equals(blast.blastName) && markets.equals(blast.markets) && contents.equals(blast.contents) && triggerTime.equals(blast.triggerTime) && advFilter.equals(blast.advFilter) && notionalValueFilter.equals(blast.notionalValueFilter);
    }

    @Override
    public String toString() {
        return "Blast{" +
                "blastId=" + blastId +
                ", blastName='" + blastName + '\'' +
                ", clientId=" + clientId +
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
        return Objects.hash(blastId, blastName, clientId, markets, contents, triggerTime, advFilter, notionalValueFilter);
    }
}
