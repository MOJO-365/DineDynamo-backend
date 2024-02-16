package com.dinedynamo.dto;


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
