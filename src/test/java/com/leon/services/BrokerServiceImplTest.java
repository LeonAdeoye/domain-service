package com.leon.services;

import com.leon.models.Broker;
import com.leon.repositories.BrokerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BrokerServiceImplTest {
    @Mock
    private BrokerRepository brokerRepository;

    @InjectMocks
    private BrokerServiceImpl brokerService;

    private List<Broker> mockBrokers;
    private Broker mockBroker1;
    private Broker mockBroker2;

    @Before
    public void setUp() {
        mockBrokers = new ArrayList<>();
        mockBroker1 = new Broker("BRK1", "Broker 1");
        mockBroker2 = new Broker("BRK2", "Broker 2");
        mockBrokers.add(mockBroker1);
        mockBrokers.add(mockBroker2);

        when(brokerRepository.findAll()).thenReturn(mockBrokers);
        brokerService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadBrokers() {
        // Arrange
        List<Broker> newBrokers = new ArrayList<>();
        Broker newBroker = new Broker("NB", "New Broker");
        newBrokers.add(newBroker);
        when(brokerRepository.findAll()).thenReturn(newBrokers);

        // Act
        brokerService.reconfigure();

        // Assert
        List<Broker> result = brokerService.getAll();
        assertEquals(1, result.size());
        assertEquals("NB", result.get(0).getBrokerAcronym());
        verify(brokerRepository, times(2)).findAll();
    }

    @Test
    public void getAll_shouldReturnAllBrokers() {
        // Act
        List<Broker> result = brokerService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("BRK1", result.get(0).getBrokerAcronym());
        assertEquals("BRK2", result.get(1).getBrokerAcronym());
    }

    @Test
    public void deleteBroker_withExistingBroker_shouldDeleteBroker() {
        // Arrange
        String brokerId = mockBroker1.getBrokerId().toString();
        when(brokerRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockBroker1));

        // Act
        brokerService.deleteBroker(brokerId);

        // Assert
        verify(brokerRepository).delete(mockBroker1);
        List<Broker> result = brokerService.getAll();
        assertEquals(1, result.size());
        assertEquals("BRK2", result.get(0).getBrokerAcronym());
    }

    @Test
    public void deleteBroker_withNonExistingBroker_shouldNotDeleteAnything() {
        // Arrange
        String brokerId = UUID.randomUUID().toString();
        when(brokerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        brokerService.deleteBroker(brokerId);

        // Assert
        verify(brokerRepository, never()).delete(any(Broker.class));
        List<Broker> result = brokerService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    public void createBroker_withNewBroker_shouldCreateBroker() {
        // Arrange
        Broker newBroker = new Broker("NB", "New Broker");
        when(brokerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(brokerRepository.save(any(Broker.class))).thenReturn(newBroker);

        // Act
        Broker result = brokerService.createBroker(newBroker);

        // Assert
        assertNotNull(result);
        assertEquals("NB", result.getBrokerAcronym());
        verify(brokerRepository).save(newBroker);
        List<Broker> allBrokers = brokerService.getAll();
        assertEquals(3, allBrokers.size());
    }

    @Test
    public void createBroker_withExistingBroker_shouldNotCreateBroker() {
        // Arrange
        when(brokerRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockBroker1));

        // Act
        Broker result = brokerService.createBroker(mockBroker1);

        // Assert
        assertNull(result);
        verify(brokerRepository, never()).save(any(Broker.class));
        List<Broker> allBrokers = brokerService.getAll();
        assertEquals(2, allBrokers.size());
    }

    @Test
    public void updateBroker_withExistingBroker_shouldUpdateBroker() {
        // Arrange
        Broker updatedBroker = new Broker("UB", "Updated Broker");
        updatedBroker.setBrokerId(mockBroker1.getBrokerId());
        when(brokerRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockBroker1));
        when(brokerRepository.save(any(Broker.class))).thenReturn(updatedBroker);

        // Act
        Broker result = brokerService.updateBroker(updatedBroker);

        // Assert
        assertNotNull(result);
        assertEquals("UB", result.getBrokerAcronym());
        verify(brokerRepository).save(updatedBroker);
    }

    @Test
    public void updateBroker_withNonExistingBroker_shouldNotUpdateBroker() {
        // Arrange
        Broker nonExistingBroker = new Broker("NB", "New Broker");
        nonExistingBroker.setBrokerId(UUID.randomUUID());
        when(brokerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Broker result = brokerService.updateBroker(nonExistingBroker);

        // Assert
        assertNull(result);
        verify(brokerRepository, never()).save(any(Broker.class));
    }
} 