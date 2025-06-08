package com.leon.services;

import com.leon.models.AssetType;
import com.leon.models.Currency;
import com.leon.models.Instrument;
import com.leon.models.SettlementType;
import com.leon.repositories.InstrumentRepository;
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
public class InstrumentServiceImplTest {
    @Mock
    private InstrumentRepository instrumentRepository;

    @InjectMocks
    private InstrumentServiceImpl instrumentService;

    private List<Instrument> mockInstruments;
    private Instrument mockInstrument1;
    private Instrument mockInstrument2;

    @Before
    public void setUp() {
        mockInstruments = new ArrayList<>();
        mockInstrument1 = new Instrument("INST1", "Instrument 1", AssetType.STOCK, "BLG1", "RIC1", 
            Currency.HKD, SettlementType.T_PLUS_ONE, "HKSE");
        mockInstrument2 = new Instrument("INST2", "Instrument 2", AssetType.STOCK, "BLG2", "RIC2", 
            Currency.HKD, SettlementType.T_PLUS_ONE, "HKSE");

        mockInstruments.add(mockInstrument1);
        mockInstruments.add(mockInstrument2);

        when(instrumentRepository.findAll()).thenReturn(mockInstruments);
        instrumentService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadInstruments() {
        // Arrange
        List<Instrument> newInstruments = new ArrayList<>();
        Instrument newInstrument = new Instrument("NEW", "New Instrument", AssetType.STOCK, "BLG3", "RIC3", 
            Currency.HKD, SettlementType.T_PLUS_ONE, "HKSE");
        newInstruments.add(newInstrument);
        when(instrumentRepository.findAll()).thenReturn(newInstruments);

        // Act
        instrumentService.reconfigure();

        // Assert
        List<Instrument> result = instrumentService.getAll();
        assertEquals(1, result.size());
        assertEquals("NEW", result.get(0).getInstrumentCode());
        assertEquals("New Instrument", result.get(0).getInstrumentDescription());
        verify(instrumentRepository, times(2)).findAll();
    }

    @Test
    public void getAll_shouldReturnAllInstruments() {
        // Act
        List<Instrument> result = instrumentService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("INST1", result.get(0).getInstrumentCode());
        assertEquals("Instrument 1", result.get(0).getInstrumentDescription());
        assertEquals("INST2", result.get(1).getInstrumentCode());
        assertEquals("Instrument 2", result.get(1).getInstrumentDescription());
    }

    @Test
    public void createInstrument_withNewInstrument_shouldCreateInstrument() {
        // Arrange
        Instrument newInstrument = new Instrument("NEW", "New Instrument", AssetType.STOCK, "BLG3", "RIC3", 
            Currency.HKD, SettlementType.T_PLUS_ONE, "HKSE");
        when(instrumentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(instrumentRepository.save(any(Instrument.class))).thenReturn(newInstrument);

        // Act
        Instrument result = instrumentService.createInstrument(newInstrument);

        // Assert
        assertNotNull(result);
        assertEquals("NEW", result.getInstrumentCode());
        assertEquals("New Instrument", result.getInstrumentDescription());
        verify(instrumentRepository).save(newInstrument);
        List<Instrument> allInstruments = instrumentService.getAll();
        assertEquals(3, allInstruments.size());
    }

    @Test
    public void createInstrument_withExistingInstrument_shouldNotCreateInstrument() {
        // Arrange
        when(instrumentRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockInstrument1));

        // Act
        Instrument result = instrumentService.createInstrument(mockInstrument1);

        // Assert
        assertNotNull(result);
        assertEquals(mockInstrument1, result);
        verify(instrumentRepository, never()).save(any(Instrument.class));
        List<Instrument> allInstruments = instrumentService.getAll();
        assertEquals(2, allInstruments.size());
    }

    @Test
    public void updateInstrument_withExistingInstrument_shouldUpdateInstrument() {
        // Arrange
        Instrument updatedInstrument = new Instrument("INST1", "Updated Instrument", AssetType.STOCK, "BLG1", "RIC1", 
            Currency.HKD, SettlementType.T_PLUS_ONE, "HKSE");
        updatedInstrument.setInstrumentId(mockInstrument1.getInstrumentId());
        when(instrumentRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockInstrument1));
        when(instrumentRepository.save(any(Instrument.class))).thenReturn(updatedInstrument);

        // Act
        Instrument result = instrumentService.updateInstrument(updatedInstrument);

        // Assert
        assertNotNull(result);
        assertEquals("INST1", result.getInstrumentCode());
        assertEquals("Updated Instrument", result.getInstrumentDescription());
        verify(instrumentRepository).save(updatedInstrument);
        List<Instrument> allInstruments = instrumentService.getAll();
        assertEquals(2, allInstruments.size());
    }

    @Test
    public void updateInstrument_withNonExistingInstrument_shouldNotUpdateInstrument() {
        // Arrange
        Instrument nonExistingInstrument = new Instrument("NON", "Non Existing", AssetType.STOCK, "BLG3", "RIC3", 
            Currency.HKD, SettlementType.T_PLUS_ONE, "HKSE");
        when(instrumentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Instrument result = instrumentService.updateInstrument(nonExistingInstrument);

        // Assert
        assertNull(result);
        verify(instrumentRepository, never()).save(any(Instrument.class));
        List<Instrument> allInstruments = instrumentService.getAll();
        assertEquals(2, allInstruments.size());
    }

    @Test
    public void deleteInstrument_withExistingInstrument_shouldDeleteInstrument() {
        // Arrange
        String instrumentId = mockInstrument1.getInstrumentId().toString();
        when(instrumentRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockInstrument1));

        // Act
        instrumentService.deleteInstrument(instrumentId);

        // Assert
        verify(instrumentRepository).delete(mockInstrument1);
        List<Instrument> result = instrumentService.getAll();
        assertEquals(1, result.size());
        assertEquals("INST2", result.get(0).getInstrumentCode());
    }

    @Test
    public void deleteInstrument_withNonExistingInstrument_shouldNotDeleteAnything() {
        // Arrange
        String instrumentId = UUID.randomUUID().toString();
        when(instrumentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        instrumentService.deleteInstrument(instrumentId);

        // Assert
        verify(instrumentRepository, never()).delete(any(Instrument.class));
        List<Instrument> result = instrumentService.getAll();
        assertEquals(2, result.size());
    }
} 