package com.leon.controllers;

import com.leon.services.ClientInterestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void getAll()
    {
        logger.info("Received request to get all interests.");
        this.clientInterestService.getAll();
    }

    @CrossOrigin
    @RequestMapping(value="/", method=DELETE)
    public void delete()
    {
        logger.info("Received request to delete interest.");
        this.clientInterestService.delete();
    }

    @CrossOrigin
    @RequestMapping(value="/", method=POST)
    public void save()
    {
        logger.info("Received request to save interest.");
        this.clientInterestService.save();
    }

    @CrossOrigin
    @RequestMapping(value="/", method=PUT)
    public void update()
    {
        logger.info("Received request to update interest.");
        this.clientInterestService.update();
    }
}
