package com.dinedynamo.services.inventory_services;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.RawMaterialStatus;
import com.dinedynamo.dto.inventory_dtos.RawMaterialAndLogsDataDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialDTO;
import com.dinedynamo.collections.inventory_management.*;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.ReplenishmentLogRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.inventory_repositories.WastageLogRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private SupplierDetailsRepository supplierDetailsRepository;

    @Mock
    private ReplenishmentLogRepository replenishmentLogRepository;

    @Mock
    private WastageLogRepository wastageLogRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSave_ValidRawMaterial() {
        // Arrange
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCurrentLevel(20);
        rawMaterial.setReorderLevel(5);
        rawMaterial.setPurchaseDate(LocalDate.now());
        rawMaterial.setExpirationDate(LocalDate.now().plusDays(10));
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);

        // Act
        RawMaterialDTO result = rawMaterialService.save(rawMaterial);

        // Assert
        assertEquals(com.dinedynamo.dto.inventory_dtos.RawMaterialStatus.VALID_AND_SAVED, result.getStatus());
        verify(rawMaterialRepository, times(1)).save(any());
    }

    @Test
    void testSave_InvalidExpirationDate() {

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setPurchaseDate(LocalDate.now());
        rawMaterial.setExpirationDate(LocalDate.now().minusDays(1));

        RawMaterialDTO result = rawMaterialService.save(rawMaterial);
        assertEquals(result.getStatus(), com.dinedynamo.dto.inventory_dtos.RawMaterialStatus.INVALID_EXPIRY);
        verify(rawMaterialRepository, never()).save(any());
    }

    @Test
    void testUpdateRawMaterial_ValidRawMaterial() {
        // Arrange
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setPurchaseDate(LocalDate.now());
        rawMaterial.setExpirationDate(LocalDate.now().plusDays(1));
        String rawMaterialId = "exampleId";
        when(rawMaterialRepository.findById(rawMaterialId)).thenReturn(java.util.Optional.of(rawMaterial));
        when(rawMaterialRepository.save(any())).thenReturn(rawMaterial);

        // Act
        RawMaterialDTO result = rawMaterialService.updateRawMaterial(rawMaterialId, rawMaterial);

        // Assert
        assertEquals(com.dinedynamo.dto.inventory_dtos.RawMaterialStatus.VALID_AND_SAVED, result.getStatus());
        verify(rawMaterialRepository, times(2)).save(any());
    }

    @Test
    void testUpdateRawMaterial_InvalidExpirationDate() {
        // Arrange
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setPurchaseDate(LocalDate.now());
        rawMaterial.setExpirationDate(LocalDate.now().minusDays(1));
        String rawMaterialId = "exampleId";

        // Act
        RawMaterialDTO result = rawMaterialService.updateRawMaterial(rawMaterialId, rawMaterial);

        // Assert
        assertEquals(com.dinedynamo.dto.inventory_dtos.RawMaterialStatus.INVALID_EXPIRY, result.getStatus());
        verify(rawMaterialRepository, never()).save(any());
    }

    @Test
    void testDeleteRawMaterial() {
        // Arrange
        RawMaterial rawMaterial = new RawMaterial();

        // Act
        boolean result = rawMaterialService.deleteRawMaterial(rawMaterial);

        // Assert
        assertTrue(result);
        verify(wastageLogRepository, times(1)).deleteByRawMaterialId(rawMaterial.getRawMaterialId());
        verify(replenishmentLogRepository, times(1)).deleteByRawMaterialId(rawMaterial.getRawMaterialId());
        verify(rawMaterialRepository, times(1)).delete(rawMaterial);
    }

    @Test
    void testDeleteAllRawMaterials() {
        // Arrange
        Restaurant restaurant = new Restaurant();

        // Act
        boolean result = rawMaterialService.deleteAllRawMaterials(restaurant);

        // Assert
        assertTrue(result);
        verify(wastageLogRepository, times(1)).deleteByRestaurantId(restaurant.getRestaurantId());
        verify(replenishmentLogRepository, times(1)).deleteByRestaurantId(restaurant.getRestaurantId());
        verify(supplierDetailsRepository, times(1)).deleteByRestaurantId(restaurant.getRestaurantId());
        verify(rawMaterialRepository, times(1)).deleteByRestaurantId(restaurant.getRestaurantId());
    }

    @Test
    void testAddUsage() {
        // Arrange
        String rawMaterialId = "exampleId";
        double amountUsed = 10.0;
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId(rawMaterialId);
        rawMaterial.setCurrentLevel(20.0);
        rawMaterialRepository.save(rawMaterial);
        when(rawMaterialRepository.findById(rawMaterialId)).thenReturn(java.util.Optional.of(rawMaterial));
        when(rawMaterialRepository.save(any())).thenReturn(rawMaterial);

        // Act
        RawMaterialDTO result = rawMaterialService.addUsage(rawMaterialId, amountUsed);

        // Assert
        assertEquals(com.dinedynamo.dto.inventory_dtos.RawMaterialStatus.POSITIVE, result.getStatus());
        assertEquals(10.0, rawMaterial.getCurrentLevel());
        verify(rawMaterialRepository, times(3)).save(any());
    }

    @Test
    void testAddWastage() {
        // Arrange
        WastageLog wastageLog = new WastageLog();
        wastageLog.setWastedQuantity(5.0);
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCurrentLevel(20.0);
        when(rawMaterialRepository.findById(wastageLog.getRawMaterialId())).thenReturn(java.util.Optional.of(rawMaterial));
        when(rawMaterialRepository.save(any())).thenReturn(rawMaterial);

        // Act
        RawMaterialDTO result = rawMaterialService.addWastage(wastageLog);

        // Assert
        assertEquals(com.dinedynamo.dto.inventory_dtos.RawMaterialStatus.POSITIVE, result.getStatus());
        assertEquals(15.0, rawMaterial.getCurrentLevel());
        verify(rawMaterialRepository, times(2)).save(any());
    }




    @Test
    void testViewRawMaterialAndLogs() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("exampleId");

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId("rawMaterialId");
        rawMaterial.setCurrentLevel(10.0);

        List<RawMaterial> rawMaterialList = Arrays.asList(rawMaterial);
        List<WastageLog> wastageLogList = Arrays.asList(new WastageLog());
        List<ReplenishmentLog> replenishmentLogList = Arrays.asList(new ReplenishmentLog());

        when(restaurantRepository.findByRestaurantId(restaurant.getRestaurantId())).thenReturn(restaurant);
        when(rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId())).thenReturn(rawMaterialList);
        when(wastageLogRepository.findByRawMaterialId(rawMaterial.getRawMaterialId())).thenReturn(wastageLogList);
        when(replenishmentLogRepository.findByRawMaterialId(rawMaterial.getRawMaterialId())).thenReturn(replenishmentLogList);

        // Act
        List<RawMaterialAndLogsDataDTO> result = rawMaterialService.viewRawMaterialAndLogs(restaurant);

        // Assert
        assertEquals(1, result.size());
        assertEquals(rawMaterial, result.get(0).getRawMaterial());
        assertEquals(wastageLogList, result.get(0).getWastageLogList());
        assertEquals(replenishmentLogList, result.get(0).getReplenishmentLogList());
        verify(rawMaterialRepository, times(1)).findByRestaurantId(restaurant.getRestaurantId());
        verify(wastageLogRepository, times(1)).findByRawMaterialId(rawMaterial.getRawMaterialId());
        verify(replenishmentLogRepository, times(1)).findByRawMaterialId(rawMaterial.getRawMaterialId());
    }
}
