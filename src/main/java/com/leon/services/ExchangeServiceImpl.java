package com.leon.services;

import com.leon.models.Exchange;
import com.leon.repositories.ExchangeRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
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
        exchanges.clear();
        List<Exchange> result = exchangeRepository.findAll();
        exchanges.addAll(result);
        logger.info("Loaded exchange service with {} exchange(s).", result.size());
        seedSessionHours();
    }

    private void seedSessionHours()
    {
        seedExisting("HKSE", "Asia/Hong_Kong", "09:30", "16:00", "12:00", "13:00", "HKD");
        seedExisting("TSE", "Asia/Tokyo", "09:00", "15:00", "11:30", "12:30", "JPY");
        seedExisting("LSE", "Europe/London", "08:00", "16:30", "", "", "GBP");
        seedExisting("NYSE", "America/New_York", "09:30", "16:00", "", "", "USD");
        seedOrCreate("OSE", "Osaka Stock Exchange", "Asia/Tokyo", "09:00", "15:00", "11:30", "12:30", "JPY");
    }

    private void seedExisting(String acronym, String timezone, String openTime, String closeTime, String lunchStart, String lunchEnd, String currency)
    {
        Exchange exchange = findByAcronym(acronym);
        if (exchange == null)
        {
            logger.warn("Exchange {} not found; skipping session-hour seed", acronym);
            return;
        }

        if (hasSessionHours(exchange))
            return;

        applySessionHours(exchange, timezone, openTime, closeTime, lunchStart, lunchEnd, currency);
        Exchange saved = exchangeRepository.save(exchange);
        replaceInCache(saved);
        logger.info("Seeded session hours for {}", acronym);
    }

    private void seedOrCreate(String acronym, String exchangeName, String timezone, String openTime, String closeTime, String lunchStart, String lunchEnd, String currency)
    {
        Exchange exchange = findByAcronym(acronym);
        if (exchange == null)
        {
            Exchange created = new Exchange();
            created.setExchangeName(exchangeName);
            created.setExchangeAcronym(acronym);
            applySessionHours(created, timezone, openTime, closeTime, lunchStart, lunchEnd, currency);
            Exchange saved = exchangeRepository.save(created);
            exchanges.add(saved);
            logger.info("Created exchange {} with session hours", acronym);
            return;
        }

        seedExisting(acronym, timezone, openTime, closeTime, lunchStart, lunchEnd, currency);
    }

    private Exchange findByAcronym(String acronym)
    {
        return exchanges.stream()
                .filter(exchange -> acronym.equalsIgnoreCase(exchange.getExchangeAcronym()))
                .findFirst()
                .orElse(null);
    }

    private boolean hasSessionHours(Exchange exchange)
    {
        return exchange.getOpenTime() != null && !exchange.getOpenTime().isBlank()
                && exchange.getCloseTime() != null && !exchange.getCloseTime().isBlank();
    }

    private void applySessionHours(Exchange exchange, String timezone, String openTime, String closeTime, String lunchStart, String lunchEnd, String currency)
    {
        exchange.setTimezone(timezone);
        exchange.setOpenTime(openTime);
        exchange.setCloseTime(closeTime);
        exchange.setLunchStart(lunchStart);
        exchange.setLunchEnd(lunchEnd);
        exchange.setCurrency(currency);
    }

    private void replaceInCache(Exchange updated)
    {
        exchanges.removeIf(exchange -> exchange.getExchangeId().equals(updated.getExchangeId()));
        exchanges.add(updated);
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
    public Exchange updateExchange(Exchange exchangeToUpdate)
    {
        Exchange existingExchange = exchangeRepository.findById(exchangeToUpdate.getExchangeId()).orElse(null);
        if (existingExchange == null)
        {
            logger.warn("Exchange with ID {} does not exist. Cannot update.", exchangeToUpdate.getExchangeId());
            return null;
        }

        Exchange updatedExchange = exchangeRepository.save(exchangeToUpdate);
        exchanges.removeIf(exchange -> exchange.getExchangeId().equals(existingExchange.getExchangeId()));
        exchanges.add(updatedExchange);
        logger.info("Updated exchange: {}", updatedExchange);
        return updatedExchange;
    }

    @Override
    public void deleteExchange(String exchangeId)
    {
        Exchange exchangeToDelete = exchangeRepository.findById(UUID.fromString(exchangeId)).orElse(null);
        if (exchangeToDelete != null)
        {
            exchangeRepository.delete(exchangeToDelete);
            exchanges.remove(exchangeToDelete);
            logger.info("Deleted exchange with ID: {}", exchangeId);
        }
        else
            logger.warn("Attempted to delete non-existing exchange with ID: {}", exchangeId);
    }

    @Override
    public Exchange getByAcronym(String exchangeAcronym)
    {
        if (exchangeAcronym == null || exchangeAcronym.isBlank())
            return null;

        return exchanges.stream()
                .filter(exchange -> exchangeAcronym.equalsIgnoreCase(exchange.getExchangeAcronym()))
                .findFirst()
                .orElseGet(() -> exchangeRepository.findByExchangeAcronymIgnoreCase(exchangeAcronym));
    }
}
