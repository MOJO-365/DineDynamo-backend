package com.dinedynamo.services.inventory_services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SupplierDetailsServiceTest {

    @Mock
    private SupplierDetailsRepository supplierDetailsRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private SupplierDetailsService supplierDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSave() {
        // Arrange
        SupplierDetails supplierDetails = new SupplierDetails();

        // Act
        SupplierDetails result = supplierDetailsService.save(supplierDetails);

        // Assert
        assertEquals(supplierDetails, result);
        verify(supplierDetailsRepository, times(1)).save(supplierDetails);
    }

    @Test
    void testUpdateSupplierDetails() {
        // Arrange
        String supplierId = "exampleId";
        SupplierDetails existingSupplierDetails = new SupplierDetails();
        existingSupplierDetails.setSupplierId(supplierId);
        SupplierDetails updatedSupplierDetails = new SupplierDetails();
        updatedSupplierDetails.setSupplierId(supplierId);
        when(supplierDetailsRepository.findById(supplierId)).thenReturn(Optional.of(existingSupplierDetails));

        // Act
        SupplierDetails result = supplierDetailsService.updateSupplierDetails(supplierId, updatedSupplierDetails);

        // Assert
        assertEquals(updatedSupplierDetails, result);
        verify(supplierDetailsRepository, times(1)).save(updatedSupplierDetails);
    }

    @Test
    void testGetItemsBySupplier() {
        // Arrange
        String supplierId = "exampleId";
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierId(supplierId);
        supplierDetails.setRawMaterialIdList(new String[]{"rawMaterialId1", "rawMaterialId2"});
        RawMaterial rawMaterial1 = new RawMaterial();
        RawMaterial rawMaterial2 = new RawMaterial();
        when(supplierDetailsRepository.findById(supplierId)).thenReturn(Optional.of(supplierDetails));
        when(rawMaterialRepository.findById("rawMaterialId1")).thenReturn(Optional.of(rawMaterial1));
        when(rawMaterialRepository.findById("rawMaterialId2")).thenReturn(Optional.of(rawMaterial2));

        // Act
        List<RawMaterial> result = supplierDetailsService.getItemsBySupplier(supplierDetails);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(rawMaterial1));
        assertTrue(result.contains(rawMaterial2));
    }

    @Test
    void testGetItemsBySupplier_NoSuchSupplier() {
        // Arrange
        String supplierId = "exampleId";
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierId(supplierId);
        when(supplierDetailsRepository.findById(supplierId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> supplierDetailsService.getItemsBySupplier(supplierDetails));
        verify(rawMaterialRepository, never()).findById(any());
    }

    @Test
    void testGetSupplierByRawMaterial() {
        // Arrange
        String restaurantId = "restaurantId";
        String rawMaterialId = "rawMaterialId";
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId(rawMaterialId);
        rawMaterial.setRestaurantId(restaurantId);
        SupplierDetails supplierDetails1 = new SupplierDetails();
        SupplierDetails supplierDetails2 = new SupplierDetails();
        String[] list=new String[2];
        list[0]=rawMaterialId;

        supplierDetails1.setRawMaterialIdList(list);
        supplierDetails2.setRawMaterialIdList(list);
        when(supplierDetailsRepository.findByRestaurantId(restaurantId)).thenReturn(Arrays.asList(supplierDetails1, supplierDetails2));

        // Act
        List<SupplierDetails> result = supplierDetailsService.getSupplierByRawMaterial(rawMaterial);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(supplierDetails1));
        assertTrue(result.contains(supplierDetails2));
    }
}
