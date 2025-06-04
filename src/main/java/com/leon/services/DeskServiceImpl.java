package com.leon.services;

import com.leon.models.Desk;
import com.leon.models.Trader;
import com.leon.repositories.DeskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeskServiceImpl implements DeskService
{
    private static final Logger logger = LoggerFactory.getLogger(DeskServiceImpl.class);
    private List<Desk> desks = new ArrayList<>();
    @Autowired
    private DeskRepository deskRepository;
    @Autowired
    private TraderService traderService;

    @PostConstruct
    public void initialize()
    {
        List<Desk> result = deskRepository.findAll();
        desks.addAll(result);
        logger.info("Loaded desk service with {} desk(s).", desks.size());
    }

    public void reconfigure()
    {
        desks.clear();
        initialize();
    }

    @Override
    public Desk getDesk(String traderId)
    {
        return desks.stream()
                .filter(desk -> desk.getTraders().contains(UUID.fromString(traderId)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean doesTraderBelongToDesk(String deskId, String traderId)
    {
        return desks.stream()
                .filter(desk -> desk.getDeskId().toString().equals(deskId))
                .anyMatch(desk -> desk.getTraders().contains(UUID.fromString(traderId)));
    }


    @Override
    public List<Desk> getAllDesks()
    {
        return desks;
    }

    @Override
    public Desk getDeskById(String deskId) {
        return desks.stream()
                .filter(desk -> desk.getDeskId().toString().equals(deskId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Desk createDesk(Desk desk)
    {
        if(desks.stream().anyMatch(existingDesk -> existingDesk.getDeskId().equals(desk.getDeskId())) ||
           deskRepository.existsById(desk.getDeskId())) {
            logger.warn("Desk with ID {} already exists.", desk.getDeskId());
            return desk;
        }
        Desk savedDesk = deskRepository.save(desk);
        desks.add(savedDesk);
        logger.info("Created new desk with ID: {}", savedDesk.getDeskId());
        return savedDesk;
    }

    @Override
    public Desk updateDesk(Desk desk)
    {
        Optional<Desk> existingDeskOptional = deskRepository.findById(desk.getDeskId());
        if (existingDeskOptional.isPresent())
        {
            Desk updatedDesk = deskRepository.save(desk);
            desks.removeIf(deskToRemove -> deskToRemove.getDeskId().equals(desk.getDeskId()));
            desks.add(updatedDesk);
            logger.info("Updated desk with ID: {}", updatedDesk.getDeskId());
            return updatedDesk;
        }
        else
        {
            logger.warn("Desk with ID {} not found for update.", desk.getDeskId());
            return null;
        }
    }

    @Override
    public void deleteDesk(String deskId)
    {
        Optional<Desk> deskOptional = deskRepository.findById(UUID.fromString(deskId));
        if (deskOptional.isPresent())
        {
            deskRepository.deleteById(UUID.fromString(deskId));
            desks.remove(deskOptional.get());
            logger.info("Deleted desk with ID: {}", deskId);
        }
        else
        {
            logger.warn("Desk with ID {} not found for deletion.", deskId);
        }
    }

    @Override
    public void addTraderToDesk(String deskId, String traderId)
    {
        Desk desk = desks.stream()
                .filter(d -> d.getDeskId().toString().equals(deskId))
                .findFirst()
                .orElse(null);

        if (desk != null)
        {
            if (!desk.getTraders().contains(UUID.fromString(traderId)))
            {
                desk.getTraders().add(UUID.fromString(traderId));
                deskRepository.save(desk);
                logger.info("Added trader {} to desk with ID: {}", traderId, deskId);
            }
            else
                logger.warn("Trader {} does not exists or is already assigned to desk with ID: {}", traderId, deskId);
        }
        else
            logger.warn("Desk with ID {} not found for adding trader.", deskId);
    }

    @Override
    public void removeTraderFromDesk(String deskId, String traderId)
    {
        Optional<Desk> deskOptional = deskRepository.findById(UUID.fromString(deskId));
        if (deskOptional.isPresent())
        {
            Desk desk = deskOptional.get();
            if (desk.getTraders().remove(traderId))
            {
                deskRepository.save(desk);
                logger.info("Removed trader {} from desk with ID: {}", traderId, deskId);
            }
            else
                logger.warn("Trader {} not found in desk with ID: {}", traderId, deskId);
        }
        else
            logger.warn("Desk with ID {} not found for removing trader.", deskId);
    }

    @Override
    public List<Trader> getTradersByDeskId(String deskId)
    {
        return desks.stream()
                .filter(desk -> desk.getDeskId().toString().equals(deskId))
                .flatMap(desk -> desk.getTraders().stream())
                .map(traderId -> traderService.getTraderById(traderId.toString()))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
