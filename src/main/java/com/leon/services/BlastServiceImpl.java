package com.leon.services;

import com.leon.models.Blast;
import com.leon.models.ClientInterest;
import com.leon.repositories.BlastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class BlastServiceImpl implements BlastService
{
    private static final Logger logger = LoggerFactory.getLogger(BlastServiceImpl.class);
    private Map<String, List<Blast>> blastMap = new HashMap<>();
    @Autowired
    private BlastRepository blastRepository;

    @PostConstruct
    public void initialize()
    {
        List<Blast> result = blastRepository.findAll();
        result.forEach(blast ->
        {
            if(!blastMap.containsKey(blast.getOwnerId()))
                blastMap.put(blast.getOwnerId(), new ArrayList<>());
            List<Blast> blasts = blastMap.get(blast.getOwnerId());
            blasts.add(blast);
        });
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
        Optional<Blast> existingBlast = blastRepository.findById(blastToSave.getBlastId());
        if (existingBlast.isPresent())
        {
            logger.warn("Blast with ID {} already exists.", blastToSave.getBlastId().toString());
            return blastToSave;
        }
        Blast result = blastRepository.save(blastToSave);
        if(!blastMap.containsKey(blastToSave.getOwnerId()))
            blastMap.put(blastToSave.getOwnerId(), new ArrayList<>());
        List<Blast> blasts = blastMap.get(result.getOwnerId());
        blasts.add(result);
        return result;
    }

    @Override
    public void deleteBlast(String ownerId, String blastId)
    {
        Blast existingBlast = blastRepository.findById(UUID.fromString(blastId)).orElse(null);
        if (existingBlast == null)
        {
            logger.warn("Blast with ID {} does not exist.", blastId);
            return;
        }
        blastRepository.delete(existingBlast);
        List<Blast> blasts = blastMap.get(ownerId);
        blasts.removeIf(blast -> blast.getBlastId().equals(UUID.fromString(blastId)));
    }

    @Override
    public Blast updateBlast(Blast blastToUpdate)
    {
        Blast existingBlast = blastRepository.findById(blastToUpdate.getBlastId()).orElse(null);
        if (existingBlast == null)
        {
            logger.warn("Blast with ID {} does not exist.", blastToUpdate.getBlastId().toString());
            return blastToUpdate;
        }
        Blast updatedBlast = blastRepository.save(blastToUpdate);
        List<Blast> blasts = blastMap.get(updatedBlast.getOwnerId());
        blasts.removeIf(blast -> blast.getBlastId().equals(updatedBlast.getBlastId()));
        blasts.add(updatedBlast);
        return updatedBlast;
    }

    @Override
    public List<Blast> getBlasts(String ownerId)
    {
        return blastMap.get(ownerId);
    }
}
