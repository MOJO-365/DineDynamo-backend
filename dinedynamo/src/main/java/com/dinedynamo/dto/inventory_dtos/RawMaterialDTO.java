package com.dinedynamo.dto.inventory_dtos;

import com.dinedynamo.collections.inventory_management.RawMaterial;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RawMaterialDTO
{
    RawMaterialStatus status;

    RawMaterial rawMaterial;


}
