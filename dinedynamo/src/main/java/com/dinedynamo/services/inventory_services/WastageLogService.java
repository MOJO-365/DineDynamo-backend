package com.dinedynamo.services.inventory_services;


import com.dinedynamo.collections.inventory_management.WastageLog;
import com.dinedynamo.dto.inventory_dtos.RawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialStatus;
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
    public boolean save(WastageLog wastageLog){

        RawMaterialDTO rawMaterialDTO = rawMaterialService.addWastage(wastageLog);

        if(rawMaterialDTO.getStatus() == RawMaterialStatus.NEGATIVE){
            return false;
        }
        else{
            wastageLog.setTimestamp(LocalDateTime.now());
            wastageLogRepository.save(wastageLog);
            return true;
        }

    }

}
