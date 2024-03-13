package com.leon.controllers;

import com.leon.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private NewsService newsService;

    @RequestMapping("/heartbeat")
    public ResponseEntity<String> heartBeat()
    {
        logger.debug("Received heartbeat request and will respond.");
        return new ResponseEntity<>("I am still here!", HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping("/reconfigure")
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.configurationService.reconfigure();
        this.blastService.reconfigure();
        this.clientService.reconfigure();
        this.clientInterestService.reconfigure();
        this.instrumentService.reconfigure();
        this.newsService.reconfigure();
        return ResponseEntity.noContent().build();
    }
}