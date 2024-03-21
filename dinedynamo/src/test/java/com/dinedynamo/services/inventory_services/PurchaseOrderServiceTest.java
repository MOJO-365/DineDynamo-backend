package com.dinedynamo.services.inventory_services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import com.dinedynamo.collections.inventory_management.PurchaseOrderStatus;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.inventory_repositories.PurchaseOrderRepository;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.external_services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private RawMaterialService rawMaterialService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSave_ValidPurchaseOrder() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setRestaurantId("restaurantId");
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("restaurantId");
        when(restaurantRepository.findById("restaurantId")).thenReturn(Optional.of(restaurant));

        // Act
        PurchaseOrder result = purchaseOrderService.save(purchaseOrder);

        // Assert
        assertNotNull(result);
        assertEquals(PurchaseOrderStatus.REQUESTED, result.getStatus());
        assertNotNull(result.getDateOfPurchaseRequest());
        verify(purchaseOrderRepository, times(1)).save(purchaseOrder);
        verify(emailService, times(1)).sendMailForPurchaseOrder(purchaseOrder, restaurant.getRestaurantName());
    }

    @Test
    void testSave_InvalidRestaurantId() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setRestaurantId("invalidRestaurantId");
        when(restaurantRepository.findById("invalidRestaurantId")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> purchaseOrderService.save(purchaseOrder));
        verify(purchaseOrderRepository, never()).save(any());
        verify(emailService, never()).sendMailForPurchaseOrder(any(), any());
    }

    @Test
    void testCompletePurchaseOrder_ValidPurchaseOrder() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId("purchaseOrderId");
        purchaseOrder.setRawMaterialId("rawMaterialId");
        purchaseOrder.setQuantity(10.0);
        RawMaterial rawMaterial = new RawMaterial();
        when(purchaseOrderRepository.findById("purchaseOrderId")).thenReturn(Optional.of(purchaseOrder));
        when(rawMaterialRepository.findById("rawMaterialId")).thenReturn(Optional.of(rawMaterial));

        // Act
        PurchaseOrder result = purchaseOrderService.completePurchaseOrder(purchaseOrder);

        // Assert
        assertNotNull(result);
        assertEquals(PurchaseOrderStatus.COMPLETED, result.getStatus());
        verify(rawMaterialService, times(1)).addNewLevelToCurrentLevelOfMaterial(rawMaterial, 10.0);
        verify(purchaseOrderRepository, times(1)).save(purchaseOrder);
    }

    @Test
    void testCompletePurchaseOrder_InvalidPurchaseOrder() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId("purchaseOrderId");
        when(purchaseOrderRepository.findById("purchaseOrderId")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> purchaseOrderService.completePurchaseOrder(purchaseOrder));
        verify(rawMaterialService, never()).addNewLevelToCurrentLevelOfMaterial(any(), anyDouble());
        verify(purchaseOrderRepository, never()).save(any());
    }

    @Test
    void testCancelPurchaseOrder_ValidPurchaseOrder() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId("purchaseOrderId");
        purchaseOrder.setRestaurantId("restaurantId");
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("restaurantId");
        when(purchaseOrderRepository.findById("purchaseOrderId")).thenReturn(Optional.of(purchaseOrder));
        when(restaurantRepository.findById("restaurantId")).thenReturn(Optional.of(restaurant));

        // Act
        PurchaseOrder result = purchaseOrderService.cancelPurchaseOrder(purchaseOrder);

        // Assert
        assertNotNull(result);
        assertEquals(PurchaseOrderStatus.CANCELLED, result.getStatus());
        verify(purchaseOrderRepository, times(1)).delete(purchaseOrder);
        verify(emailService, times(1)).sendMailForCancelPurchaseOrder(purchaseOrder, restaurant.getRestaurantName());
    }

    @Test
    void testCancelPurchaseOrder_InvalidPurchaseOrder() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId("purchaseOrderId");
        when(purchaseOrderRepository.findById("purchaseOrderId")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> purchaseOrderService.cancelPurchaseOrder(purchaseOrder));
        verify(purchaseOrderRepository, never()).delete(any());
        verify(emailService, never()).sendMailForCancelPurchaseOrder(any(), any());
    }

    @Test
    void testChangeStatusToRequested_ValidPurchaseOrder() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId("purchaseOrderId");
        when(purchaseOrderRepository.findById("purchaseOrderId")).thenReturn(Optional.of(purchaseOrder));

        // Act
        PurchaseOrder result = purchaseOrderService.changeStatusToRequested(purchaseOrder);

        // Assert
        assertNotNull(result);
        assertEquals(PurchaseOrderStatus.REQUESTED, result.getStatus());
        verify(purchaseOrderRepository, times(1)).save(purchaseOrder);
    }

    @Test
    void testChangeStatusToRequested_InvalidPurchaseOrder() {
        // Arrange
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId("purchaseOrderId");
        when(purchaseOrderRepository.findById("purchaseOrderId")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> purchaseOrderService.changeStatusToRequested(purchaseOrder));
        verify(purchaseOrderRepository, never()).save(any());
    }
}

