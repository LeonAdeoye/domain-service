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

// SAMPLE DATA
// [
//    {
//        "clientInterestId": "2dbdbc66-c51b-4d3f-87bf-7219ebd492e8",
//        "clientId": "f066ba8b-4a89-4f48-ab4a-f80a1a9be4de",
//        "notes": "Nomura notes",
//        "side": "BUY",
//        "instrumentCode": "8602.T",
//        "ownerId": "ladeoye"
//    }
//]

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
    public ResponseEntity<List<ClientInterest>> getAll(@RequestParam String ownerId)
    {
        if(ownerId == null || ownerId.isEmpty())
        {
            logger.error("Received request to get all client interests but owner Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to get all interests for owner Id: {}", ownerId);
        return new ResponseEntity<>(this.clientInterestService.getAll(ownerId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/{clientId}", method=GET, produces = "application/json")
    public ResponseEntity<List<ClientInterest>> getAllByClientId(@RequestParam String ownerId, @RequestParam String clientId)
    {
        if(ownerId == null || ownerId.isEmpty())
        {
            logger.error("Received request to get all client interests for and owner and client but owner Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(clientId == null || clientId.isEmpty())
        {
            logger.error("Received request to get all interests for a client but the client Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to get interests for a client with Id: {}.", clientId);
        return new ResponseEntity<>(this.clientInterestService.getAllByClientId(ownerId, clientId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=DELETE)
    public ResponseEntity<Void> delete(@RequestParam String ownerId, @RequestParam String clientInterestId)
    {
        if(clientInterestId == null || clientInterestId.isEmpty())
        {
            logger.error("Received request to delete client interest but client interest Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(ownerId == null || ownerId.isEmpty())
        {
            logger.error("Received request to delete client interest but owner Id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete interest with owner Id: {} and client interest Id: {}.", ownerId, clientInterestId);
        this.clientInterestService.delete(ownerId, clientInterestId);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ClientInterest> save(@RequestBody ClientInterest clientInterestToSave)
    {
        if(!ClientInterest.isValidateClientInterest(clientInterestToSave))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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
        if(!ClientInterest.isValidateClientInterest(clientInterestToUpdate))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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
