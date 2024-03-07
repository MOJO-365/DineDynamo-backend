package com.dinedynamo.dto.inventory_dtos;


import com.dinedynamo.collections.inventory_management.RawMaterial;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RawMaterialAndLogsDataDTO {

    RawMaterial rawMaterial;

    int wastageLogsCount;

    int replenishmentLogsCount;
}
