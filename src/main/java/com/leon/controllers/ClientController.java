package com.leon.controllers;

import com.leon.models.Client;
import com.leon.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/client")
public class ClientController
{
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    @Autowired
    private ClientService clientService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.clientService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=DELETE)
    public ResponseEntity<Void> delete(@RequestParam String clientId)
    {
        if(clientId == null || clientId.isEmpty())
        {
            logger.error("Received request to delete client but client Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete client with Id: {}.", clientId);
        this.clientService.delete(clientId);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Client> save(@RequestBody Client clientToSave)
    {
        if(clientToSave == null)
        {
            logger.error("Received request to save client but client was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(clientToSave.getClientName() == null || clientToSave.getClientName().isEmpty() )
        {
            logger.error("Received INVALID request to save client but client name was null or empty. Cannot save client.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to save client: {}.", clientToSave.toString());
        Client savedClient = this.clientService.save(clientToSave);

        if (savedClient == null)
        {
            logger.error("Failed to save client: ", clientToSave);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(savedClient, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<Client>> getAll()
    {
        logger.info("Received request to get all clients.");
        return new ResponseEntity<>(this.clientService.getAll(), HttpStatus.OK);
    }
}
