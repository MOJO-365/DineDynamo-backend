package com.dinedynamo.dto.table_dtos;

import com.dinedynamo.collections.table_collections.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditAllTablesRequestBody
{
    Table[] listOfTables;
}
