package com.leon.services;

import com.leon.models.Exchange;
import com.leon.repositories.ExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ExchangeServiceImpl implements ExchangeService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeServiceImpl.class);
    @Autowired
    private ExchangeRepository exchangeRepository;
    private List<Exchange> exchanges = new ArrayList<>();

    @Override
    public List<Exchange> getAll()
    {
        return exchanges;
    }

    @PostConstruct
    public void initialize()
    {
        List<Exchange> result = exchangeRepository.findAll();
        exchanges.addAll(result);
        logger.info("Loaded instrument service with {} instruments(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
        exchanges.clear();
        initialize();
    }

    @Override
    public Exchange createExchange(Exchange exchange)
    {
        Exchange existingExchange = exchangeRepository.findById(exchange.getExchangeId()).orElse(null);
        if (existingExchange != null)
        {
            logger.warn("Exchange with ID {} already exists. Not creating a new one.", exchange.getExchangeId());
            return existingExchange;
        }

        Exchange createdExchange = exchangeRepository.save(exchange);
        exchanges.add(createdExchange);
        logger.info("Created new exchange with ID: {}", createdExchange.getExchangeId());
        return createdExchange;
    }

    @Override
    public void deleteExchange(String exchangeId)
    {
        Exchange exchangeToDelete = exchangeRepository.findById(exchangeId).orElse(null);
        if (exchangeToDelete != null)
        {
            exchangeRepository.delete(exchangeToDelete);
            exchanges.remove(exchangeToDelete);
            logger.info("Deleted exchange with ID: {}", exchangeId);
        }
        else
        {
            logger.warn("Attempted to delete non-existing exchange with ID: {}", exchangeId);
        }
    }
}
