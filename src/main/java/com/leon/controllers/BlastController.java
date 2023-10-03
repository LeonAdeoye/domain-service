package com.leon.controllers;

import com.leon.models.Blast;
import com.leon.services.BlastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
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
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.blastService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Blast> save(@RequestBody Blast blastToSave)
    {
        if(blastToSave == null)
        {
            logger.error("Received INVALID request to save blast but blast was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(blastToSave.getBlastId() != null && !blastToSave.getBlastId().isEmpty() )
        {
            logger.error("Received INVALID request to save blast but blast Id was NOT null or NOT empty. Cannot save blast!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to save blast: {}.", blastToSave);
        Blast savedBlast = this.blastService.saveBlast(blastToSave);

        if (savedBlast == null)
        {
            logger.error("Failed to save blast: {}.", blastToSave);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(savedBlast, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=DELETE)
    public ResponseEntity<Void> delete(@RequestParam String ownerId, @RequestParam String blastId)
    {
        if(blastId == null || blastId.isEmpty())
        {
            logger.error("Received INVALID request to delete blast but blast Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(ownerId == null || ownerId.isEmpty())
        {
            logger.error("Received INVALID request to delete blast but owner Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete blast with owner Id: {} and blast Id: {}", ownerId, blastId);
        this.blastService.deleteBlast(ownerId, blastId);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Blast> update(@RequestBody Blast blastToUpdate)
    {
        if(blastToUpdate == null)
        {
            logger.error("Received INVALID request to update blast but blast was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(blastToUpdate.getBlastId() == null || blastToUpdate.getBlastId().isEmpty())
        {
            logger.error("Received INVALID request to update blast but blast Id was null or empty. Cannot update blast without blast Id.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to update blast: {}.", blastToUpdate);
        Blast updatedBlast = this.blastService.updateBlast(blastToUpdate);

        if (updatedBlast == null)
        {
            logger.error("Failed to update blast: {}.", blastToUpdate);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedBlast, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<Blast>> getAll(@RequestParam String ownerId)
    {
        if(ownerId == null || ownerId.isEmpty())
        {
            logger.error("Received INVALID request to get all blast but owner Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to get all blasts for owner Id: {}.", ownerId);
        return new ResponseEntity<>(this.blastService.getBlasts(ownerId), HttpStatus.OK);
    }
}
