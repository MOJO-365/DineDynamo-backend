package com.dinedynamo.dto.inventory_dtos;

import com.dinedynamo.collections.inventory_management.RawMaterial;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUsageForRawMaterialDTO {
    double amountUsed;
    String rawMaterialId;
}
