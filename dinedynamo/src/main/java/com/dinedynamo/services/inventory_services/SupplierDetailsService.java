package com.dinedynamo.services.inventory_services;

import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SupplierDetailsService
{
    @Autowired
    SupplierDetailsRepository supplierDetailsRepository;

    @Autowired
    RawMaterialRepository rawMaterialRepository;



    public SupplierDetails save(SupplierDetails supplierDetails){

        supplierDetailsRepository.save(supplierDetails);
        return supplierDetails;
    }


    public SupplierDetails updateSupplierDetails(String supplierId, SupplierDetails updatedSupplierDetails){


        SupplierDetails existingSupplierDetails = supplierDetailsRepository.findById(supplierId).orElse(null);

        updatedSupplierDetails.setSupplierId(existingSupplierDetails.getSupplierId());

        supplierDetailsRepository.save(updatedSupplierDetails);
        return updatedSupplierDetails;
    }


    public List<RawMaterial> getItemsBySupplier(SupplierDetails supplierDetails){

        List<RawMaterial> rawMaterialList = new ArrayList<>();

        supplierDetails = supplierDetailsRepository.findById(supplierDetails.getSupplierId()).orElse(null);
        if(supplierDetails == null){
            throw new RuntimeException("No such supplier found");
        }

        for(String id: supplierDetails.getRawMaterialIdList()){

            RawMaterial rawMaterial = rawMaterialRepository.findById(id).orElse(null);

            if(rawMaterial!=null)   rawMaterialList.add(rawMaterial);
        }
        return rawMaterialList;
    }

    public List<SupplierDetails> getSupplierByRawMaterial(RawMaterial rawMaterial){

        String restaurantId = rawMaterial.getRestaurantId();
        List<SupplierDetails> supplierDetailsOfRestaurant = supplierDetailsRepository.findByRestaurantId(restaurantId);
        List<SupplierDetails> supplierForRawMaterial = new ArrayList<>();

        for(SupplierDetails supplierDetails: supplierDetailsOfRestaurant){

            String[] rawMaterialIdList = supplierDetails.getRawMaterialIdList();

            List<String> rawMaterials = Arrays.asList(rawMaterialIdList);

            if(rawMaterials.contains(rawMaterial.getRawMaterialId())){
                supplierForRawMaterial.add(supplierDetails);
            }
        }

        return supplierForRawMaterial;
    }
}
