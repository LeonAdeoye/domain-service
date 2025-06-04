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
import java.util.UUID;

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
        instruments.addAll(instrumentRepository.findAll());
        logger.info("Loaded instrument service with {} instruments(s).", instruments.size());
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
        Instrument existingInstrument = instrumentRepository.findById(instrument.getInstrumentId()).orElse(null);
        if (existingInstrument != null)
        {
            logger.warn("Instrument {} already exists. Not creating a new one.", instrument);
            return existingInstrument;
        }

        Instrument createdInstrument = instrumentRepository.save(instrument);
        instruments.add(createdInstrument);
        logger.info("Created new instrument: {}", createdInstrument);
        return createdInstrument;
    }

    @Override
    public void deleteInstrument(String instrumentId)
    {
        Instrument instrumentToDelete = instrumentRepository.findById(UUID.fromString(instrumentId)).orElse(null);
        if (instrumentToDelete != null)
        {
            instrumentRepository.delete(instrumentToDelete);
            instruments.removeIf(instrument -> instrument.getInstrumentId().equals(instrumentToDelete.getInstrumentId()));
            logger.info("Deleted instrument: {}", instrumentToDelete);
        }
        else
        {
            logger.warn("Attempted to delete non-existing instrument with Id: {}", instrumentId);
        }
    }

    @Override
    public Instrument updateInstrument(Instrument instrumentToUpdate) {
        Instrument existingInstrument = instrumentRepository.findById(instrumentToUpdate.getInstrumentId()).orElse(null);
        if (existingInstrument == null) {
            logger.warn("Instrument {} does not exist. Cannot update.", instrumentToUpdate);
            return null;
        }

        Instrument updatedInstrument = instrumentRepository.save(instrumentToUpdate);
        instruments.removeIf(instrument -> instrument.getInstrumentId().equals(existingInstrument.getInstrumentId()));
        instruments.add(updatedInstrument);
        logger.info("Updated instrument: {}", updatedInstrument);
        return updatedInstrument;
    }
}
