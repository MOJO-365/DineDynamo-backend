package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.BugQuery;
import com.dinedynamo.services.RaiseBugQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class RaiseBugQuery
{

    @Autowired
    RaiseBugQueryService raiseBugQueryService;

        @PostMapping("/dinedynamo/restaurant/query/raise-query")
    ResponseEntity<ApiResponse> raiseBugQuery(@RequestBody BugQuery bugQuery){

        boolean isQueryRaised = raiseBugQueryService.createAndSendQuery(bugQuery);

        if(!isQueryRaised){
            System.out.println("FAILED TO SEND THE QUERY");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",bugQuery),HttpStatus.OK);
    }
}
