package com.dinedynamo.dto.inventory_dtos;


import com.dinedynamo.collections.inventory_management.WastageLog;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EditWastageLogDTO {

    String wastageLogId;

    WastageLog wastageLog;
}
