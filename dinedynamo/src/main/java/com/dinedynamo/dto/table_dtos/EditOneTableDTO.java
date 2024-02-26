package com.dinedynamo.dto.table_dtos;


import com.dinedynamo.collections.table_collections.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditOneTableDTO
{
    String tableId;

    Table table;
}
