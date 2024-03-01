package com.dinedynamo.services.inventory_services;

import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.ReplenishmentLog;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.ReplenishmentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReplenishmentLogService
{

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    RawMaterialService rawMaterialService;

    @Autowired
    ReplenishmentLogRepository replenishmentLogRepository;

    public ReplenishmentLog save(ReplenishmentLog replenishmentLog){

        RawMaterial rawMaterial = rawMaterialRepository.findById(replenishmentLog.getRawMaterialId()).orElse(null);

        if(rawMaterial == null){
            System.out.println("RAW MATERIAL ID INCORRECT, NOT FOUND IN DB");
            return null;
        }

        if(replenishmentLog.getReplenishedQuantity() == 0.0){
            System.out.println("QUANTITY CANNOT BE ZERO FOR REPLENISHMENT");
            return null;
        }

        else{

            rawMaterial.setCurrentLevel(rawMaterial.getCurrentLevel() + replenishmentLog.getReplenishedQuantity());

            rawMaterialRepository.save(rawMaterial);
            rawMaterialService.updateStatusOfRawMaterial(rawMaterial);

            replenishmentLog.setTimestamp(LocalDateTime.now());
            replenishmentLogRepository.save(replenishmentLog);

            System.out.println("REPLENISHMENT DATA SAVED, RAW MATERIAL-CURRENT LEVEL UPDATED");
        }

        return replenishmentLog;

    }
}
