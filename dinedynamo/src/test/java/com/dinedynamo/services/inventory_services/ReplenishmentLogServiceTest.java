package com.dinedynamo.services.inventory_services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.ReplenishmentLog;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.ReplenishmentLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReplenishmentLogServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private RawMaterialService rawMaterialService;

    @Mock
    private ReplenishmentLogRepository replenishmentLogRepository;

    @InjectMocks
    private ReplenishmentLogService replenishmentLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSave_ValidReplenishmentLog() {
        // Arrange
        ReplenishmentLog replenishmentLog = new ReplenishmentLog();
        replenishmentLog.setRawMaterialId("rawMaterialId");
        replenishmentLog.setReplenishedQuantity(10.0);
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCurrentLevel(20.0);
        when(rawMaterialRepository.findById("rawMaterialId")).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialService.updateStatusOfRawMaterial(rawMaterial)).thenReturn(rawMaterial);

        // Act
        ReplenishmentLog result = replenishmentLogService.save(replenishmentLog);

        // Assert
        assertNotNull(result);
        assertEquals(replenishmentLog, result);
        assertEquals(30.0, rawMaterial.getCurrentLevel());
        verify(rawMaterialRepository, times(1)).save(rawMaterial);
        verify(replenishmentLogRepository, times(1)).save(replenishmentLog);
    }

    @Test
    void testSave_RawMaterialNotFound() {
        // Arrange
        ReplenishmentLog replenishmentLog = new ReplenishmentLog();
        replenishmentLog.setRawMaterialId("rawMaterialId");
        replenishmentLog.setReplenishedQuantity(10.0);
        when(rawMaterialRepository.findById("rawMaterialId")).thenReturn(Optional.empty());

        // Act
        ReplenishmentLog result = replenishmentLogService.save(replenishmentLog);

        // Assert
        assertNull(result);
        verify(rawMaterialRepository, never()).save(any());
        verify(replenishmentLogRepository, never()).save(any());
    }

    @Test
    void testSave_ZeroReplenishedQuantity() {
        // Arrange
        ReplenishmentLog replenishmentLog = new ReplenishmentLog();
        replenishmentLog.setRawMaterialId("rawMaterialId");
        replenishmentLog.setReplenishedQuantity(0.0);

        // Act
        ReplenishmentLog result = replenishmentLogService.save(replenishmentLog);

        // Assert
        assertNull(result);
        verify(rawMaterialRepository, never()).save(any());
        verify(replenishmentLogRepository, never()).save(any());
    }
}
