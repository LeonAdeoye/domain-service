package com.leon.controllers;

import com.leon.models.Broker;
import com.leon.services.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/broker")
public class BrokerController {
    private static final Logger logger = LoggerFactory.getLogger(BrokerController.class);
    
    @Autowired
    private BrokerService brokerService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure broker service.");
        brokerService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<Broker>> getAll()
    {
        logger.info("Received request to get all brokers.");
        return new ResponseEntity<>(brokerService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Broker> createBroker(@RequestBody Broker broker)
    {
        if (broker == null || broker.getBrokerId() == null || broker.getBrokerId().toString().isEmpty())
        {
            logger.error("Attempted to create a broker with null or empty ID.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (broker.getBrokerAcronym() == null || broker.getBrokerAcronym().isEmpty())
        {
            logger.error("Attempted to create a broker with null or empty acronym.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to create broker: {}", broker);
        Broker createdBroker = brokerService.createBroker(broker);
        return new ResponseEntity<>(createdBroker, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(method=PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Broker> updateBroker(@RequestBody Broker broker)
    {
        if (broker == null || broker.getBrokerId() == null || broker.getBrokerId().toString().isEmpty())
        {
            logger.error("Attempted to update a broker with null or empty ID.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to update broker: {}", broker);
        Broker updatedBroker = brokerService.updateBroker(broker);
        if (updatedBroker != null)
        {
            return new ResponseEntity<>(updatedBroker, HttpStatus.OK);
        }
        else
        {
            logger.warn("Broker with ID {} not found. Cannot update.", broker.getBrokerId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/{brokerId}", method = DELETE)
    public ResponseEntity<Void> deleteBroker(@PathVariable String brokerId)
    {
        if (brokerId == null || brokerId.isEmpty())
        {
            logger.error("Received request to delete broker but broker ID was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete broker with ID: {}", brokerId);
        brokerService.deleteBroker(brokerId);
        return ResponseEntity.noContent().build();
    }
} 