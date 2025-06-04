package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("News")
public class News
{
    @Id
    private UUID newsId;
    private String stockCode;
    private String headline;
    private String link;
    private String source;
    private LocalDateTime timeStamp;

    public News()
    {
        this.newsId = UUID.randomUUID();
        this.stockCode = "";
        this.headline = "";
        this.link = "";
        this.source = "";
        this.timeStamp = LocalDateTime.now();
    }

    public News(String newsId, String stockCode, String headline, String link, String source, LocalDateTime timeStamp)
    {
        this.stockCode = stockCode;
        this.headline = headline;
        this.link = link;
        this.source = source;
        this.timeStamp = timeStamp;
    }

    public UUID getNewsId()
    {
        return newsId;
    }

    public void setNewsId(UUID newsId)
    {
        this.newsId = newsId;
    }

    public String getStockCode()
    {
        return stockCode;
    }

    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }

    public String getHeadline()
    {
        return headline;
    }

    public void setHeadline(String headline)
    {
        this.headline = headline;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime date)
    {
        this.timeStamp = date;
    }

    @Override
    public String toString()
    {
        return "News{" +
                "stockCode='" + stockCode + '\'' +
                ", headline='" + headline + '\'' +
                ", link='" + link + '\'' +
                ", source='" + source + '\'' +
                ", newId='" + newsId + "\'" +
                ", date=" + timeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof News)) return false;
        News news = (News) o;
        return newsId.equals(news.newsId) && stockCode.equals(news.stockCode) && headline.equals(news.headline) && link.equals(news.link) && source.equals(news.source) && timeStamp.equals(news.timeStamp);
    }

    @Override
    public int hashCode()
    {
        return newsId.hashCode() + stockCode.hashCode() + headline.hashCode() + link.hashCode() + source.hashCode() + timeStamp.hashCode();
    }

}
