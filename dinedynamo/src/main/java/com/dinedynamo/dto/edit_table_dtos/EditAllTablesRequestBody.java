package com.dinedynamo.dto.edit_table_dtos;

import com.dinedynamo.collections.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditAllTablesRequestBody
{
    Table[] listOfTables;
}
