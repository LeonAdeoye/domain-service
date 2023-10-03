package com.leon.controllers;

import com.leon.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void delete()
    {
        logger.info("Received request to delete client.");
        this.clientService.delete();
    }

    @CrossOrigin
    @RequestMapping(value="/", method=GET)
    public void getAll()
    {
        logger.info("Received request to get all clients.");
        this.clientService.getAll();
    }
}
