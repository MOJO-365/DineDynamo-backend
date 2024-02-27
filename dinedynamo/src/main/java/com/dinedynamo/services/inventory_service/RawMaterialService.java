package com.dinedynamo.services.inventory_service;



import com.dinedynamo.collections.inventory_management.RawMaterial;

import com.dinedynamo.collections.restaurant_collections.Restaurant;

import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.ReplenishmentLogRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.inventory_repositories.WastageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public RawMaterial updateRawMaterial(String rawMaterialId, RawMaterial updatedRawMaterial){

        if(rawMaterialId.equals("") || rawMaterialId.equals(" ") || rawMaterialId == null){
            System.out.println("PASS RAW MATERIAL ID IN REQUEST BODY: editRawMaterialDTO");
            return null;
        }

        updatedRawMaterial.setRawMaterialId(rawMaterialId);
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

        rawMaterial.setCurrentLevel(currentLevel);
        rawMaterialRepository.save(rawMaterial);
        return rawMaterial;
    }

}


