package com.leon.services;

import com.leon.models.ClientInterest;
import com.leon.models.Side;
import com.leon.repositories.ClientInterestRepository;
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
public class ClientInterestServiceImplTest {
    @Mock
    private ClientInterestRepository clientInterestRepository;

    @InjectMocks
    private ClientInterestServiceImpl clientInterestService;

    private List<ClientInterest> mockInterests;
    private ClientInterest mockInterest1;
    private ClientInterest mockInterest2;
    private String ownerId = "testOwner";
    private UUID clientId = UUID.randomUUID();

    @Before
    public void setUp() {
        mockInterests = new ArrayList<>();
        mockInterest1 = new ClientInterest(ownerId, clientId, "Test Notes 1", Side.BUY, "AAPL");
        mockInterest2 = new ClientInterest(ownerId, clientId, "Test Notes 2", Side.SELL, "GOOGL");

        mockInterests.add(mockInterest1);
        mockInterests.add(mockInterest2);

        when(clientInterestRepository.findAll()).thenReturn(mockInterests);
        clientInterestService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadInterests() {
        // Arrange
        List<ClientInterest> newInterests = new ArrayList<>();
        ClientInterest newInterest = new ClientInterest(ownerId, clientId, "New Interest", Side.BUY, "MSFT");
        newInterests.add(newInterest);
        when(clientInterestRepository.findAll()).thenReturn(newInterests);

        // Act
        clientInterestService.reconfigure();

        // Assert
        List<ClientInterest> result = clientInterestService.getAll(ownerId);
        assertEquals(1, result.size());
        assertEquals("New Interest", result.get(0).getNotes());
        assertEquals("MSFT", result.get(0).getInstrumentCode());
        verify(clientInterestRepository, times(2)).findAll();
    }

    @Test
    public void getAll_shouldReturnAllInterestsForOwner() {
        // Act
        List<ClientInterest> result = clientInterestService.getAll(ownerId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Notes 1", result.get(0).getNotes());
        assertEquals("AAPL", result.get(0).getInstrumentCode());
        assertEquals("Test Notes 2", result.get(1).getNotes());
        assertEquals("GOOGL", result.get(1).getInstrumentCode());
    }

    @Test
    public void getAllByClientId_shouldReturnInterestsForClient() {
        // Act
        List<ClientInterest> result = clientInterestService.getAllByClientId(ownerId, clientId.toString());

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Notes 1", result.get(0).getNotes());
        assertEquals("AAPL", result.get(0).getInstrumentCode());
        assertEquals("Test Notes 2", result.get(1).getNotes());
        assertEquals("GOOGL", result.get(1).getInstrumentCode());
    }

    @Test
    public void save_withNewInterest_shouldSaveInterest() {
        // Arrange
        ClientInterest newInterest = new ClientInterest(ownerId, clientId, "New Interest", Side.BUY, "MSFT");
        when(clientInterestRepository.save(any(ClientInterest.class))).thenReturn(newInterest);

        // Act
        ClientInterest result = clientInterestService.save(newInterest);

        // Assert
        assertNotNull(result);
        assertEquals("New Interest", result.getNotes());
        assertEquals("MSFT", result.getInstrumentCode());
        verify(clientInterestRepository).save(newInterest);
        List<ClientInterest> allInterests = clientInterestService.getAll(ownerId);
        assertEquals(3, allInterests.size());
    }

    @Test
    public void update_withExistingInterest_shouldUpdateInterest() {
        // Arrange
        ClientInterest updatedInterest = new ClientInterest(ownerId, clientId, "Updated Interest", Side.SELL, "AMZN");
        updatedInterest.setClientInterestId(mockInterest1.getClientInterestId());
        when(clientInterestRepository.save(any(ClientInterest.class))).thenReturn(updatedInterest);

        // Act
        ClientInterest result = clientInterestService.update(updatedInterest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Interest", result.getNotes());
        assertEquals("AMZN", result.getInstrumentCode());
        verify(clientInterestRepository).save(updatedInterest);
        List<ClientInterest> allInterests = clientInterestService.getAll(ownerId);
        assertEquals(2, allInterests.size());
    }

    @Test
    public void delete_withExistingInterest_shouldDeleteInterest() {
        // Arrange
        String interestId = mockInterest1.getClientInterestId().toString();
        when(clientInterestRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockInterest1));

        // Act
        clientInterestService.delete(ownerId, interestId);

        // Assert
        verify(clientInterestRepository).delete(mockInterest1);
        List<ClientInterest> result = clientInterestService.getAll(ownerId);
        assertEquals(1, result.size());
        assertEquals("Test Notes 2", result.get(0).getNotes());
    }

    @Test
    public void delete_withNonExistingInterest_shouldNotDeleteAnything() {
        // Arrange
        String interestId = UUID.randomUUID().toString();
        when(clientInterestRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        clientInterestService.delete(ownerId, interestId);

        // Assert
        verify(clientInterestRepository, never()).delete(any(ClientInterest.class));
        List<ClientInterest> result = clientInterestService.getAll(ownerId);
        assertEquals(2, result.size());
    }
} 