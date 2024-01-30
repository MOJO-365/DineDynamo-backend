package com.dinedynamo.dto;

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
