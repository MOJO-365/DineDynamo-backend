package com.dinedynamo.services.inventory_services;

import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierDetailsService
{
    @Autowired
    SupplierDetailsRepository supplierDetailsRepository;

    @Autowired
    RawMaterialRepository rawMaterialRepository;


    public SupplierDetails save(SupplierDetails supplierDetails){

        RawMaterial rawMaterial = rawMaterialRepository.findById(supplierDetails.getRawMaterialId()).orElse(null);

        if(rawMaterial == null){
            System.out.println("RAW MATERIAL ID NOT IN DATABASE");
            return null;
        }


        supplierDetailsRepository.save(supplierDetails);
        return supplierDetails;
    }

    public SupplierDetails updateSupplierDetails(String supplierId, SupplierDetails updatedSupplierDetails){

        RawMaterial rawMaterial = rawMaterialRepository.findById(updatedSupplierDetails.getRawMaterialId()).orElse(null);

        if(rawMaterial == null){
            System.out.println("RAW MATERIAL ID NOT IN DATABASE");
            return null;
        }
        updatedSupplierDetails.setSupplierId(supplierId);
        supplierDetailsRepository.save(updatedSupplierDetails);
        return updatedSupplierDetails;
    }
}
