package com.dinedynamo.services.inventory_service;


import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.WastageLog;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.WastageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WastageLogService {


    @Autowired
    WastageLogRepository wastageLogRepository;

    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    RawMaterialService rawMaterialService;

    boolean isRequestValid(WastageLog wastageLog){

        if(wastageLog.getRawMaterialId() == null || wastageLog.getRawMaterialId().equals(" ") || wastageLog.getRawMaterialId().equals(" ")
                || wastageLog.getRestaurantId().equals(" ") || wastageLog.getRestaurantId().equals(" ")
                 || wastageLog.getWastedQuantity() == 0.0
        ){

            return false;
        }
        return true;
    }


    @Transactional
    public WastageLog save(WastageLog wastageLog){

        boolean isRequestValid = isRequestValid(wastageLog);
        if(!isRequestValid){
            System.out.println("PASS RAW MATERIAL ID AND RESTAURANT ID IN REQUEST");
            return null;
        }

        RawMaterial rawMaterial = rawMaterialService.addWastage(wastageLog);

        if(rawMaterial == null){
            //no such raw material exists case
            return null;
        }
        wastageLog.setTimestamp(LocalDateTime.now());
        wastageLogRepository.save(wastageLog);
        return wastageLog;

    }


    @Transactional
    public WastageLog updateWastageLog(String wastageLogId, WastageLog updatedWastageLog){

        if(!isRequestValid(updatedWastageLog)){
            System.out.println("PASS RAW MATERIAL ID AND RESTAURANT ID WASTAGE LOG OBJ");
            return null;
        }

        RawMaterial rawMaterial = rawMaterialRepository.findById(updatedWastageLog.getRawMaterialId()).orElse(null);
        WastageLog existingWastageLog = wastageLogRepository.findById(wastageLogId).orElse(null);
        if(rawMaterial == null){
            System.out.println("RAW MATERIAL WITH THIS ID NOT FOUND IN DB");
            System.out.println("UPDATION FAILED");
            return null;
        }
        if(existingWastageLog == null){
            System.out.println("WASTAGE LOG WITH THIS ID NOT FOUND IN DB");
            System.out.println("UPDATION FAILED");
            return null;
        }

        rawMaterial.setCurrentLevel(existingWastageLog.getWastedQuantity() + rawMaterial.getCurrentLevel());
        rawMaterialRepository.save(rawMaterial);
        rawMaterialService.addWastage(updatedWastageLog);

        updatedWastageLog.setTimestamp(LocalDateTime.now());
        updatedWastageLog.setWastageLogId(wastageLogId);
        wastageLogRepository.save(updatedWastageLog);
        return updatedWastageLog;
    }
}
