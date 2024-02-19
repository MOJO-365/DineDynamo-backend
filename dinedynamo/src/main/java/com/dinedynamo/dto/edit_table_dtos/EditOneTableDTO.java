package com.dinedynamo.dto.edit_table_dtos;


import com.dinedynamo.collections.Table;
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
