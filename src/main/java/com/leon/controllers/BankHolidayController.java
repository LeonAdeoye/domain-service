package com.leon.controllers;

import com.leon.models.BankHoliday;
import com.leon.services.BankHolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/bankHoliday")
public class BankHolidayController
{
    private static final Logger logger = LoggerFactory.getLogger(BankHolidayController.class);
    
    @Autowired
    private BankHolidayService bankHolidayService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure bank holiday service.");
        bankHolidayService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<BankHoliday>> getAll()
    {
        logger.info("Received request to get all bank holidays.");
        return new ResponseEntity<>(bankHolidayService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/current", method=GET, produces = "application/json")
    public ResponseEntity<List<BankHoliday>> getBankHolidays()
    {
        logger.info("Received request to get current and future bank holidays.");
        return new ResponseEntity<>(bankHolidayService.getBankHolidays(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/country/{countryCode}", method=GET, produces = "application/json")
    public ResponseEntity<List<BankHoliday>> getBankHolidaysByCountry(@PathVariable String countryCode)
    {
        if (countryCode == null || countryCode.isEmpty())
        {
            logger.error("Received request to get bank holidays by country but country code was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to get bank holidays for country: {}", countryCode);
        return new ResponseEntity<>(bankHolidayService.getBankHolidaysByCountry(countryCode), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/dateRange", method=GET, produces = "application/json")
    public ResponseEntity<List<BankHoliday>> getBankHolidaysByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
    {
        if (startDate == null || endDate == null)
        {
            logger.error("Received request to get bank holidays by date range but start date or end date was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (startDate.isAfter(endDate))
        {
            logger.error("Received request to get bank holidays by date range but start date is after end date.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to get bank holidays between {} and {}.", startDate, endDate);
        return new ResponseEntity<>(bankHolidayService.getBankHolidaysByDateRange(startDate, endDate), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/nextYear", method=GET, produces = "application/json")
    public ResponseEntity<List<BankHoliday>> getHolidaysWithinNextYear()
    {
        logger.info("Received request to get holidays within next year.");
        return new ResponseEntity<>(bankHolidayService.getHolidaysWithinNextYear(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/check", method=GET, produces = "application/json")
    public ResponseEntity<Boolean> isBankHoliday(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String countryCode)
    {
        if (date == null)
        {
            logger.error("Received request to check if date is bank holiday but date was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to check if {} is a bank holiday for country: {}.", date, countryCode);
        boolean isHoliday = bankHolidayService.isBankHoliday(date, countryCode);
        return new ResponseEntity<>(isHoliday, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/businessDays", method=GET, produces = "application/json")
    public ResponseEntity<Integer> calculateBusinessDays(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String countryCode)
    {
        if (startDate == null || endDate == null)
        {
            logger.error("Received request to calculate business days but start date or end date was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (startDate.isAfter(endDate))
        {
            logger.error("Received request to calculate business days but start date is after end date.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to calculate business days between {} and {} for country: {}.", startDate, endDate, countryCode);
        int businessDays = bankHolidayService.calculateBusinessDays(startDate, endDate, countryCode);
        return new ResponseEntity<>(businessDays, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/countries", method=GET, produces = "application/json")
    public ResponseEntity<List<String>> getCountries()
    {
        logger.info("Received request to get supported countries.");
        return new ResponseEntity<>(bankHolidayService.getCountries(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BankHoliday> addBankHoliday(@RequestBody BankHoliday bankHoliday)
    {
        if (bankHoliday == null)
        {
            logger.error("Received request to add bank holiday but bank holiday was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (bankHoliday.getCountryCode() == null || bankHoliday.getCountryCode().isEmpty())
        {
            logger.error("Received request to add bank holiday but country code was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (bankHoliday.getHolidayName() == null || bankHoliday.getHolidayName().isEmpty())
        {
            logger.error("Received request to add bank holiday but holiday name was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (bankHoliday.getHolidayDate() == null)
        {
            logger.error("Received request to add bank holiday but holiday date was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try
        {
            logger.info("Received request to add bank holiday: {}", bankHoliday);
            BankHoliday addedHoliday = bankHolidayService.addBankHoliday(bankHoliday);
            return new ResponseEntity<>(addedHoliday, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("Failed to add bank holiday: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @RequestMapping(method=PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BankHoliday> updateBankHoliday(@RequestBody BankHoliday bankHoliday)
    {
        if (bankHoliday == null)
        {
            logger.error("Received request to update bank holiday but bank holiday was null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (bankHoliday.getId() == null || bankHoliday.getId().isEmpty())
        {
            logger.error("Received request to update bank holiday but ID was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try
        {
            logger.info("Received request to update bank holiday: {}", bankHoliday);
            BankHoliday updatedHoliday = bankHolidayService.updateBankHoliday(bankHoliday);
            
            if (updatedHoliday == null)
            {
                logger.warn("Bank holiday with ID {} not found. Cannot update.", bankHoliday.getId());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(updatedHoliday, HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("Failed to update bank holiday: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = DELETE)
    public ResponseEntity<Void> deleteBankHoliday(@PathVariable String id)
    {
        if (id == null || id.isEmpty())
        {
            logger.error("Received request to delete bank holiday but ID was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete bank holiday with ID: {}", id);
        bankHolidayService.deleteBankHoliday(id);
        return ResponseEntity.noContent().build();
    }
}
