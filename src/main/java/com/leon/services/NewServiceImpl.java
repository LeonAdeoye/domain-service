package com.leon.services;

import com.leon.models.News;
import com.leon.repositories.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewServiceImpl implements NewsService
{
    private static final Logger logger = LoggerFactory.getLogger(NewServiceImpl.class);
    private Map<String, List<News>> newsMap = new HashMap<>();

    @Autowired
    private NewsRepository newsRepository;

    private void addNewsToMap(News newsItem)
    {
        if(!newsMap.containsKey(newsItem.getStockCode()))
            newsMap.put(newsItem.getStockCode(), new ArrayList<>());
        List<News> newsList = newsMap.get(newsItem.getStockCode());
        newsList.add(newsItem);
    }

    @PostConstruct
    public void initialize()
    {
        List<News> result = newsRepository.findAll();
        result.forEach(newsItem -> addNewsToMap(newsItem));
        logger.info("Loaded news service with {} news item(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
        newsMap.clear();
        initialize();
    }

    @Override
    public News saveNews(News newsToSave)
    {
        News savedNews = newsRepository.save(newsToSave);
        addNewsToMap(newsToSave);
        return savedNews;
    }

    @Override
    public void deleteNews(String newsId)   {
        newsRepository.deleteById(UUID.fromString(newsId));
        newsMap.values().forEach(newsList -> newsList.removeIf(news -> news.getNewsId().equals(UUID.fromString(newsId))));
    }

    @Override
    public News updateNews(News newsToUpdate)
    {
        News savedNews = newsRepository.save(newsToUpdate);
        List<News> newsList = newsMap.get(newsToUpdate.getStockCode());
        newsList.removeIf(news -> news.getNewsId().equals(newsToUpdate.getNewsId()));
        newsList.add(savedNews);
        return savedNews;
    }

    @Override
    public List<News> getNewsByStockCode(String stockCode, LocalDateTime fromTimeStamp)
    {
        return newsMap.get(stockCode).stream()
                .filter(newsItem -> newsItem.getTimeStamp().isAfter(fromTimeStamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getAllNews(LocalDateTime fromTimeStamp)
    {
        return newsMap.values().stream()
                .flatMap(List::stream)
                .filter(newsItem -> newsItem.getTimeStamp().isAfter(fromTimeStamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getNewsBySource(String source, LocalDateTime fromTimeStamp)
    {
        return newsMap.values().stream()
                .flatMap(List::stream)
                .filter(newsItem -> newsItem.getTimeStamp().isAfter(fromTimeStamp) && newsItem.getSource().equals(source))
                .collect(Collectors.toList());
    }
}
