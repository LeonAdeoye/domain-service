package com.leon.controllers;

import com.leon.models.Desk;
import com.leon.models.Trader;
import com.leon.services.DeskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/desk")
public class DeskController
{
    private static final Logger logger = LoggerFactory.getLogger(BlastController.class);
    @Autowired
    private DeskService deskService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.deskService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET)
    public ResponseEntity<List<Desk>> getAllDesks()
    {
        logger.info("Received request to get all desks.");
        return ResponseEntity.ok(deskService.getAllDesks());
    }

    @CrossOrigin
    @RequestMapping(value="/traders/{deskId}", method=GET)
    public ResponseEntity<List<Trader>> getTraders(@PathVariable String deskId)
    {
        if (deskId == null || deskId.isEmpty())
        {
            logger.error("Received request to get traders for desk but desk ID was null or empty.");
            return ResponseEntity.badRequest().build();
        }

        logger.info("Received request to get traders for desk.");
        List<Trader> traders = deskService.getTradersByDeskId(deskId);
        if (traders.isEmpty()) {
            logger.warn("No traders found for the desk.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(traders);
    }

        @CrossOrigin
        @RequestMapping(value = "/{deskId}", method = GET)
        public ResponseEntity<Desk> getDeskById(@PathVariable String deskId)
        {
            if (deskId == null || deskId.isEmpty())
            {
                logger.error("Received request to get desk but desk ID was null or empty.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to get desk by ID: {}", deskId);
            Desk desk = deskService.getDeskById(deskId);
            if (desk != null)
            {
                return ResponseEntity.ok(desk);
            }
            else
            {
                logger.warn("Desk with ID {} not found.", deskId);
                return ResponseEntity.notFound().build();
            }
        }

        @CrossOrigin
        @RequestMapping(method = POST)
        public ResponseEntity<Desk> createDesk(@RequestBody Desk desk)
        {
            if (desk == null || desk.getDeskId() == null || desk.getDeskId().toString().isEmpty())
            {
                logger.error("Attempted to create a desk with null or empty ID.");
                return ResponseEntity.badRequest().build();
            }

            if (desk.getDeskName() == null || desk.getDeskName().isEmpty())
            {
                logger.error("Attempted to create a desk with null or empty name.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to create a new desk.");
            Desk createdDesk = deskService.createDesk(desk);
            return new ResponseEntity<>(createdDesk, HttpStatus.CREATED);
        }

        @CrossOrigin
        @RequestMapping(method = PUT)
        public ResponseEntity<Desk> updateDesk(@RequestBody Desk desk)
        {
            if (desk == null || desk.getDeskId() == null || desk.getDeskId().toString().isEmpty())
            {
                logger.error("Attempted to update a desk with null or empty ID.");
                return ResponseEntity.badRequest().build();
            }

            if (desk.getDeskName() == null || desk.getDeskName().isEmpty())
            {
                logger.error("Attempted to update a desk with null or empty name.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to update desk with ID: {}", desk.getDeskId());
            Desk updatedDesk = deskService.updateDesk(desk);
            if (updatedDesk != null)
            {
                return ResponseEntity.ok(updatedDesk);
            }
            else
            {
                logger.warn("Desk with ID {} not found for update.", desk.getDeskId());
                return ResponseEntity.notFound().build();
            }
        }

        @CrossOrigin
        @RequestMapping(value = "/{deskId}", method = DELETE)
        public ResponseEntity<Void> deleteDesk(@PathVariable String deskId)
        {
            if (deskId == null || deskId.isEmpty())
            {
                logger.error("Received request to delete desk but desk ID was null or empty.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to delete desk with ID: {}", deskId);
            deskService.deleteDesk(deskId);
            return ResponseEntity.noContent().build();
        }

        @CrossOrigin
        @RequestMapping(value = "/addTrader/{deskId}/{traderId}", method = POST)
        public ResponseEntity<Void> addTraderToDesk(@PathVariable String deskId, @PathVariable String traderId)
        {
            if (deskId == null || deskId.isEmpty() || traderId == null || traderId.isEmpty())
            {
                logger.error("Received request to add trader to desk but desk ID or trader ID was null or empty.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to add trader {} to desk ID: {}", traderId, deskId);
            deskService.addTraderToDesk(deskId, traderId);
            return ResponseEntity.noContent().build();
        }

        @CrossOrigin
        @RequestMapping(value = "/removeTrader/{deskId}/{traderId}", method = DELETE)
        public ResponseEntity<Void> removeTraderFromDesk(@PathVariable String deskId, @PathVariable String traderId)
        {
            if (deskId == null || deskId.isEmpty() || traderId == null || traderId.isEmpty())
            {
                logger.error("Received request to remove trader from desk but desk ID or trader ID was null or empty.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to remove trader {} from desk ID: {}", traderId, deskId);
            deskService.removeTraderFromDesk(deskId, traderId);
            return ResponseEntity.noContent().build();
        }

        @CrossOrigin
        @RequestMapping(value = "/belongs/{deskId}/{traderId}", method = GET)
        public ResponseEntity<Boolean> doesTraderBelongToDesk(@PathVariable String deskId, @PathVariable String traderId)
        {
            if (deskId == null || deskId.isEmpty() || traderId == null || traderId.isEmpty())
            {
                logger.error("Received request to check if trader belongs to desk but desk ID or trader ID was null or empty.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to check if trader {} belongs to desk ID: {}", traderId, deskId);
            boolean belongs = deskService.doesTraderBelongToDesk(deskId, traderId);
            return ResponseEntity.ok(belongs);
        }

        @CrossOrigin
        @RequestMapping(value = "/deskByTrader/{traderId}", method = GET)
        public ResponseEntity<Desk> getDeskByTraderId(@PathVariable String traderId)
        {
            if (traderId == null || traderId.isEmpty())
            {
                logger.error("Received request to get desk for trader but trader ID was null or empty.");
                return ResponseEntity.badRequest().build();
            }

            logger.info("Received request to get desk for trader ID: {}", traderId);
            Desk desk = deskService.getDesk(traderId);
            if (desk != null)
            {
                return ResponseEntity.ok(desk);
            }
            else
            {
                logger.warn("No desk found for trader ID: {}", traderId);
                return ResponseEntity.notFound().build();
            }
        }
    }

