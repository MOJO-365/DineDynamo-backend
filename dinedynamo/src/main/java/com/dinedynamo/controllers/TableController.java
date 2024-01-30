package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.dto.EditAllTablesRequestBody;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.Table;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TableRepository;
import com.dinedynamo.services.CloudinaryService;
import com.dinedynamo.services.TableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class TableController
{
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    TableService tableService;

    @Autowired
    CloudinaryService cloudinaryService;


    @GetMapping("/dinedynamo/restaurant/test-table")
    public String test(){
        return "hello world";
    }

    @PostMapping("/dinedynamo/restaurant/table/getalltables")
    public ResponseEntity<ApiResponse> getAllTables(@RequestBody Restaurant restaurant){

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).get();
        System.out.println(restaurant.getRestaurantName());
        List<Table> listOfAllTables = tableRepository.findByRestaurantId(restaurant.getRestaurantId());
        System.out.println(listOfAllTables.get(0));
        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",listOfAllTables.toArray()),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/restaurant/table/addtable")
    public ResponseEntity<ApiResponse> addTable(@RequestBody Table table) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(table.getRestaurantId()).get();
        table.setIsPositionAbsolute(false);
        table.setRestaurantId(restaurant.getRestaurantId());

        tableService.save(table);


        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",table),HttpStatus.OK);

    }

    @DeleteMapping("/dinedynamo/restaurant/table/deletetable")
    public ResponseEntity<ApiResponse> deleteTable(@RequestBody Table table) throws IOException {

        table = tableService.findById(table.getTableId());
        tableService.delete(table);
        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",table),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/restaurant/table/gettable")
    public ResponseEntity<ApiResponse> getTable(@RequestBody Table table){

        table = tableService.findById(table.getTableId());
        System.out.println("TABLE: "+table.getTableName());

        if(table == null){
            return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
        }

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",table),HttpStatus.OK);
    }



    //This method does not update the QR codes every time. It will only update the details other than table QR URL
    @PutMapping("/dinedynamo/restaurant/table/editalltables")
    public ResponseEntity<ApiResponse> editAllTables(@RequestBody EditAllTablesRequestBody editAllTablesRequestBody) throws IOException {

        List<Table> listOfUpdatedTables = new ArrayList<Table>();

        for(Table table: editAllTablesRequestBody.getListOfTables()){
            String tableId = table.getTableId();
            if(tableId != null){
                Table existingTable = tableService.findById(table.getTableId());

                    if(existingTable!=null){
                        Table updatedTable = new Table();
                        updatedTable.setTableId(existingTable.getTableId());
                        updatedTable.setCapacity(table.getCapacity());
                        updatedTable.setCoordinateX(table.getCoordinateX());
                        updatedTable.setCoordinateY(table.getCoordinateY());
                        updatedTable.setStatus(table.getStatus());
                        updatedTable.setTableName(table.getTableName());
                        updatedTable.setIsPositionAbsolute(table.getIsPositionAbsolute());
                        updatedTable.setRestaurantId(existingTable.getRestaurantId());

                        tableService.delete(existingTable);
                        tableService.save(updatedTable);

                        listOfUpdatedTables.add(updatedTable);
                    }

            }

            else{
                tableService.save(table);
                listOfUpdatedTables.add(table);

            }

        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",listOfUpdatedTables.toArray()),HttpStatus.OK);

    }


    @DeleteMapping("/dinedynamo/restaurant/table/deletealltables")
    public ResponseEntity<ApiResponse> deleteAllTables(@RequestBody Restaurant restaurant) throws IOException {

        restaurant = restaurantRepository.findById(restaurant.getRestaurantId()).get();


        List<Table> listOfTable = tableRepository.findByRestaurantId(restaurant.getRestaurantId());

        for(Table table: listOfTable){
            tableService.delete(table);
        }

        return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK,"success",restaurant),HttpStatus.OK);


    }




    //If this method returns null, this means that the image does not exist in cloudinary
    @GetMapping("/dinedynamo/restaurant/table/{publicId}")
    public ResponseEntity<ApiResponse> getImageURLFromPublicID(@PathVariable String publicId) throws Exception {
        String imageURL = cloudinaryService.getImageURLFromPublicId(publicId);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",imageURL),HttpStatus.OK);
    }



    @PostMapping("/dinedynamo/restaurant/table/findbytableid")
    public ResponseEntity<ApiResponse> findTableDetailsByTableId(@RequestBody Table table){

        String tableId = table.getTableId();

        if(tableId.equals("") || tableId.equals(" ") || tableId == null){
            System.out.println("TABLE-ID IS empty");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);
        }

        table =  tableRepository.findById(table.getTableId()).orElse(null);

        if(table == null){
            System.out.println("TABLE-ID IS INCORRECT, THE TABLE WITH THIS ID DOES NOT EXIST IN THE DATABASE");
            //return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",null),HttpStatus.OK);

            throw new RuntimeException("Table with this id does not exist in the database");
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",table),HttpStatus.OK);


    }

}
