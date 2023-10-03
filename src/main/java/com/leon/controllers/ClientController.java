package com.leon.controllers;

import com.leon.models.Client;
import com.leon.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController()
@RequestMapping("/client")
public class ClientController
{
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    @Autowired
    private ClientService clientService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public void reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.clientService.reconfigure();
    }

    @CrossOrigin
    @RequestMapping(value="/", method=DELETE)
    public void delete(@RequestParam String clientId)
    {
        if(clientId == null || clientId.isEmpty())
            throw new IllegalArgumentException("clientId is null or empty.");

        logger.info("Received request to delete client with Id: {}.", clientId);
        this.clientService.delete(clientId);
    }

    public Client save(@RequestBody Client clientToSave)
    {
        if(clientToSave == null)
        {
            logger.error("Received request to save client but client is null.");
            return null;
        }

        logger.info("Received request to save client: {}.", clientToSave.toString());
        return this.clientService.save(clientToSave);
    }

    @CrossOrigin
    @RequestMapping(value="/", method=GET)
    public void getAll()
    {
        logger.info("Received request to get all clients.");
        this.clientService.getAll();
    }
}
