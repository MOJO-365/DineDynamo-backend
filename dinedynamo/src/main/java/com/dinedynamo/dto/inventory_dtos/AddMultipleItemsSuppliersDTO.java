package com.dinedynamo.dto.inventory_dtos;


import com.dinedynamo.collections.inventory_management.SupplierDetails;
import lombok.*;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddMultipleItemsSuppliersDTO {


    String restaurantId;

    String[] rawMaterialIdList;

    String supplierName;

    String supplierPhone;

    String supplierAddress;

    String supplierEmailId;
}
