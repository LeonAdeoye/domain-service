package com.leon.services;

import com.leon.models.BankHoliday;
import com.leon.repositories.BankHolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class BankHolidayServiceImpl implements BankHolidayService
{
    private static final Logger logger = LoggerFactory.getLogger(BankHolidayServiceImpl.class);
    
    @Autowired
    private BankHolidayRepository bankHolidayRepository;
    
    private List<BankHoliday> bankHolidays = new ArrayList<>();
    private final List<String> countries = Arrays.asList("JP", "HK");

    @PostConstruct
    public void initialize()
    {
        logger.info("BankHolidayServiceImpl.initialize() called - starting initialization...");
        try
        {
            List<BankHoliday> result = bankHolidayRepository.findAll();
            logger.info("Repository query completed. Found {} existing holidays.", result.size());
        }
        catch (Exception e)
        {
            logger.error("Error during BankHolidayServiceImpl initialization: {}", e.getMessage(), e);
        }
    }

    @Override
    public void reconfigure()
    {
        bankHolidays.clear();
        initialize();
    }

    @Override
    public List<BankHoliday> getAll()
    {
        return bankHolidays;
    }

    @Override
    public List<BankHoliday> getBankHolidays()
    {
        LocalDate today = LocalDate.now();
        return bankHolidays.stream()
                .filter(holiday -> holiday.getHolidayDate().isAfter(today) || holiday.getHolidayDate().isEqual(today))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<BankHoliday> getBankHolidaysByCountry(String countryCode)
    {
        LocalDate today = LocalDate.now();
        return bankHolidays.stream()
                .filter(holiday -> holiday.getCountryCode().equals(countryCode) && 
                        (holiday.getHolidayDate().isAfter(today) || holiday.getHolidayDate().isEqual(today)))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<BankHoliday> getBankHolidaysByDateRange(LocalDate startDate, LocalDate endDate)
    {
        LocalDate today = LocalDate.now();
        LocalDate cutoffDate = LocalDate.of(2026, 8, 31);
        
        return bankHolidays.stream()
                .filter(holiday -> holiday.getHolidayDate().isAfter(startDate.minusDays(1)) && 
                        holiday.getHolidayDate().isBefore(endDate.plusDays(1)) &&
                        holiday.getHolidayDate().isBefore(cutoffDate.plusDays(1)) &&
                        (holiday.getHolidayDate().isAfter(today.minusDays(1)) || holiday.getHolidayDate().isEqual(today)))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<BankHoliday> getHolidaysWithinNextYear()
    {
        LocalDate today = LocalDate.now();
        LocalDate nextYear = today.plusYears(1);
        LocalDate cutoffDate = LocalDate.of(2026, 8, 31);
        
        return bankHolidays.stream()
                .filter(holiday -> holiday.getHolidayDate().isAfter(today.minusDays(1)) && 
                        holiday.getHolidayDate().isBefore(nextYear.plusDays(1)) &&
                        holiday.getHolidayDate().isBefore(cutoffDate.plusDays(1)))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean isBankHoliday(LocalDate date, String countryCode)
    {
        return bankHolidays.stream()
                .anyMatch(holiday -> holiday.getHolidayDate().isEqual(date) && 
                        (countryCode == null || holiday.getCountryCode().equals(countryCode)));
    }

    @Override
    public int calculateBusinessDays(LocalDate startDate, LocalDate endDate, String countryCode)
    {
        int businessDays = 0;
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate))
        {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY)
            {
                if (!isBankHoliday(currentDate, countryCode))
                {
                    businessDays++;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return businessDays;
    }

    @Override
    public BankHoliday addBankHoliday(BankHoliday bankHoliday)
    {
        if (bankHoliday.getHolidayDate() != null)
        {
            LocalDate cutoffDate = LocalDate.of(2026, 8, 31);
            if (bankHoliday.getHolidayDate().isAfter(cutoffDate))
            {
                throw new IllegalArgumentException("Cannot add holidays after August 2026");
            }
        }
        
        if (bankHoliday.getId() == null)
        {
            bankHoliday.setId(UUID.randomUUID().toString());
        }
        
        BankHoliday savedHoliday = bankHolidayRepository.save(bankHoliday);
        bankHolidays.add(savedHoliday);
        logger.info("Added bank holiday: {}", savedHoliday);
        return savedHoliday;
    }

    @Override
    public BankHoliday updateBankHoliday(BankHoliday bankHoliday)
    {
        if (bankHoliday.getHolidayDate() != null)
        {
            LocalDate cutoffDate = LocalDate.of(2026, 8, 31);
            if (bankHoliday.getHolidayDate().isAfter(cutoffDate))
            {
                throw new IllegalArgumentException("Cannot update holidays to dates after August 2026");
            }
        }
        
        BankHoliday existingHoliday = bankHolidayRepository.findById(bankHoliday.getId()).orElse(null);
        if (existingHoliday == null)
        {
            logger.warn("Bank holiday with ID {} does not exist. Cannot update.", bankHoliday.getId());
            return null;
        }
        
        BankHoliday updatedHoliday = bankHolidayRepository.save(bankHoliday);
        bankHolidays.removeIf(h -> h.getId().equals(existingHoliday.getId()));
        bankHolidays.add(updatedHoliday);
        logger.info("Updated bank holiday: {}", updatedHoliday);
        return updatedHoliday;
    }

    @Override
    public void deleteBankHoliday(String id)
    {
        BankHoliday holidayToDelete = bankHolidayRepository.findById(id).orElse(null);
        if (holidayToDelete != null)
        {
            bankHolidayRepository.delete(holidayToDelete);
            bankHolidays.removeIf(h -> h.getId().equals(id));
            logger.info("Deleted bank holiday: {}", holidayToDelete);
        }
        else
        {
            logger.warn("Attempted to delete non-existing bank holiday with ID: {}", id);
        }
    }

    @Override
    public List<String> getCountries()
    {
        return countries;
    }
}
