package com.leon.services;

import com.leon.models.Exchange;
import com.leon.models.Instrument;
import com.leon.repositories.ExchangeRepository;
import com.leon.repositories.InstrumentRepository;
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
}
