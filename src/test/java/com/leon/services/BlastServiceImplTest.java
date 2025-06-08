package com.leon.services;

import com.leon.models.Blast;
import com.leon.repositories.BlastRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlastServiceImplTest {
    @Mock
    private BlastRepository blastRepository;

    @InjectMocks
    private BlastServiceImpl blastService;

    private List<Blast> mockBlasts;
    private Blast mockBlast1;
    private Blast mockBlast2;
    private String ownerId = "testOwner";

    @Before
    public void setUp() {
        mockBlasts = new ArrayList<>();
        mockBlast1 = new Blast();
        mockBlast1.setBlastId(UUID.randomUUID());
        mockBlast1.setOwnerId(ownerId);
        mockBlast1.setBlastName("Blast 1");
        mockBlast1.setMarkets(Arrays.asList("HK", "SG"));
        mockBlast1.setContents(Arrays.asList(Blast.ContentType.IOIs, Blast.ContentType.NEWS));
        mockBlast1.setTriggerTime(LocalTime.of(9, 0));

        mockBlast2 = new Blast();
        mockBlast2.setBlastId(UUID.randomUUID());
        mockBlast2.setOwnerId(ownerId);
        mockBlast2.setBlastName("Blast 2");
        mockBlast2.setMarkets(Arrays.asList("US", "UK"));
        mockBlast2.setContents(Arrays.asList(Blast.ContentType.FLOWS, Blast.ContentType.HOLDINGS));
        mockBlast2.setTriggerTime(LocalTime.of(15, 0));

        mockBlasts.add(mockBlast1);
        mockBlasts.add(mockBlast2);

        when(blastRepository.findAll()).thenReturn(mockBlasts);
        blastService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadBlasts() {
        // Arrange
        List<Blast> newBlasts = new ArrayList<>();
        Blast newBlast = new Blast();
        newBlast.setBlastId(UUID.randomUUID());
        newBlast.setOwnerId(ownerId);
        newBlast.setBlastName("New Blast");
        newBlast.setMarkets(Arrays.asList("JP"));
        newBlast.setContents(Arrays.asList(Blast.ContentType.IOIs));
        newBlast.setTriggerTime(LocalTime.of(10, 0));
        newBlasts.add(newBlast);
        when(blastRepository.findAll()).thenReturn(newBlasts);

        // Act
        blastService.reconfigure();

        // Assert
        List<Blast> result = blastService.getBlasts(ownerId);
        assertEquals(1, result.size());
        assertEquals("New Blast", result.get(0).getBlastName());
        assertEquals("JP", result.get(0).getMarkets().get(0));
        verify(blastRepository, times(2)).findAll();
    }

    @Test
    public void getBlasts_shouldReturnAllBlastsForOwner() {
        // Act
        List<Blast> result = blastService.getBlasts(ownerId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Blast 1", result.get(0).getBlastName());
        assertEquals("HK", result.get(0).getMarkets().get(0));
        assertEquals("Blast 2", result.get(1).getBlastName());
        assertEquals("US", result.get(1).getMarkets().get(0));
    }

    @Test
    public void saveBlast_withNewBlast_shouldSaveBlast() {
        // Arrange
        Blast newBlast = new Blast();
        newBlast.setBlastId(UUID.randomUUID());
        newBlast.setOwnerId(ownerId);
        newBlast.setBlastName("New Blast");
        newBlast.setMarkets(Arrays.asList("JP"));
        newBlast.setContents(Arrays.asList(Blast.ContentType.IOIs));
        newBlast.setTriggerTime(LocalTime.of(10, 0));
        when(blastRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(blastRepository.save(any(Blast.class))).thenReturn(newBlast);

        // Act
        Blast result = blastService.saveBlast(newBlast);

        // Assert
        assertNotNull(result);
        assertEquals("New Blast", result.getBlastName());
        assertEquals("JP", result.getMarkets().get(0));
        verify(blastRepository).save(newBlast);
        List<Blast> allBlasts = blastService.getBlasts(ownerId);
        assertEquals(3, allBlasts.size());
    }

    @Test
    public void saveBlast_withExistingBlast_shouldNotSaveBlast() {
        // Arrange
        when(blastRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockBlast1));

        // Act
        Blast result = blastService.saveBlast(mockBlast1);

        // Assert
        assertNotNull(result);
        assertEquals(mockBlast1, result);
        verify(blastRepository, never()).save(any(Blast.class));
        List<Blast> allBlasts = blastService.getBlasts(ownerId);
        assertEquals(2, allBlasts.size());
    }

    @Test
    public void updateBlast_withExistingBlast_shouldUpdateBlast() {
        // Arrange
        Blast updatedBlast = new Blast();
        updatedBlast.setBlastId(mockBlast1.getBlastId());
        updatedBlast.setOwnerId(ownerId);
        updatedBlast.setBlastName("Updated Blast");
        updatedBlast.setMarkets(Arrays.asList("AU"));
        updatedBlast.setContents(Arrays.asList(Blast.ContentType.NEWS));
        updatedBlast.setTriggerTime(LocalTime.of(11, 0));
        when(blastRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockBlast1));
        when(blastRepository.save(any(Blast.class))).thenReturn(updatedBlast);

        // Act
        Blast result = blastService.updateBlast(updatedBlast);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Blast", result.getBlastName());
        assertEquals("AU", result.getMarkets().get(0));
        verify(blastRepository).save(updatedBlast);
        List<Blast> allBlasts = blastService.getBlasts(ownerId);
        assertEquals(2, allBlasts.size());
    }

    @Test
    public void updateBlast_withNonExistingBlast_shouldNotUpdateBlast() {
        // Arrange
        Blast nonExistingBlast = new Blast();
        nonExistingBlast.setBlastId(UUID.randomUUID());
        nonExistingBlast.setOwnerId(ownerId);
        nonExistingBlast.setBlastName("Non Existing Blast");
        when(blastRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Blast result = blastService.updateBlast(nonExistingBlast);

        // Assert
        assertNotNull(result);
        assertEquals(nonExistingBlast, result);
        verify(blastRepository, never()).save(any(Blast.class));
        List<Blast> allBlasts = blastService.getBlasts(ownerId);
        assertEquals(2, allBlasts.size());
    }

    @Test
    public void deleteBlast_withExistingBlast_shouldDeleteBlast() {
        // Arrange
        String blastId = mockBlast1.getBlastId().toString();
        when(blastRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockBlast1));

        // Act
        blastService.deleteBlast(ownerId, blastId);

        // Assert
        verify(blastRepository).delete(mockBlast1);
        List<Blast> result = blastService.getBlasts(ownerId);
        assertEquals(1, result.size());
        assertEquals("Blast 2", result.get(0).getBlastName());
    }

    @Test
    public void deleteBlast_withNonExistingBlast_shouldNotDeleteAnything() {
        // Arrange
        String blastId = UUID.randomUUID().toString();
        when(blastRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        blastService.deleteBlast(ownerId, blastId);

        // Assert
        verify(blastRepository, never()).delete(any(Blast.class));
        List<Blast> result = blastService.getBlasts(ownerId);
        assertEquals(2, result.size());
    }
} 