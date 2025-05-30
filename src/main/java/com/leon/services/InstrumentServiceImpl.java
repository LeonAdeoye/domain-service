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

    @Override
    public Instrument createInstrument(Instrument instrument)
    {
        Instrument existingInstrument = instrumentRepository.findById(instrument.getInstrumentCode()).orElse(null);
        if (existingInstrument != null)
        {
            logger.warn("Instrument with code {} already exists. Not creating a new one.", instrument.getInstrumentCode());
            return existingInstrument;
        }

        Instrument createdInstrument = instrumentRepository.save(instrument);
        instruments.add(createdInstrument);
        logger.info("Created new instrument with code: {}", createdInstrument.getInstrumentCode());
        return createdInstrument;
    }

    @Override
    public void deleteInstrument(String instrumentCode)
    {
        Instrument instrumentToDelete = instrumentRepository.findById(instrumentCode).orElse(null);
        if (instrumentToDelete != null)
        {
            instrumentRepository.delete(instrumentToDelete);
            instruments.remove(instrumentToDelete);
            logger.info("Deleted instrument with ID: {}", instrumentCode);
        }
        else
        {
            logger.warn("Attempted to delete non-existing instrument with code: {}", instrumentCode);
        }
    }
}
