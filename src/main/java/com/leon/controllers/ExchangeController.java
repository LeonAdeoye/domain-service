package com.leon.controllers;

import com.leon.models.Exchange;
import com.leon.services.ExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/exchange")
public class ExchangeController
{
    private static final Logger logger = LoggerFactory.getLogger(ExchangeController.class);
    @Autowired
    private ExchangeService exchangeService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.exchangeService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<Exchange>> getAll()
    {
        logger.info("Received request to get all exchanges.");
        return new ResponseEntity<>(this.exchangeService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=POST , consumes = "application/json"  ,produces = "application/json")
    public ResponseEntity<Exchange> createExchange(@RequestBody Exchange exchange) {
        if (exchange == null || exchange.getExchangeId() == null || exchange.getExchangeId().isEmpty()) {
            logger.error("Attempted to create an exchange with null or empty ID.");
            return null;
        }

        if(exchange.getExchangeName() == null || exchange.getExchangeName().isEmpty()) {
            logger.error("Attempted to create an exchange with null or empty name.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to create exchange: {}", exchange);
        Exchange createdExchange = exchangeService.createExchange(exchange);
        return new ResponseEntity<>(createdExchange, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/{exchangeId}", method = DELETE)
    public ResponseEntity<Void> deleteExchange(@PathVariable String exchangeId) {
        logger.info("Received request to delete exchange with ID: {}", exchangeId);
        exchangeService.deleteExchange(exchangeId);
        return ResponseEntity.noContent().build();
    }
}
