package com.dinedynamo.collections.inventory_management;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("suppliers")
public class SupplierDetails
{

    @Id
    String supplierId;

    String restaurantId;

    String[] rawMaterialIdList;

    String supplierName;

    String supplierPhone;

    String supplierAddress;

    String supplierEmailId;


}
