package com.dinedynamo.collections;


import  lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Menu
{
    String restaurantId;
    @Id
    String menuId;

    JSONArray listOfMenuItem;


}
