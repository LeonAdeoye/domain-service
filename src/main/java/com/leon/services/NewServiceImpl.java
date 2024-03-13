package com.leon.services;

import com.leon.models.News;
import com.leon.repositories.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void saveNews(News newsToSave)
    {
        News result = newsRepository.save(newsToSave);
        addNewsToMap(newsToSave);
    }

    @Override
    public void deleteNews(String newsId)
    {
        newsRepository.deleteById(newsId);
        for(List<News> newsList : newsMap.values())
        {
            if(newsList.removeIf(news -> news.getNewsId().equals(newsId)))
                break;
        }
    }

    @Override
    public void updateNews(News newsToUpdate)
    {
        newsRepository.save(newsToUpdate);
        List<News> newsList = newsMap.get(newsToUpdate.getNewsId());
        newsList.removeIf(news -> news.getNewsId().equals(newsToUpdate.getNewsId()));
        newsList.add(newsToUpdate);
    }

    @Override
    public List<News> getNewsByStockCode(String stockCode, LocalDateTime fromTimeStamp)
    {
        return newsMap.get(stockCode).stream()
                .filter(newsItem -> newsItem.getTimeStamp().isAfter(fromTimeStamp))
                .toList();
    }

    @Override
    public List<News> getAllNews(LocalDateTime fromTimeStamp)
    {
        return newsMap.values().stream()
                .flatMap(List::stream)
                .filter(newsItem -> newsItem.getTimeStamp().isAfter(fromTimeStamp))
                .toList();
    }

    @Override
    public List<News> getNewsBySource(String source, LocalDateTime fromTimeStamp)
    {
        return newsMap.values().stream()
                .flatMap(List::stream)
                .filter(newsItem -> newsItem.getTimeStamp().isAfter(fromTimeStamp) && newsItem.getSource().equals(source))
                .toList();
    }
}
