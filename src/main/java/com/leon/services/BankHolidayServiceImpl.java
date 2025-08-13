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
            
            if (result.isEmpty())
            {
                logger.info("No bank holidays found in database. Initializing with default holidays.");
                initializeDefaultHolidays();
                logger.info("Default holidays initialization completed. Total holidays in memory: {}", bankHolidays.size());
            }
            else
            {
                bankHolidays.addAll(result);
                logger.info("Loaded bank holiday service with {} holiday(s).", result.size());
            }
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

    private void initializeDefaultHolidays()
    {
        logger.info("Starting initializeDefaultHolidays()...");
        int currentYear = LocalDate.now().getYear();
        int nextYear = currentYear + 1;
        int yearAfterNext = currentYear + 2;
        LocalDate cutoffDate = LocalDate.of(yearAfterNext, 12, 31);
        
        logger.info("Generating holidays for years: {}, {}, {} (cutoff: {})", currentYear, nextYear, yearAfterNext, cutoffDate);
        
        for (String countryCode : countries)
        {
            logger.info("Generating holidays for country: {}", countryCode);
            addDefaultHolidaysForCountry(countryCode, currentYear);
            addDefaultHolidaysForCountry(countryCode, nextYear);
            addDefaultHolidaysForCountry(countryCode, yearAfterNext);
        }
        
        logger.info("Generated {} holidays before filtering", bankHolidays.size());
        
        // Filter out holidays that are too far in the future
        bankHolidays = bankHolidays.stream()
                .filter(holiday -> !holiday.getHolidayDate().isAfter(cutoffDate))
                .collect(java.util.stream.Collectors.toList());
        
        logger.info("After filtering, {} holidays remain. Saving to database...", bankHolidays.size());
        
        try
        {
            // Save all holidays to database
            List<BankHoliday> savedHolidays = bankHolidayRepository.saveAll(bankHolidays);
            logger.info("Successfully saved {} holidays to database", savedHolidays.size());
        }
        catch (Exception e)
        {
            logger.error("Error saving holidays to database: {}", e.getMessage(), e);
        }
        
        logger.info("Initialized bank holidays for {} countries for {}-{}, filtered to exclude holidays after December {}", 
                countries.size(), currentYear, yearAfterNext, yearAfterNext);
    }

    private void addDefaultHolidaysForCountry(String countryCode, int year)
    {
        List<BankHoliday> holidays = getDefaultHolidaysForCountry(countryCode, year);
        for (BankHoliday holiday : holidays)
        {
            holiday.setId(UUID.randomUUID().toString());
            bankHolidays.add(holiday);
        }
    }

    private List<BankHoliday> getDefaultHolidaysForCountry(String countryCode, int year)
    {
        List<BankHoliday> holidays = new ArrayList<>();
        
        switch (countryCode)
        {
            case "JP":
                holidays.add(new BankHoliday("JP", "New Year's Day", LocalDate.of(year, 1, 1), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Coming of Age Day", getNthMondayOfMonth(year, 1, 2), true, "Second Monday in January"));
                holidays.add(new BankHoliday("JP", "National Foundation Day", LocalDate.of(year, 2, 11), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Emperor's Birthday", LocalDate.of(year, 2, 23), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Vernal Equinox Day", LocalDate.of(year, 3, 20), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Showa Day", LocalDate.of(year, 4, 29), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Constitution Memorial Day", LocalDate.of(year, 5, 3), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Greenery Day", LocalDate.of(year, 5, 4), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Children's Day", LocalDate.of(year, 5, 5), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Marine Day", getNthMondayOfMonth(year, 7, 3), true, "Third Monday in July"));
                holidays.add(new BankHoliday("JP", "Mountain Day", LocalDate.of(year, 8, 11), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Respect for the Aged Day", getNthMondayOfMonth(year, 9, 3), true, "Third Monday in September"));
                holidays.add(new BankHoliday("JP", "Autumnal Equinox Day", LocalDate.of(year, 9, 23), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Sports Day", getNthMondayOfMonth(year, 10, 2), true, "Second Monday in October"));
                holidays.add(new BankHoliday("JP", "Culture Day", LocalDate.of(year, 11, 3), true, "National holiday"));
                holidays.add(new BankHoliday("JP", "Labor Thanksgiving Day", LocalDate.of(year, 11, 23), true, "National holiday"));
                break;
                
            case "HK":
                holidays.add(new BankHoliday("HK", "New Year's Day", LocalDate.of(year, 1, 1), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Chinese New Year", getChineseNewYear(year), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Good Friday", getGoodFriday(year), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Easter Monday", getEasterMonday(year), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Ching Ming Festival", LocalDate.of(year, 4, 5), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Labour Day", LocalDate.of(year, 5, 1), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Buddha's Birthday", LocalDate.of(year, 5, 15), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Tuen Ng Festival", LocalDate.of(year, 6, 22), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Hong Kong Special Administrative Region Establishment Day", LocalDate.of(year, 7, 1), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Chung Yeung Festival", LocalDate.of(year, 10, 15), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Christmas Day", LocalDate.of(year, 12, 25), true, "Public holiday"));
                holidays.add(new BankHoliday("HK", "Boxing Day", LocalDate.of(year, 12, 26), true, "Public holiday"));
                break;
                
            default:
                holidays.add(new BankHoliday(countryCode, "New Year's Day", LocalDate.of(year, 1, 1), true, "Public holiday"));
                holidays.add(new BankHoliday(countryCode, "Christmas Day", LocalDate.of(year, 12, 25), true, "Public holiday"));
                break;
        }
        
        return holidays;
    }

    private LocalDate getNthMondayOfMonth(int year, int month, int nth)
    {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate firstMonday = firstDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        return firstMonday.plusWeeks(nth - 1);
    }

    private LocalDate getGoodFriday(int year)
    {
        // Simplified Easter calculation (Meeus/Jones/Butcher algorithm)
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;
        
        LocalDate easter = LocalDate.of(year, month, day);
        return easter.minusDays(2);
    }

    private LocalDate getEasterMonday(int year)
    {
        LocalDate goodFriday = getGoodFriday(year);
        return goodFriday.plusDays(3);
    }

    private LocalDate getChineseNewYear(int year)
    {
        // Simplified Chinese New Year calculation (approximate)
        LocalDate baseDate = LocalDate.of(year, 1, 21);
        int dayOffset = (int) ((year - 2000) * 0.2422);
        return baseDate.plusDays(dayOffset);
    }
}
