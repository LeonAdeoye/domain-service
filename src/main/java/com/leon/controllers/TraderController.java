package com.leon.controllers;

import com.leon.models.Trader;
import com.leon.services.TraderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/trader")
public class TraderController
{
    private static final Logger logger = LoggerFactory.getLogger(TraderController.class);
    @Autowired
    private TraderService traderService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure trader service.");
        traderService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET)
    public ResponseEntity<List<Trader>> getAllTraders()
    {
        logger.info("Received request to get all desks.");
        return ResponseEntity.ok(traderService.getAllTraders());
    }

    @CrossOrigin
    @RequestMapping(method=DELETE)
    public ResponseEntity<Void> deleteTrader(String traderId)
    {
        logger.info("Received request to delete trader with ID: {}", traderId);

        if (traderService.doesTraderExist(traderId)) {
            traderService.deleteTrader(traderId);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Trader with ID {} does not exist.", traderId);
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @RequestMapping(value="/{traderId}", method=GET)
    public ResponseEntity<Trader> getTraderById(String traderId)
    {
        logger.info("Received request to get trader with ID: {}", traderId);
        Trader trader = traderService.getTraderById(traderId);

        if (trader != null) {
            return ResponseEntity.ok(trader);
        } else {
            logger.warn("Trader with ID {} not found.", traderId);
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Trader> createTrader(@RequestBody Trader trader) {
        logger.info("Creating new trader.");
        Trader createdTrader = traderService.createTrader(trader);
        return ResponseEntity.ok(createdTrader);
    }

    @PutMapping
    public ResponseEntity<Trader> updateTrader(@RequestBody Trader trader) {
        logger.info("Updating trader with ID: {}", trader.getTraderId());
        Trader updatedTrader = traderService.updateTrader(trader);
        if (updatedTrader != null) {
            return ResponseEntity.ok(updatedTrader);
        } else {
            logger.warn("Trader with ID {} not found. Cannot update.", trader.getTraderId());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exists/{traderId}")
    public ResponseEntity<Boolean> doesTraderExist(@PathVariable String traderId) {
        logger.info("Checking existence of trader with ID: {}", traderId);
        boolean exists = traderService.doesTraderExist(traderId);
        return ResponseEntity.ok(exists);
    }
}