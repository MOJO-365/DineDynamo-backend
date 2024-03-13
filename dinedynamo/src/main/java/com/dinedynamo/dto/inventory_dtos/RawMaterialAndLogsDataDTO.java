package com.dinedynamo.dto.inventory_dtos;


import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.ReplenishmentLog;
import com.dinedynamo.collections.inventory_management.WastageLog;
import jdk.dynalink.linker.LinkerServices;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RawMaterialAndLogsDataDTO {

    RawMaterial rawMaterial;

    List<WastageLog> wastageLogList;

    List<ReplenishmentLog> replenishmentLogList;

}
