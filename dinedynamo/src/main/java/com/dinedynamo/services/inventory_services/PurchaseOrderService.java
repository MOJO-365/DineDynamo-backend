package com.dinedynamo.services.inventory_services;

import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import com.dinedynamo.collections.inventory_management.PurchaseOrderStatus;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.repositories.inventory_repositories.PurchaseOrderRepository;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.external_services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PurchaseOrderService {


    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    SupplierDetailsRepository supplierDetailsRepository;

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    RawMaterialService rawMaterialService;

    @Autowired
    EmailService emailService;

    public PurchaseOrder save(PurchaseOrder purchaseOrder){

        Restaurant restaurant = restaurantRepository.findById(purchaseOrder.getRestaurantId()).orElse(null);

        if(restaurant == null){
            System.out.println("RESTAURANT-ID NOT IN DB");
            throw new RuntimeException("Restaurant not found in db");
        }

        else{

            purchaseOrder.setDateOfPurchaseRequest(LocalDate.now());
            purchaseOrder.setStatus(PurchaseOrderStatus.REQUESTED);
            purchaseOrderRepository.save(purchaseOrder);

            emailService.sendMailForPurchaseOrder(purchaseOrder, restaurant.getRestaurantName());
            return purchaseOrder;

        }
    }

    public PurchaseOrder completePurchaseOrder(PurchaseOrder purchaseOrder){

        purchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getPurchaseOrderId()).orElse(null);
        if(purchaseOrder == null){
            System.out.println("NO SUCH PURCHASE ORDER FOUND IN DB");
            throw new RuntimeException("No purchase order found in database");
        }

        else{

            RawMaterial rawMaterial = rawMaterialRepository.findById(purchaseOrder.getRawMaterialId()).orElse(null);
            if(rawMaterial == null){
                throw new RuntimeException("No such raw material found in database");

            }
            rawMaterialService.addNewLevelToCurrentLevelOfMaterial(rawMaterial, purchaseOrder.getQuantity());

            purchaseOrder.setStatus(PurchaseOrderStatus.COMPLETED);
            purchaseOrderRepository.save(purchaseOrder);
            return purchaseOrder;
        }
    }

    public PurchaseOrder cancelPurchaseOrder(PurchaseOrder purchaseOrder){

        purchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getPurchaseOrderId()).orElse(null);
        Restaurant restaurant = restaurantRepository.findById(purchaseOrder.getRestaurantEmail()).orElse(null);
        if(purchaseOrder == null ){
            System.out.println("NO SUCH PURCHASE ORDER FOUND IN DB");
            throw new RuntimeException("No purchase order found in database");
        }

        else{

            emailService.sendMailForCancelPurchaseOrder(purchaseOrder, restaurant.getRestaurantName());
            purchaseOrder.setStatus(PurchaseOrderStatus.CANCELLED);
            purchaseOrderRepository.delete(purchaseOrder);
            return purchaseOrder;
        }
    }

    public PurchaseOrder changeStatusToRequested(PurchaseOrder purchaseOrder){

        purchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getPurchaseOrderId()).orElse(null);
        Restaurant restaurant = restaurantRepository.findById(purchaseOrder.getRestaurantEmail()).orElse(null);
        if(purchaseOrder == null ){
            System.out.println("NO SUCH PURCHASE ORDER FOUND IN DB");
            throw new RuntimeException("No purchase order found in database");
        }
        if(restaurant == null){
            System.out.println("RESTAURANT-ID NOT IN DB");
            throw new RuntimeException("Restaurant not found in db");
        }
        else{
            purchaseOrder.setStatus(PurchaseOrderStatus.REQUESTED);
            purchaseOrderRepository.save(purchaseOrder);
            return purchaseOrder;
        }
    }
}
