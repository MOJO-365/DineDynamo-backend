package com.dinedynamo.collections.inventory_management;


import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierDetails
{

    @Id
    String supplierId;

    String restaurantId;

    String rawMaterialId;

    String supplierName;

    String supplierPhone;

    String supplierAddress;

    String supplierEmailId;


}
