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
}
