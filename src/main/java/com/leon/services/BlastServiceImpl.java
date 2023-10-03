package com.leon.services;

import com.leon.models.Blast;
import com.leon.repositories.BlastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlastServiceImpl implements BlastService
{
    private static final Logger logger = LoggerFactory.getLogger(BlastServiceImpl.class);
    private Map<String, Blast> blastMap = new HashMap<>();

    @Autowired
    BlastRepository blastRepository;

    @PostConstruct
    public void initialize()
    {
        List<Blast> result = blastRepository.findAll();
        result.forEach(blast -> blastMap.put(blast.getBlastId(), blast));
        logger.info("Loaded blast service with {} blast(s).", result.size());
    }

    public void reconfigure()
    {
        blastMap.clear();
        initialize();
    }

    @Override
    public Blast saveBlast(Blast blastToSave)
    {
        Blast result = blastRepository.save(blastToSave);
        blastMap.put(result.getBlastId(), result);
        return result;
    }

    @Override
    public void deleteBlast(String blastId)
    {
        blastRepository.deleteById(blastId);
        blastMap.remove(blastId);
    }

    @Override
    public Blast updateBlast(Blast blastToUpdate)
    {
        Blast result = blastRepository.save(blastToUpdate);
        blastMap.put(result.getBlastId(), result);
        return result;
    }

    @Override
    public List<Blast> getBlasts()
    {
        return blastMap.values().stream().collect(Collectors.toList());
    }
}
