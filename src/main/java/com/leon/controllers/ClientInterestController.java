package com.leon.controllers;

import com.leon.models.ClientInterest;
import com.leon.services.ClientInterestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.clientInterestService.reconfigure();
        return ResponseEntity.noContent().build();
    }
    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<ClientInterest>> getAll()
    {
        logger.info("Received request to get all interests.");
        return new ResponseEntity<>(this.clientInterestService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/{clientId}", method=GET, produces = "application/json")
    public ResponseEntity<List<ClientInterest>> getAllByClientId(@RequestParam String clientId)
    {
        if(clientId == null || clientId.isEmpty())
        {
            logger.error("Received request to get all interests for a client but the client Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to get interests for a client with Id: {}.", clientId);
        return new ResponseEntity<>(this.clientInterestService.getAllByClientId(clientId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=DELETE)
    public ResponseEntity<Void> delete(@RequestParam String clientInterestId)
    {
        if(clientInterestId == null || clientInterestId.isEmpty())
        {
            logger.error("Received request to delete client interest but client interest Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete interest with Id: {}.", clientInterestId);
        this.clientInterestService.delete(clientInterestId);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ClientInterest> save(@RequestBody ClientInterest clientInterestToSave)
    {
        if(clientInterestToSave == null)
        {
            logger.error("Received request to save client interest but client interest was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(clientInterestToSave.getClientInterestId() != null && !clientInterestToSave.getClientInterestId().isEmpty())
        {
            logger.error("Received INVALID request to save client interest but client interest Id was NOT null or NOT empty. Cannot save client interest!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to save interest: {}.", clientInterestToSave);
        ClientInterest savedClientInterest = this.clientInterestService.save(clientInterestToSave);

        if (savedClientInterest == null)
        {
            logger.error("Failed to save interest: {}.", clientInterestToSave);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(savedClientInterest, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ClientInterest> update(@RequestBody ClientInterest clientInterestToUpdate)
    {
        if(clientInterestToUpdate == null)
        {
            logger.error("Received request to update client interest but client interest was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(clientInterestToUpdate.getClientInterestId() == null || clientInterestToUpdate.getClientInterestId().isEmpty() )
        {
            logger.error("Received INVALID request to update client interest but client interest Id was null or empty. Cannot update client interest without valid client interest Id.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to update interest: {}.", clientInterestToUpdate);
        ClientInterest updatedClient = this.clientInterestService.update(clientInterestToUpdate);

        if (updatedClient == null)
        {
            logger.error("Failed to update interest: {}.", clientInterestToUpdate);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }
}
