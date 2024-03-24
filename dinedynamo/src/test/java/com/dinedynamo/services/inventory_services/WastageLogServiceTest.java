package com.dinedynamo.services.inventory_services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.dinedynamo.collections.inventory_management.WastageLog;
import com.dinedynamo.dto.inventory_dtos.RawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialStatus;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.WastageLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WastageLogServiceTest {

    @Mock
    private WastageLogRepository wastageLogRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private RawMaterialService rawMaterialService;

    @InjectMocks
    private WastageLogService wastageLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSave_ValidWastageLog() {
        // Arrange
        WastageLog wastageLog = new WastageLog();
        RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
        rawMaterialDTO.setStatus(RawMaterialStatus.POSITIVE);
        when(rawMaterialService.addWastage(wastageLog)).thenReturn(rawMaterialDTO);

        // Act
        boolean result = wastageLogService.save(wastageLog);

        // Assert
        assertTrue(result);
        verify(wastageLogRepository, times(1)).save(wastageLog);
    }

    @Test
    void testSave_InvalidWastageLog() {
        // Arrange
        WastageLog wastageLog = new WastageLog();
        RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
        rawMaterialDTO.setStatus(RawMaterialStatus.NEGATIVE);
        when(rawMaterialService.addWastage(wastageLog)).thenReturn(rawMaterialDTO);

        // Act
        boolean result = wastageLogService.save(wastageLog);

        // Assert
        assertFalse(result);
        verify(wastageLogRepository, never()).save(any());
    }
}
