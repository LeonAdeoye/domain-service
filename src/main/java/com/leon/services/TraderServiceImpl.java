package com.leon.services;

import com.leon.models.Trader;
import com.leon.repositories.TraderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TraderServiceImpl  implements TraderService
{
    private static final Logger logger = LoggerFactory.getLogger(TraderServiceImpl.class);
    private List<Trader> traders = new ArrayList<>();
    @Autowired
    private TraderRepository traderRepository;

    @PostConstruct
    public void initialize()
    {
        traders.addAll(traderRepository.findAll());
        logger.info("Loaded trader service with {} trader(s).", traders.size());
    }

    @Override
    public void reconfigure() {
        traders.clear();
        initialize();
    }

    @Override
    public List<Trader> getAllTraders()
    {
        return traders;
    }

    @Override
    public Trader getTraderById(String traderId)
    {
        return traders.stream()
                .filter(trader -> trader.getTraderId().toString().equals(traderId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Trader createTrader(Trader trader)
    {
        Trader existingTrader = traderRepository.findById(trader.getTraderId()).orElse(null);
        if (existingTrader != null) {
            logger.warn("Trader with ID {} already exists. Not creating a new one.", trader.getTraderId());
            return existingTrader;
        }

        Trader createdTrader = traderRepository.save(trader);
        traders.add(createdTrader);
        logger.info("Created new trader with ID: {}", createdTrader.getTraderId());
        return createdTrader;
    }

    @Override
    public Trader updateTrader(Trader trader)
    {
        Trader existingTrader = traderRepository.findById(trader.getTraderId()).orElse(null);
        if (existingTrader == null) {
            logger.warn("Trader with ID {} does not exist. Cannot update.", trader.getTraderId());
            return null;
        }

        Trader updatedTrader = traderRepository.save(trader);
        traders.remove(existingTrader);
        traders.add(updatedTrader);
        logger.info("Updated trader with ID: {}", updatedTrader.getTraderId());
        return updatedTrader;
    }

    @Override
    public void deleteTrader(String traderId)
    {
        Trader traderToDelete = traderRepository.findById(UUID.fromString(traderId)).orElse(null);
        if (traderToDelete != null) {
            traderRepository.delete(traderToDelete);
            traders.remove(traderToDelete);
            logger.info("Deleted trader with ID: {}", traderId);
        }
        else
            logger.warn("Attempted to delete non-existing trader with ID: {}", traderId);
    }

    @Override
    public boolean doesTraderExist(String traderId)
    {
        Trader trader = traders.stream()
                .filter(t -> t.getTraderId().toString().equals(traderId))
                .findFirst()
                .orElse(null);
        if (trader != null) {
            logger.info("Trader with ID {} exists.", traderId);
            return true;
        } else {
            logger.warn("Trader with ID {} does not exist.", traderId);
            return false;
        }
    }
}
