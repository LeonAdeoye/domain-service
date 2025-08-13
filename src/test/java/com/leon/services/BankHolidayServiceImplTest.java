package com.leon.services;

import com.leon.models.BankHoliday;
import com.leon.repositories.BankHolidayRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankHolidayServiceImplTest
{
    @Mock
    private BankHolidayRepository bankHolidayRepository;
    
    @InjectMocks
    private BankHolidayServiceImpl bankHolidayService;
    
    private BankHoliday testHoliday;
    
    @Before
    public void setUp()
    {
        testHoliday = new BankHoliday();
        testHoliday.setId("test-id");
        testHoliday.setCountryCode("JP");
        testHoliday.setHolidayName("Test Holiday");
        testHoliday.setHolidayDate(LocalDate.of(2024, 12, 25));
        testHoliday.setPublicHoliday(true);
        testHoliday.setDescription("Test description");
    }
    
    @Test
    void getAll_shouldReturnAllHolidays()
    {
        // Given
        List<BankHoliday> expectedHolidays = Arrays.asList(testHoliday);
        when(bankHolidayRepository.findAll()).thenReturn(expectedHolidays);
        
        // When
        List<BankHoliday> result = bankHolidayService.getAll();
        
        // Then
        assertEquals(expectedHolidays, result);
        verify(bankHolidayRepository).findAll();
    }
    
    @Test
    void addBankHoliday_shouldSaveAndReturnHoliday()
    {
        // Given
        when(bankHolidayRepository.save(any(BankHoliday.class))).thenReturn(testHoliday);
        
        // When
        BankHoliday result = bankHolidayService.addBankHoliday(testHoliday);
        
        // Then
        assertNotNull(result);
        assertEquals(testHoliday.getId(), result.getId());
        verify(bankHolidayRepository).save(testHoliday);
    }
    
    @Test
    void addBankHoliday_withFutureDateAfterCutoff_shouldThrowException()
    {
        // Given
        testHoliday.setHolidayDate(LocalDate.of(2027, 1, 1));
        
        // When & Then
        try
        {
            bankHolidayService.addBankHoliday(testHoliday);
            fail("Expected IllegalArgumentException to be thrown");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    @Test
    void updateBankHoliday_withExistingHoliday_shouldUpdateAndReturnHoliday()
    {
        // Given
        when(bankHolidayRepository.findById(testHoliday.getId())).thenReturn(Optional.of(testHoliday));
        when(bankHolidayRepository.save(any(BankHoliday.class))).thenReturn(testHoliday);
        
        // When
        BankHoliday result = bankHolidayService.updateBankHoliday(testHoliday);
        
        // Then
        assertNotNull(result);
        assertEquals(testHoliday.getId(), result.getId());
        verify(bankHolidayRepository).findById(testHoliday.getId());
        verify(bankHolidayRepository).save(testHoliday);
    }
    
    @Test
    void updateBankHoliday_withNonExistingHoliday_shouldReturnNull()
    {
        // Given
        when(bankHolidayRepository.findById(testHoliday.getId())).thenReturn(Optional.empty());
        
        // When
        BankHoliday result = bankHolidayService.updateBankHoliday(testHoliday);
        
        // Then
        assertNull(result);
        verify(bankHolidayRepository).findById(testHoliday.getId());
        verify(bankHolidayRepository, never()).save(any(BankHoliday.class));
    }
    
    @Test
    void deleteBankHoliday_withExistingHoliday_shouldDeleteHoliday()
    {
        // Given
        when(bankHolidayRepository.findById(testHoliday.getId())).thenReturn(Optional.of(testHoliday));
        
        // When
        bankHolidayService.deleteBankHoliday(testHoliday.getId());
        
        // Then
        verify(bankHolidayRepository).findById(testHoliday.getId());
        verify(bankHolidayRepository).delete(testHoliday);
    }
    
    @Test
    void getCountries_shouldReturnSupportedCountries()
    {
        // When
        List<String> result = bankHolidayService.getCountries();
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("JP"));
        assertTrue(result.contains("HK"));
    }
}
