package com.leon.services;

import com.leon.models.Broker;
import com.leon.repositories.BrokerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BrokerServiceImpl implements BrokerService {
    private static final Logger logger = LoggerFactory.getLogger(BrokerServiceImpl.class);
    private List<Broker> brokers = new ArrayList<>();
    
    @Autowired
    private BrokerRepository brokerRepository;

    @PostConstruct
    public void initialize()
    {
        List<Broker> result = brokerRepository.findAll();
        brokers.addAll(result);
        logger.info("Loaded broker service with {} broker(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
        brokers.clear();
        initialize();
    }

    @Override
    public List<Broker> getAll() {
        return brokers;
    }

    @Override
    public void deleteBroker(String brokerId)
    {
        Broker brokerToDelete = brokerRepository.findById(UUID.fromString(brokerId)).orElse(null);
        if (brokerToDelete != null)
        {
            brokerRepository.delete(brokerToDelete);
            brokers.remove(brokerToDelete);
            logger.info("Deleted broker with ID: {}", brokerId);
        }
        else
            logger.warn("Attempted to delete non-existing broker with ID: {}", brokerId);
    }

    @Override
    public Broker createBroker(Broker broker)
    {
        Broker existingBroker = brokerRepository.findById(broker.getBrokerId()).orElse(null);
        if (existingBroker != null)
        {
            logger.warn("Broker with ID {} already exists. Not creating a new one.", broker.getBrokerId());
            return null;
        }

        Broker createdBroker = brokerRepository.save(broker);
        brokers.add(createdBroker);
        logger.info("Created new broker with ID: {}", createdBroker.getBrokerId());
        return createdBroker;
    }

    @Override
    public Broker updateBroker(Broker broker)
    {
        Broker existingBroker = brokerRepository.findById(broker.getBrokerId()).orElse(null);
        if (existingBroker == null)
        {
            logger.warn("Attempted to update non-existing broker with ID: {}", broker.getBrokerId());
            return null;
        }

        Broker updatedBroker = brokerRepository.save(broker);
        brokers.remove(existingBroker);
        brokers.add(updatedBroker);
        logger.info("Updated broker with ID: {}", updatedBroker.getBrokerId());
        return updatedBroker;
    }
} 