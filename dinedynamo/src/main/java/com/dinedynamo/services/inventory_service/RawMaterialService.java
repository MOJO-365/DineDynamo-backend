package com.dinedynamo.services.inventory_service;



import com.dinedynamo.collections.inventory_management.RawMaterial;

import com.dinedynamo.collections.inventory_management.WastageLog;
import com.dinedynamo.collections.restaurant_collections.Restaurant;

import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.ReplenishmentLogRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.inventory_repositories.WastageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RawMaterialService {

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    SupplierDetailsRepository supplierDetailsRepository;

    @Autowired
    ReplenishmentLogRepository replenishmentLogRepository;

    @Autowired
    WastageLogRepository wastageLogRepository;



    boolean isRequestValid(RawMaterial rawMaterial){

        if(rawMaterial.getRestaurantId().equals(" ") || rawMaterial.getRestaurantId().equals(" ")
                || rawMaterial.getRestaurantId() == null || rawMaterial.getReorderLevel() == 0.0 || rawMaterial.getCurrentLevel() == 0.0
        ){

            System.out.println("NO RESTAURANT ID OR NO CURRENT/REORDER LEVEL FOUND IN REQUEST");
            return false;
        }
        return true;
    }

    public RawMaterial save(RawMaterial rawMaterial){

        if(!isRequestValid(rawMaterial)){
            return null;
        }
        rawMaterial.setTimestamp(LocalDateTime.now());
        rawMaterialRepository.save(rawMaterial);
        return rawMaterial;
    }

    public RawMaterial updateRawMaterial(String rawMaterialId, RawMaterial updatedRawMaterial){

        if(rawMaterialId.equals("") || rawMaterialId.equals(" ") || rawMaterialId == null){
            System.out.println("PASS RAW MATERIAL ID IN REQUEST BODY: editRawMaterialDTO");
            return null;
        }
        if(!isRequestValid(updatedRawMaterial)){
            return null;
        }


        updatedRawMaterial.setRawMaterialId(rawMaterialId);
        updatedRawMaterial.setTimestamp(LocalDateTime.now());
        rawMaterialRepository.save(updatedRawMaterial);
        return updatedRawMaterial;
    }

    public boolean deleteRawMaterial(RawMaterial rawMaterial){

        wastageLogRepository.deleteByRawMaterialId(rawMaterial.getRawMaterialId());
        replenishmentLogRepository.deleteByRawMaterialId(rawMaterial.getRawMaterialId());
        supplierDetailsRepository.deleteByRawMaterialId(rawMaterial.getRawMaterialId());
        rawMaterialRepository.delete(rawMaterial);
        return true;

    }

    public boolean deleteAllRawMaterials(Restaurant restaurant){

        wastageLogRepository.deleteByRestaurantId(restaurant.getRestaurantId());
        replenishmentLogRepository.deleteByRestaurantId(restaurant.getRestaurantId());
        supplierDetailsRepository.deleteByRestaurantId(restaurant.getRestaurantId());
        rawMaterialRepository.deleteByRestaurantId(restaurant.getRestaurantId());
        return true;

    }

    @Transactional
    public RawMaterial addUsage(RawMaterial rawMaterial, double amountUsed){

        double currentLevel = rawMaterial.getCurrentLevel();
        currentLevel -= amountUsed;

        if(currentLevel < 0){
            System.out.println("CURRENT LEVEL VALUE LESS THAN USAGE QTY");
            currentLevel = 0;
        }

        rawMaterial.setCurrentLevel(currentLevel);
        rawMaterial.setTimestamp(LocalDateTime.now());
        rawMaterialRepository.save(rawMaterial);
        return rawMaterial;
    }

    @Transactional
    public RawMaterial addWastage(WastageLog wastageLog){

        RawMaterial rawMaterial = rawMaterialRepository.findById(wastageLog.getRawMaterialId()).orElse(null);
        if(rawMaterial == null){
            System.out.println("NO SUCH RAW MATERIAL EXISTS IN DB");
            return null;
        }

        double currentLevel = rawMaterial.getCurrentLevel();
        currentLevel -= wastageLog.getWastedQuantity();

        if(currentLevel < 0){
            System.out.println("CURRENT LEVEL VALUE LESS THAN WASTAGE QTY");
            currentLevel = 0;
        }

        rawMaterial.setCurrentLevel(currentLevel);
        rawMaterial.setTimestamp(LocalDateTime.now());
        rawMaterialRepository.save(rawMaterial);
        return rawMaterial;
    }
}


