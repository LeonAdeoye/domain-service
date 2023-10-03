package com.leon.controllers;

import com.leon.models.ClientInterest;
import com.leon.services.ClientInterestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/interest")
public class ClientInterestController
{
    private static final Logger logger = LoggerFactory.getLogger(ClientInterestController.class);
    @Autowired
    private ClientInterestService clientInterestService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public void reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.clientInterestService.reconfigure();
    }
    @CrossOrigin
    @RequestMapping(value="/", method=GET)
    public List<ClientInterest> getAll()
    {
        logger.info("Received request to get all interests.");
        return this.clientInterestService.getAll();
    }

    @CrossOrigin
    @RequestMapping(value="/{clientId}", method=GET)
    public List<ClientInterest> getAllByClientId(@RequestParam String clientId)
    {
        if(clientId == null || clientId.isEmpty())
        {;
            return new ArrayList<>();
        }

        logger.info("Received request to get all interests.");
        return this.clientInterestService.getAllByClientId(clientId);
    }

    @CrossOrigin
    @RequestMapping(value="/", method=DELETE)
    public void delete(String clientInterestId)
    {
        if(clientInterestId == null || clientInterestId.isEmpty())
        {
            logger.error("Received request to delete interest but interest Id is null or empty.");
            return;
        }

        logger.info("Received request to delete interest with Id: {}.", clientInterestId);
        this.clientInterestService.delete(clientInterestId);
    }

    @CrossOrigin
    @RequestMapping(value="/", method=POST)
    public ClientInterest save(@RequestBody ClientInterest clientInterestToSave)
    {
        if(clientInterestToSave == null)
        {
            logger.error("Received request to save interest but interest is null.");
            return null;
        }

        logger.info("Received request to save interest: {}.", clientInterestToSave);
        return this.clientInterestService.save(clientInterestToSave);
    }

    @CrossOrigin
    @RequestMapping(value="/", method=PUT)
    public ClientInterest update(@RequestBody ClientInterest clientInterestToUpdate)
    {
        if(clientInterestToUpdate == null)
        {
            logger.error("Received request to update interest but interest is null.");
            return null;
        }

        logger.info("Received request to update interest: {}.", clientInterestToUpdate);
        return this.clientInterestService.update(clientInterestToUpdate);
    }
}
