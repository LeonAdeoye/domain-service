package com.leon.controllers;

import com.leon.models.Instrument;
import com.leon.services.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/instrument")
public class InstrumentController
{
    private static final Logger logger = LoggerFactory.getLogger(InstrumentController.class);
    @Autowired
    private InstrumentService instrumentService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.instrumentService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<Instrument>> getAll()
    {
        logger.info("Received request to get all instruments.");
        return new ResponseEntity<>(this.instrumentService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=POST , consumes = "application/json"  ,produces = "application/json")
    public ResponseEntity<Instrument> createInstrument(@RequestBody Instrument instrument)
    {
        if(!Instrument.isValid(instrument))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        logger.info("Received request to create instrument: {}", instrument);
        Instrument createdInstrument = instrumentService.createInstrument(instrument);
        return new ResponseEntity<>(createdInstrument, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/{instrumentId}", method = DELETE)
    public ResponseEntity<Void> deleteInstrument(@PathVariable String instrumentId)
    {
        if (instrumentId == null || instrumentId.isEmpty())
        {
            logger.error("Received request to delete instrument but instrument id was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete instrument with ID: {}", instrumentId);
        instrumentService.deleteInstrument(instrumentId);
        return ResponseEntity.noContent().build();
    }
}
