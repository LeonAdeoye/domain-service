package com.leon.services;

import com.leon.models.News;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsService
{
    void reconfigure();

    News saveNews(News newsToSave);

    void deleteNews(String newsId);

    News updateNews(News newsToUpdate);

    List<News> getNewsByStockCode(String stockCode, LocalDateTime fromTimeStamp);

    List<News> getAllNews(LocalDateTime fromTimeStamp);

    List<News> getNewsBySource(String source, LocalDateTime fromTimeStamp);
}
