package com.dinedynamo.controllers.admin_controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.subscriptions_collections.RestaurantSubscription;
import com.dinedynamo.collections.subscriptions_collections.SubscriptionPlan;
import com.dinedynamo.dto.subscription_dtos.EditSubscriptionPlanDTO;
import com.dinedynamo.dto.subscription_dtos.ViewRestaurantSubscriptionDTO;
import com.dinedynamo.repositories.subscription_repositories.SubscriptionPlanRepository;
import com.dinedynamo.services.subscription_services.AdminSubscriptionControlService;
import com.dinedynamo.services.subscription_services.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class AdminSubscriptionControlController
{
    @Autowired
    SubscriptionPlanService subscriptionPlanService;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    AdminSubscriptionControlService adminSubscriptionControlService;




    @PostMapping("/dinedynamo/admin/subscriptions/create-subscription-plan")
    public ResponseEntity<ApiResponse> createSubscriptionPlan(@RequestBody SubscriptionPlan subscriptionPlan){


        subscriptionPlan = subscriptionPlanService.save(subscriptionPlan);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",subscriptionPlan),HttpStatus.OK);
    }


    @DeleteMapping("/dinedynamo/admin/subscriptions/delete-subscription-plan")
    public ResponseEntity<ApiResponse> deleteSubscriptionPlan(@RequestBody SubscriptionPlan subscriptionPlan){

        subscriptionPlanRepository.delete(subscriptionPlan);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",subscriptionPlan),HttpStatus.OK);

    }


    @PutMapping("/dinedynamo/admin/subscriptions/edit-subscription-plan")
    public ResponseEntity<ApiResponse> editSubscriptionPlan(@RequestBody EditSubscriptionPlanDTO editSubscriptionPlanDTO){

        SubscriptionPlan edittedSubscriptionPlan = subscriptionPlanService.edit(editSubscriptionPlanDTO.getSubscriptionPlanId(), editSubscriptionPlanDTO.getSubscriptionPlan());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",edittedSubscriptionPlan),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/admin/subscriptions/view-all-subscription-plans")
    public ResponseEntity<ApiResponse> viewAllSubscriptionPlans(){

        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",subscriptionPlanList),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/admin/subscriptions/view-all-restaurant-subscriptions")
    public ResponseEntity<ApiResponse> viewAllRestaurantSubscriptions(){

        List<ViewRestaurantSubscriptionDTO> allRestaurantSubscriptionList = adminSubscriptionControlService.viewAllRestaurantSubscriptions();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",allRestaurantSubscriptionList),HttpStatus.OK);
    }


    @PostMapping("/dinedynamo/admin/subscriptions/view-all-active-subscriptions")
    public ResponseEntity<ApiResponse> viewAllActiveRestaurantSubscriptions(){

        List<ViewRestaurantSubscriptionDTO> allActiveRestaurantSubscriptionList = adminSubscriptionControlService.viewAllActiveRestaurantSubscriptions();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",allActiveRestaurantSubscriptionList),HttpStatus.OK);
    }

    @PostMapping("/dinedynamo/admin/subscriptions/view-all-inactive-subscriptions")
    public ResponseEntity<ApiResponse> viewAllInactiveRestaurantSubscriptions(){

        List<ViewRestaurantSubscriptionDTO> allInactiveRestaurantSubscriptionList = adminSubscriptionControlService.viewAllInactiveRestaurantSubscriptions();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",allInactiveRestaurantSubscriptionList),HttpStatus.OK);
    }
}
