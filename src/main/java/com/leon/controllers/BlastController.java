package com.leon.controllers;

import com.leon.models.Blast;
import com.leon.services.BlastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public Blast save(@RequestBody Blast blastToSave)
    {
        if(blastToSave == null)
        {
            logger.error("Received request to save blast but blast is null.");
            return null;
        }

        logger.info("Received request to save blast: {}.", blastToSave);
        return this.blastService.saveBlast(blastToSave);
    }

    @CrossOrigin
    @RequestMapping(value="/{id}", method=DELETE)
    public void delete(@RequestParam String blastId)
    {
        if(blastId == null || blastId.isEmpty())
        {
            logger.error("Received request to delete blast but blast Id is null or empty.");
            return;
        }

        logger.info("Received request to delete blast with Id: {}.", blastId);
        this.blastService.deleteBlast(blastId);
    }

    @CrossOrigin
    @RequestMapping(value="/{id}", method=PUT)
    public Blast update(@RequestBody Blast blastToUpdate)
    {
        if(blastToUpdate == null)
        {
            logger.error("Received request to update blast but blast is null.");
            return null;
        }

        logger.info("Received request to update blast: {}.", blastToUpdate);
        return this.blastService.updateBlast(blastToUpdate);
    }

    @CrossOrigin
    @RequestMapping(value="/", method=GET)
    public void getAll()
    {
        logger.info("Received request to get all blasts.");
        this.blastService.getBlasts();
    }
}
