package com.leon.services;

import com.leon.models.Instrument;
import com.leon.repositories.InstrumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstrumentServiceImpl implements InstrumentService
{
    private static final Logger logger = LoggerFactory.getLogger(InstrumentServiceImpl.class);
    @Autowired
    private InstrumentRepository instrumentRepository;
    private List<Instrument> instruments = new ArrayList<>();

    @Override
    public List<Instrument> getAll()
    {
        return instruments;
    }

    @PostConstruct
    public void initialize()
    {
        List<Instrument> result = instrumentRepository.findAll();
        instruments.addAll(result);
        logger.info("Loaded instrument service with {} instruments(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
        instruments.clear();
        initialize();
    }
}
