package com.leon.controllers;

import com.leon.services.BlastService;
import com.leon.services.ClientInterestService;
import com.leon.services.ClientService;
import com.leon.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController()
public class MainController
{
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private BlastService blastService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientInterestService clientInterestService;

    @RequestMapping("/heartbeat")
    public String heartBeat()
    {
        logger.debug("Received heartbeat request and will respond.");

        return "I am still here!";
    }

    @CrossOrigin
    @RequestMapping("/reconfigure")
    public void reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.configurationService.reconfigure();
        this.blastService.reconfigure();
        this.clientService.reconfigure();
        this.clientInterestService.reconfigure();
    }
}