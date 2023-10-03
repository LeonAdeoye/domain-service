package com.leon.controllers;

import com.leon.services.BlastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/blast")
public class BlastController
{
    private static final Logger logger = LoggerFactory.getLogger(BlastController.class);
    @Autowired
    private BlastService blastService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public void reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.blastService.reconfigure();
    }

    @CrossOrigin
    @RequestMapping(value="/", method=POST)
    public void save()
    {
        logger.info("Received request to save blast.");
        this.blastService.saveBlast();
    }

    @CrossOrigin
    @RequestMapping(value="/{id}", method=DELETE)
    public void delete()
    {
        logger.info("Received request to delete blast.");
        this.blastService.deleteBlast();
    }

    @CrossOrigin
    @RequestMapping(value="/{id}", method=PUT)
    public void update()
    {
        logger.info("Received request to update blast.");
        this.blastService.updateBlast();
    }

    @CrossOrigin
    @RequestMapping(value="/", method=GET)
    public void getAll()
    {
        logger.info("Received request to get all blasts.");
        this.blastService.getBlasts();
    }
}
