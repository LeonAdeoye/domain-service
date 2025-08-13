package com.leon.services;

import com.leon.models.BankHoliday;
import java.time.LocalDate;
import java.util.List;

public interface BankHolidayService
{
    void reconfigure();
    
    List<BankHoliday> getAll();
    
    List<BankHoliday> getBankHolidays();
    
    List<BankHoliday> getBankHolidaysByCountry(String countryCode);
    
    List<BankHoliday> getBankHolidaysByDateRange(LocalDate startDate, LocalDate endDate);
    
    List<BankHoliday> getHolidaysWithinNextYear();
    
    boolean isBankHoliday(LocalDate date, String countryCode);
    
    int calculateBusinessDays(LocalDate startDate, LocalDate endDate, String countryCode);
    
    BankHoliday addBankHoliday(BankHoliday bankHoliday);
    
    BankHoliday updateBankHoliday(BankHoliday bankHoliday);
    
    void deleteBankHoliday(String id);
    
    List<String> getCountries();
}
