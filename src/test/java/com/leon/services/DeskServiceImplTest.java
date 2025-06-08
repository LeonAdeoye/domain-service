package com.leon.services;

import com.leon.models.Desk;
import com.leon.models.Trader;
import com.leon.repositories.DeskRepository;
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
public class DeskServiceImplTest {
    @Mock
    private DeskRepository deskRepository;

    @Mock
    private TraderService traderService;

    @InjectMocks
    private DeskServiceImpl deskService;

    private List<Desk> mockDesks;
    private Desk mockDesk1;
    private Trader mockTrader1;

    @Before
    public void setUp() {
        mockDesks = new ArrayList<>();
        mockDesk1 = new Desk();
        mockDesk1.setDeskId(UUID.randomUUID());
        mockDesk1.setDeskName("Desk 1");
        mockDesk1.setTraders(new ArrayList<>());

        mockTrader1 = new Trader();
        mockTrader1.setTraderId(UUID.randomUUID());
        mockTrader1.setFirstName("John");
        mockTrader1.setLastName("Doe");
        mockTrader1.setUserId("jdoe");
        mockDesks.add(mockDesk1);

        when(deskRepository.findAll()).thenReturn(mockDesks);
        deskService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadDesks() {
        // Arrange
        List<Desk> newDesks = new ArrayList<>();
        Desk newDesk = new Desk();
        newDesk.setDeskId(UUID.randomUUID());
        newDesk.setDeskName("New Desk");
        newDesk.setTraders(new ArrayList<>());
        newDesks.add(newDesk);
        when(deskRepository.findAll()).thenReturn(newDesks);

        // Act
        deskService.reconfigure();

        // Assert
        List<Desk> result = deskService.getAllDesks();
        assertEquals(1, result.size());
        assertEquals("New Desk", result.get(0).getDeskName());
        verify(deskRepository, times(2)).findAll();
    }

    @Test
    public void getAllDesks_shouldReturnAllDesks() {
        // Act
        List<Desk> result = deskService.getAllDesks();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Desk 1", result.get(0).getDeskName());
    }

    @Test
    public void getDeskById_withExistingDesk_shouldReturnDesk() {
        // Arrange
        String deskId = mockDesk1.getDeskId().toString();

        // Act
        Desk result = deskService.getDeskById(deskId);

        // Assert
        assertNotNull(result);
        assertEquals("Desk 1", result.getDeskName());
    }

    @Test
    public void getDeskById_withNonExistingDesk_shouldReturnNull() {
        // Arrange
        String deskId = UUID.randomUUID().toString();

        // Act
        Desk result = deskService.getDeskById(deskId);

        // Assert
        assertNull(result);
    }

    @Test
    public void createDesk_withNewDesk_shouldCreateDesk() {
        // Arrange
        Desk newDesk = new Desk();
        newDesk.setDeskId(UUID.randomUUID());
        newDesk.setDeskName("New Desk");
        newDesk.setTraders(new ArrayList<>());
        when(deskRepository.existsById(any(UUID.class))).thenReturn(false);
        when(deskRepository.save(any(Desk.class))).thenReturn(newDesk);

        // Act
        Desk result = deskService.createDesk(newDesk);

        // Assert
        assertNotNull(result);
        assertEquals("New Desk", result.getDeskName());
        verify(deskRepository).save(newDesk);
        List<Desk> allDesks = deskService.getAllDesks();
        assertEquals(2, allDesks.size());
    }

    @Test
    public void createDesk_withExistingDesk_shouldNotCreateDesk() {
        // Arrange
        lenient().when(deskRepository.existsById(any(UUID.class))).thenReturn(true);

        // Act
        Desk result = deskService.createDesk(mockDesk1);

        // Assert
        assertNotNull(result);
        verify(deskRepository, never()).save(any(Desk.class));
        List<Desk> allDesks = deskService.getAllDesks();
        assertEquals(1, allDesks.size());
    }

    @Test
    public void updateDesk_withExistingDesk_shouldUpdateDesk() {
        // Arrange
        Desk updatedDesk = new Desk();
        updatedDesk.setDeskId(mockDesk1.getDeskId());
        updatedDesk.setDeskName("Updated Desk");
        updatedDesk.setTraders(new ArrayList<>());
        when(deskRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockDesk1));
        when(deskRepository.save(any(Desk.class))).thenReturn(updatedDesk);

        // Act
        Desk result = deskService.updateDesk(updatedDesk);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Desk", result.getDeskName());
        verify(deskRepository).save(updatedDesk);
    }

    @Test
    public void updateDesk_withNonExistingDesk_shouldNotUpdateDesk() {
        // Arrange
        Desk nonExistingDesk = new Desk();
        nonExistingDesk.setDeskId(UUID.randomUUID());
        when(deskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Desk result = deskService.updateDesk(nonExistingDesk);

        // Assert
        assertNull(result);
        verify(deskRepository, never()).save(any(Desk.class));
    }

    @Test
    public void deleteDesk_withExistingDesk_shouldDeleteDesk() {
        // Arrange
        String deskId = mockDesk1.getDeskId().toString();
        when(deskRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockDesk1));

        // Act
        deskService.deleteDesk(deskId);

        // Assert
        verify(deskRepository).deleteById(UUID.fromString(deskId));
        List<Desk> result = deskService.getAllDesks();
        assertEquals(0, result.size());
    }

    @Test
    public void deleteDesk_withNonExistingDesk_shouldNotDeleteAnything() {
        // Arrange
        String deskId = UUID.randomUUID().toString();
        when(deskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        deskService.deleteDesk(deskId);

        // Assert
        verify(deskRepository, never()).deleteById(any(UUID.class));
        List<Desk> result = deskService.getAllDesks();
        assertEquals(1, result.size());
    }

    @Test
    public void addTraderToDesk_withValidData_shouldAddTrader() {
        // Arrange
        String deskId = mockDesk1.getDeskId().toString();
        String traderId = mockTrader1.getTraderId().toString();
        when(deskRepository.save(any(Desk.class))).thenReturn(mockDesk1);

        // Act
        deskService.addTraderToDesk(deskId, traderId);

        // Assert
        verify(deskRepository).save(mockDesk1);
        assertTrue(mockDesk1.getTraders().contains(UUID.fromString(traderId)));
    }

    @Test
    public void addTraderToDesk_withNonExistingDesk_shouldNotAddTrader() {
        // Arrange
        String deskId = UUID.randomUUID().toString();
        String traderId = mockTrader1.getTraderId().toString();

        // Act
        deskService.addTraderToDesk(deskId, traderId);

        // Assert
        verify(deskRepository, never()).save(any(Desk.class));
    }

    @Test
    public void removeTraderFromDesk_withValidData_shouldRemoveTrader() {
        // Arrange
        String deskId = mockDesk1.getDeskId().toString();
        String traderId = mockTrader1.getTraderId().toString();
        mockDesk1.getTraders().add(UUID.fromString(traderId));
        when(deskRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockDesk1));
        when(deskRepository.save(any(Desk.class))).thenReturn(mockDesk1);

        // Act
        deskService.removeTraderFromDesk(deskId, traderId);

        // Assert
        verify(deskRepository).save(mockDesk1);
        assertFalse(mockDesk1.getTraders().contains(UUID.fromString(traderId)));
    }

    @Test
    public void removeTraderFromDesk_withNonExistingDesk_shouldNotRemoveTrader() {
        // Arrange
        String deskId = UUID.randomUUID().toString();
        String traderId = mockTrader1.getTraderId().toString();
        when(deskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        deskService.removeTraderFromDesk(deskId, traderId);

        // Assert
        verify(deskRepository, never()).save(any(Desk.class));
    }

    @Test
    public void getTradersByDeskId_withValidDesk_shouldReturnTraders() {
        // Arrange
        String deskId = mockDesk1.getDeskId().toString();
        mockDesk1.getTraders().add(mockTrader1.getTraderId());
        when(traderService.getTraderById(any(String.class))).thenReturn(mockTrader1);

        // Act
        List<Trader> result = deskService.getTradersByDeskId(deskId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(mockTrader1, result.get(0));
    }

    @Test
    public void getTradersByDeskId_withNonExistingDesk_shouldReturnEmptyList() {
        // Arrange
        String deskId = UUID.randomUUID().toString();

        // Act
        List<Trader> result = deskService.getTradersByDeskId(deskId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void getDesk_withValidTrader_shouldReturnDesk() {
        // Arrange
        String traderId = mockTrader1.getTraderId().toString();
        mockDesk1.getTraders().add(UUID.fromString(traderId));

        // Act
        Desk result = deskService.getDesk(traderId);

        // Assert
        assertNotNull(result);
        assertEquals(mockDesk1, result);
    }

    @Test
    public void getDesk_withNonExistingTrader_shouldReturnNull() {
        // Arrange
        String traderId = UUID.randomUUID().toString();

        // Act
        Desk result = deskService.getDesk(traderId);

        // Assert
        assertNull(result);
    }

    @Test
    public void doesTraderBelongToDesk_withValidData_shouldReturnTrue() {
        // Arrange
        String deskId = mockDesk1.getDeskId().toString();
        String traderId = mockTrader1.getTraderId().toString();
        mockDesk1.getTraders().add(UUID.fromString(traderId));

        // Act
        boolean result = deskService.doesTraderBelongToDesk(deskId, traderId);

        // Assert
        assertTrue(result);
    }

    @Test
    public void doesTraderBelongToDesk_withNonExistingDesk_shouldReturnFalse() {
        // Arrange
        String deskId = UUID.randomUUID().toString();
        String traderId = mockTrader1.getTraderId().toString();

        // Act
        boolean result = deskService.doesTraderBelongToDesk(deskId, traderId);

        // Assert
        assertFalse(result);
    }
} 