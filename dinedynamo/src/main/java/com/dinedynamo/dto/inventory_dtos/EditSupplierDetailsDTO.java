package com.dinedynamo.dto.inventory_dtos;


import com.dinedynamo.collections.inventory_management.SupplierDetails;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EditSupplierDetailsDTO {
    String supplierId;
    SupplierDetails supplierDetails;
}
