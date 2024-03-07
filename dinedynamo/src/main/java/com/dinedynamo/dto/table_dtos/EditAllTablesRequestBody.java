package com.dinedynamo.dto.table_dtos;

import com.dinedynamo.collections.table_collections.Table;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditAllTablesRequestBody
{
    Table[] listOfTables;
}
