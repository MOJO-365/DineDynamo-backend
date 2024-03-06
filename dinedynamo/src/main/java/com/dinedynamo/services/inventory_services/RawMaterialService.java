package com.dinedynamo.services.inventory_services;



import com.dinedynamo.collections.inventory_management.RawMaterial;

import com.dinedynamo.collections.inventory_management.WastageLog;
import com.dinedynamo.collections.restaurant_collections.Restaurant;

import com.dinedynamo.dto.inventory_dtos.RawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialStatus;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.ReplenishmentLogRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.inventory_repositories.WastageLogRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    RestaurantRepository restaurantRepository;


    boolean isRequestValid(RawMaterial rawMaterial){

        if(rawMaterial.getRestaurantId().equals(" ") || rawMaterial.getRestaurantId().equals(" ")
                || rawMaterial.getRestaurantId() == null || rawMaterial.getReorderLevel() == 0.0 || rawMaterial.getCurrentLevel() == 0.0
        ){

            System.out.println("NO RESTAURANT ID OR NO CURRENT/REORDER LEVEL FOUND IN REQUEST");
            return false;
        }
        return true;
    }

    public RawMaterialDTO save(RawMaterial rawMaterial){


        if(rawMaterial.getExpirationDate().isBefore(rawMaterial.getPurchaseDate())){
            System.out.println("EXP DATE CANNOT BE BEFORE THE PURCHASE DATE");
            RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
            rawMaterialDTO.setStatus(RawMaterialStatus.INVALID_EXPIRY);
            rawMaterialDTO.setRawMaterial(rawMaterial);
            return rawMaterialDTO;
        }

        else{
            rawMaterial.setTimestamp(LocalDateTime.now());
            rawMaterialRepository.save(rawMaterial);

            rawMaterial = updateStatusOfRawMaterial(rawMaterial);

            RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
            rawMaterialDTO.setStatus(RawMaterialStatus.VALID_AND_SAVED);
            rawMaterialDTO.setRawMaterial(rawMaterial);
            return rawMaterialDTO;

        }
    }

    public RawMaterialDTO updateRawMaterial(String rawMaterialId, RawMaterial updatedRawMaterial){


        if(updatedRawMaterial.getExpirationDate().isBefore(updatedRawMaterial.getPurchaseDate())){
            System.out.println("EXP DATE CANNOT BE BEFORE THE PURCHASE DATE");
            RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
            rawMaterialDTO.setStatus(RawMaterialStatus.INVALID_EXPIRY);
            rawMaterialDTO.setRawMaterial(updatedRawMaterial);
            return rawMaterialDTO;
        }
        else{
            updatedRawMaterial.setRawMaterialId(rawMaterialId);
            updatedRawMaterial.setTimestamp(LocalDateTime.now());
            rawMaterialRepository.save(updatedRawMaterial);

            updatedRawMaterial = updateStatusOfRawMaterial(updatedRawMaterial);


            RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
            rawMaterialDTO.setStatus(RawMaterialStatus.VALID_AND_SAVED);
            rawMaterialDTO.setRawMaterial(updatedRawMaterial);
            return rawMaterialDTO;

        }
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
    public RawMaterialDTO addUsage(String rawMaterialId, double amountUsed){

        RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId).orElse(null);
        if(rawMaterial == null){
            System.out.println("In addUsage(): RawMaterial not in db");
            throw new RuntimeException("No such raw material found in database");

        }
        else{

            double currentLevel = rawMaterial.getCurrentLevel();
            currentLevel -= amountUsed;

            boolean isUpdationOfCurrentLevelValid = isUpdationOfCurrentLevelValid(rawMaterial, amountUsed);

            if(!isUpdationOfCurrentLevelValid){
                System.out.println("NEGATIVE CURRENT LEVEL AFTER REMOVING WASTAGE QTY");

                RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
                rawMaterialDTO.setRawMaterial(rawMaterial);
                rawMaterialDTO.setStatus(RawMaterialStatus.NEGATIVE);
                return rawMaterialDTO;
            }

            else{
                rawMaterial.setCurrentLevel(currentLevel);
                rawMaterial.setTimestamp(LocalDateTime.now());
                rawMaterialRepository.save(rawMaterial);
                rawMaterial = updateStatusOfRawMaterial(rawMaterial);


                RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
                rawMaterialDTO.setStatus(RawMaterialStatus.POSITIVE);
                rawMaterialDTO.setRawMaterial(rawMaterial);
                return rawMaterialDTO;
            }

        }
    }

    @Transactional
    public RawMaterialDTO addWastage(WastageLog wastageLog){

        RawMaterial rawMaterial = rawMaterialRepository.findById(wastageLog.getRawMaterialId()).orElse(null);

        if(rawMaterial == null){
            System.out.println("NO SUCH RAW MATERIAL EXISTS IN DB");
            throw new RuntimeException("No such raw material found in database");
        }

        else{

            boolean isUpdationOfCurrentLevelValid = isUpdationOfCurrentLevelValid(rawMaterial, wastageLog.getWastedQuantity());

            if(!isUpdationOfCurrentLevelValid){
                System.out.println("NEGATIVE CURRENT LEVEL AFTER REMOVING WASTAGE QTY");

                RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
                rawMaterialDTO.setRawMaterial(rawMaterial);
                rawMaterialDTO.setStatus(RawMaterialStatus.NEGATIVE);
                return rawMaterialDTO;
            }

            else{
                double currentLevel = rawMaterial.getCurrentLevel();
                currentLevel -= wastageLog.getWastedQuantity();

                rawMaterial.setCurrentLevel(currentLevel);
                rawMaterial.setTimestamp(LocalDateTime.now());

                rawMaterialRepository.save(rawMaterial);

                rawMaterial = updateStatusOfRawMaterial(rawMaterial);

                RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
                rawMaterialDTO.setStatus(RawMaterialStatus.POSITIVE);
                rawMaterialDTO.setRawMaterial(rawMaterial);
                return rawMaterialDTO;
            }
        }
    }


    public List<RawMaterial> updateStatusOfAllRawMaterials(Restaurant restaurant){

        restaurant = restaurantRepository.findByRestaurantId(restaurant.getRestaurantId());

        List<RawMaterial> rawMaterialList = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId());

        for(RawMaterial rawMaterial: rawMaterialList){

            updateStatusOfRawMaterial(rawMaterial);
        }

        rawMaterialList = rawMaterialRepository.findByRestaurantId(restaurant.getRestaurantId());
        return rawMaterialList;
    }

    public RawMaterial updateStatusOfRawMaterial(RawMaterial rawMaterial){

        rawMaterial = rawMaterialRepository.findById(rawMaterial.getRawMaterialId()).orElse(null);
        if(rawMaterial == null){
            System.out.println("In updateStatusOfRawMaterial mtd: RAW MATERIAL NOT PRESENT IN DB");
            return null;
        }

        double currentLevel = rawMaterial.getCurrentLevel();
        double reorderLevel = rawMaterial.getReorderLevel();

        if (currentLevel <= reorderLevel) {
            rawMaterial.setStatus(com.dinedynamo.collections.inventory_management.RawMaterialStatus.CRITICAL);
        } else if (currentLevel <= reorderLevel + 5) {
            rawMaterial.setStatus(com.dinedynamo.collections.inventory_management.RawMaterialStatus.NEAR_REORDER);
        } else {
            rawMaterial.setStatus(com.dinedynamo.collections.inventory_management.RawMaterialStatus.SUFFICIENT);
        }

        rawMaterialRepository.save(rawMaterial);
        return rawMaterial;
    }


    public boolean isUpdationOfCurrentLevelValid(RawMaterial rawMaterial, double level){

        rawMaterial = rawMaterialRepository.findById(rawMaterial.getRawMaterialId()).orElse(null);
        if(rawMaterial == null){
            throw new RuntimeException("Raw material does not exist in db");
        }
        else{
            double currentLevel = rawMaterial.getCurrentLevel();
            if(currentLevel - level < 0){
                return false;
            }
            return true;
        }
    }
}


