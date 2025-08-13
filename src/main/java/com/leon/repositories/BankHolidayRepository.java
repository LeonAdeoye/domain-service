package com.leon.repositories;

import com.leon.models.BankHoliday;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankHolidayRepository extends MongoRepository<BankHoliday, String>
{
    List<BankHoliday> findByCountryCode(String countryCode);
    
    List<BankHoliday> findByHolidayDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<BankHoliday> findByCountryCodeAndHolidayDateBetween(String countryCode, LocalDate startDate, LocalDate endDate);
    
    List<BankHoliday> findByHolidayDateGreaterThanEqual(LocalDate date);
    
    List<BankHoliday> findByCountryCodeAndHolidayDateGreaterThanEqual(String countryCode, LocalDate date);
}
