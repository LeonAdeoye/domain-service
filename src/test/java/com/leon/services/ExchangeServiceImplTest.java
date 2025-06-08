package com.leon.services;

import com.leon.models.Exchange;
import com.leon.repositories.ExchangeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeServiceImplTest {
    @Mock
    private ExchangeRepository exchangeRepository;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    private List<Exchange> mockExchanges;
    private Exchange mockExchange1;
    private Exchange mockExchange2;

    @Before
    public void setUp() {
        mockExchanges = new ArrayList<>();
        mockExchange1 = new Exchange();
        mockExchange1.setExchangeId(UUID.randomUUID());
        mockExchange1.setExchangeName("Exchange 1");
        mockExchange1.setExchangeAcronym("EX1");

        mockExchange2 = new Exchange();
        mockExchange2.setExchangeId(UUID.randomUUID());
        mockExchange2.setExchangeName("Exchange 2");
        mockExchange2.setExchangeAcronym("EX2");

        mockExchanges.add(mockExchange1);
        mockExchanges.add(mockExchange2);

        when(exchangeRepository.findAll()).thenReturn(mockExchanges);
        exchangeService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadExchanges() {
        // Arrange
        List<Exchange> newExchanges = new ArrayList<>();
        Exchange newExchange = new Exchange();
        newExchange.setExchangeId(UUID.randomUUID());
        newExchange.setExchangeName("New Exchange");
        newExchange.setExchangeAcronym("NEW");
        newExchanges.add(newExchange);
        when(exchangeRepository.findAll()).thenReturn(newExchanges);

        // Act
        exchangeService.reconfigure();

        // Assert
        List<Exchange> result = exchangeService.getAll();
        assertEquals(1, result.size());
        assertEquals("New Exchange", result.get(0).getExchangeName());
        assertEquals("NEW", result.get(0).getExchangeAcronym());
        verify(exchangeRepository, times(2)).findAll();
    }

    @Test
    public void getAll_shouldReturnAllExchanges() {
        // Act
        List<Exchange> result = exchangeService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Exchange 1", result.get(0).getExchangeName());
        assertEquals("EX1", result.get(0).getExchangeAcronym());
        assertEquals("Exchange 2", result.get(1).getExchangeName());
        assertEquals("EX2", result.get(1).getExchangeAcronym());
    }

    @Test
    public void createExchange_withNewExchange_shouldCreateExchange() {
        // Arrange
        Exchange newExchange = new Exchange();
        newExchange.setExchangeId(UUID.randomUUID());
        newExchange.setExchangeName("New Exchange");
        newExchange.setExchangeAcronym("NEW");
        when(exchangeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(exchangeRepository.save(any(Exchange.class))).thenReturn(newExchange);

        // Act
        Exchange result = exchangeService.createExchange(newExchange);

        // Assert
        assertNotNull(result);
        assertEquals("New Exchange", result.getExchangeName());
        assertEquals("NEW", result.getExchangeAcronym());
        verify(exchangeRepository).save(newExchange);
        List<Exchange> allExchanges = exchangeService.getAll();
        assertEquals(3, allExchanges.size());
    }

    @Test
    public void createExchange_withExistingExchange_shouldNotCreateExchange() {
        // Arrange
        when(exchangeRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockExchange1));

        // Act
        Exchange result = exchangeService.createExchange(mockExchange1);

        // Assert
        assertNotNull(result);
        assertEquals(mockExchange1, result);
        verify(exchangeRepository, never()).save(any(Exchange.class));
        List<Exchange> allExchanges = exchangeService.getAll();
        assertEquals(2, allExchanges.size());
    }

    @Test
    public void deleteExchange_withExistingExchange_shouldDeleteExchange() {
        // Arrange
        String exchangeId = mockExchange1.getExchangeId().toString();
        when(exchangeRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockExchange1));

        // Act
        exchangeService.deleteExchange(exchangeId);

        // Assert
        verify(exchangeRepository).delete(mockExchange1);
        List<Exchange> result = exchangeService.getAll();
        assertEquals(1, result.size());
        assertEquals("Exchange 2", result.get(0).getExchangeName());
    }

    @Test
    public void deleteExchange_withNonExistingExchange_shouldNotDeleteAnything() {
        // Arrange
        String exchangeId = UUID.randomUUID().toString();
        when(exchangeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        exchangeService.deleteExchange(exchangeId);

        // Assert
        verify(exchangeRepository, never()).delete(any(Exchange.class));
        List<Exchange> result = exchangeService.getAll();
        assertEquals(2, result.size());
    }
} 