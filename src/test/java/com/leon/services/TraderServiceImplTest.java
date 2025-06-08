package com.leon.services;

import com.leon.models.Trader;
import com.leon.repositories.TraderRepository;
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
public class TraderServiceImplTest {
    @Mock
    private TraderRepository traderRepository;

    @InjectMocks
    private TraderServiceImpl traderService;

    private List<Trader> mockTraders;
    private Trader mockTrader1;
    private Trader mockTrader2;

    @Before
    public void setUp() {
        mockTraders = new ArrayList<>();
        mockTrader1 = new Trader();
        mockTrader1.setTraderId(UUID.randomUUID());
        mockTrader1.setFirstName("John");
        mockTrader1.setLastName("Doe");
        mockTrader1.setUserId("jdoe");

        mockTrader2 = new Trader();
        mockTrader2.setTraderId(UUID.randomUUID());
        mockTrader2.setFirstName("Jane");
        mockTrader2.setLastName("Smith");
        mockTrader2.setUserId("jsmith");

        mockTraders.add(mockTrader1);
        mockTraders.add(mockTrader2);

        when(traderRepository.findAll()).thenReturn(mockTraders);
        traderService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadTraders() {
        // Arrange
        List<Trader> newTraders = new ArrayList<>();
        Trader newTrader = new Trader();
        newTrader.setTraderId(UUID.randomUUID());
        newTrader.setFirstName("New");
        newTrader.setLastName("Trader");
        newTrader.setUserId("ntrader");
        newTraders.add(newTrader);
        when(traderRepository.findAll()).thenReturn(newTraders);

        // Act
        traderService.reconfigure();

        // Assert
        List<Trader> result = traderService.getAllTraders();
        assertEquals(1, result.size());
        assertEquals("New", result.get(0).getFirstName());
        assertEquals("Trader", result.get(0).getLastName());
        assertEquals("ntrader", result.get(0).getUserId());
        verify(traderRepository, times(2)).findAll();
    }

    @Test
    public void getAllTraders_shouldReturnAllTraders() {
        // Act
        List<Trader> result = traderService.getAllTraders();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("jdoe", result.get(0).getUserId());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals("jsmith", result.get(1).getUserId());
    }

    @Test
    public void getTraderById_withExistingTrader_shouldReturnTrader() {
        // Arrange
        String traderId = mockTrader1.getTraderId().toString();

        // Act
        Trader result = traderService.getTraderById(traderId);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("jdoe", result.getUserId());
    }

    @Test
    public void getTraderById_withNonExistingTrader_shouldReturnNull() {
        // Arrange
        String traderId = UUID.randomUUID().toString();

        // Act
        Trader result = traderService.getTraderById(traderId);

        // Assert
        assertNull(result);
    }

    @Test
    public void createTrader_withNewTrader_shouldCreateTrader() {
        // Arrange
        Trader newTrader = new Trader();
        newTrader.setTraderId(UUID.randomUUID());
        newTrader.setFirstName("New");
        newTrader.setLastName("Trader");
        newTrader.setUserId("ntrader");
        when(traderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(traderRepository.save(any(Trader.class))).thenReturn(newTrader);

        // Act
        Trader result = traderService.createTrader(newTrader);

        // Assert
        assertNotNull(result);
        assertEquals("New", result.getFirstName());
        assertEquals("Trader", result.getLastName());
        assertEquals("ntrader", result.getUserId());
        verify(traderRepository).save(newTrader);
        List<Trader> allTraders = traderService.getAllTraders();
        assertEquals(3, allTraders.size());
    }

    @Test
    public void createTrader_withExistingTrader_shouldNotCreateTrader() {
        // Arrange
        when(traderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockTrader1));

        // Act
        Trader result = traderService.createTrader(mockTrader1);

        // Assert
        assertNotNull(result);
        assertEquals(mockTrader1, result);
        verify(traderRepository, never()).save(any(Trader.class));
        List<Trader> allTraders = traderService.getAllTraders();
        assertEquals(2, allTraders.size());
    }

    @Test
    public void updateTrader_withExistingTrader_shouldUpdateTrader() {
        // Arrange
        Trader updatedTrader = new Trader();
        updatedTrader.setTraderId(mockTrader1.getTraderId());
        updatedTrader.setFirstName("Updated");
        updatedTrader.setLastName("Trader");
        updatedTrader.setUserId("utrader");
        when(traderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockTrader1));
        when(traderRepository.save(any(Trader.class))).thenReturn(updatedTrader);

        // Act
        Trader result = traderService.updateTrader(updatedTrader);

        // Assert
        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        assertEquals("Trader", result.getLastName());
        assertEquals("utrader", result.getUserId());
        verify(traderRepository).save(updatedTrader);
    }

    @Test
    public void updateTrader_withNonExistingTrader_shouldNotUpdateTrader() {
        // Arrange
        Trader nonExistingTrader = new Trader();
        nonExistingTrader.setTraderId(UUID.randomUUID());
        when(traderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Trader result = traderService.updateTrader(nonExistingTrader);

        // Assert
        assertNull(result);
        verify(traderRepository, never()).save(any(Trader.class));
    }

    @Test
    public void deleteTrader_withExistingTrader_shouldDeleteTrader() {
        // Arrange
        String traderId = mockTrader1.getTraderId().toString();
        when(traderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockTrader1));

        // Act
        traderService.deleteTrader(traderId);

        // Assert
        verify(traderRepository).delete(mockTrader1);
        List<Trader> result = traderService.getAllTraders();
        assertEquals(1, result.size());
        assertEquals("Jane", result.get(0).getFirstName());
    }

    @Test
    public void deleteTrader_withNonExistingTrader_shouldNotDeleteAnything() {
        // Arrange
        String traderId = UUID.randomUUID().toString();
        when(traderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        traderService.deleteTrader(traderId);

        // Assert
        verify(traderRepository, never()).delete(any(Trader.class));
        List<Trader> result = traderService.getAllTraders();
        assertEquals(2, result.size());
    }

    @Test
    public void doesTraderExist_withExistingTrader_shouldReturnTrue() {
        // Arrange
        String traderId = mockTrader1.getTraderId().toString();

        // Act
        boolean result = traderService.doesTraderExist(traderId);

        // Assert
        assertTrue(result);
    }

    @Test
    public void doesTraderExist_withNonExistingTrader_shouldReturnFalse() {
        // Arrange
        String traderId = UUID.randomUUID().toString();

        // Act
        boolean result = traderService.doesTraderExist(traderId);

        // Assert
        assertFalse(result);
    }
} 