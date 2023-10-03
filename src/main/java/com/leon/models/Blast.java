package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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

    private int blastId;
    private String blastName;
    private int clientId;
    private String[] markets;
    private ContentType[] contents = new ContentType[4];
    private LocalTime triggerTime;
    private Map<String, Double> advFilter;
    private Map<String, Integer> notionalValueFilter;

    public int getBlastId()
    {
        return blastId;
    }

    public void setBlastId(int blastId)
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

    public int getClientId()
    {
        return clientId;
    }

    public void setClientId(int clientId)
    {
        this.clientId = clientId;
    }

    public String[] getMarkets()
    {
        return markets;
    }

    public void setMarkets(String[] markets)
    {
        this.markets = markets;
    }

    public ContentType[] getContents()
    {
        return contents;
    }

    public void setContents(ContentType[] contents)
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
        return blastId == blast.blastId && clientId == blast.clientId && blastName.equals(blast.blastName) && Arrays.equals(markets, blast.markets) && Arrays.equals(contents, blast.contents) && triggerTime.equals(blast.triggerTime) && advFilter.equals(blast.advFilter) && notionalValueFilter.equals(blast.notionalValueFilter);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hash(blastId, blastName, clientId, triggerTime, advFilter, notionalValueFilter);
        result = 31 * result + Arrays.hashCode(markets);
        result = 31 * result + Arrays.hashCode(contents);
        return result;
    }

    @Override
    public String toString() {
        return "Blast{" +
                "blastId=" + blastId +
                ", blastName='" + blastName + '\'' +
                ", clientId=" + clientId +
                ", markets=" + Arrays.toString(markets) +
                ", contents=" + Arrays.toString(contents) +
                ", triggerTime=" + triggerTime +
                ", advFilter=" + advFilter +
                ", notionalValueFilter=" + notionalValueFilter +
                '}';
    }
}
