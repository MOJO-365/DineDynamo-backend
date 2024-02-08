package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.MergeCaseReservationRequest;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.WaitingList;
import com.dinedynamo.dto.MergeCaseReservationRequestBody;
import com.dinedynamo.repositories.MergeCaseReservationRequestRepository;
import com.dinedynamo.services.MergeCaseReservationRequestService;
import com.dinedynamo.services.SmsService;
import com.dinedynamo.services.TableReservationService;
import com.dinedynamo.services.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class MergeCaseReservationController
{

    @Autowired
    WaitingListService waitingListService;

    @Autowired
    MergeCaseReservationRequestService mergeCaseReservationRequestService;

    @Autowired
    MergeCaseReservationRequestRepository mergeCaseReservationRequestRepository;

    @Autowired
    SmsService smsService;

    @PostMapping("/dinedynamo/restaurant/reservations/save-mergecase-reservation-request")
    ResponseEntity<ApiResponse> saveMergeCaseReservationRequest(@RequestBody MergeCaseReservationRequest mergeCaseReservationRequest){


        boolean isRequestValid = mergeCaseReservationRequestService.isRequestValid(mergeCaseReservationRequest);

        if(!isRequestValid){
            System.out.println("REQUEST DOES NOT CONTAIN REQUIRED FIELDS");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);

        }

        mergeCaseReservationRequest = mergeCaseReservationRequestService.save(mergeCaseReservationRequest);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",mergeCaseReservationRequest),HttpStatus.OK);

    }

    @PostMapping("/dinedynamo/restaurant/reservations/accept-mergecase-reservation-request")
    ResponseEntity<ApiResponse> acceptMergeCaseReservationRequest(@RequestBody MergeCaseReservationRequestBody mergeCaseReservationRequestBody){


        MergeCaseReservationRequest mergeCaseReservationRequest = mergeCaseReservationRequestBody.getMergeCaseReservationRequest();
        boolean isRequestValid = mergeCaseReservationRequestService.isRequestValid(mergeCaseReservationRequest);

        System.out.println("-->"+mergeCaseReservationRequest);
        if(!isRequestValid){
            System.out.println("REQUEST DOES NOT CONTAIN REQUIRED FIELDS");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);

        }

        mergeCaseReservationRequest = mergeCaseReservationRequestRepository.findByCustomerPhoneAndRestaurantId(mergeCaseReservationRequest.getCustomerPhone(), mergeCaseReservationRequest.getRestaurantId()).orElse(null);

        if(mergeCaseReservationRequest == null){
            System.out.println("SUCH REQUEST DOES NOT EXIST IN DB");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);

        }


        WaitingList waitingList = mergeCaseReservationRequestService.acceptMergeCaseRequest(mergeCaseReservationRequest);

        smsService.sendMessageForMergeCaseReservationRequest(mergeCaseReservationRequest.getCustomerPhone(),mergeCaseReservationRequestBody.getMessageContent());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",waitingList),HttpStatus.OK);


    }


    @PostMapping("/dinedynamo/restaurant/reservations/reject-mergecase-reservation-request")
    ResponseEntity<ApiResponse> rejectMergeCaseReservationRequest(@RequestBody MergeCaseReservationRequestBody mergeCaseReservationRequestBody){

        MergeCaseReservationRequest mergeCaseReservationRequest = mergeCaseReservationRequestBody.getMergeCaseReservationRequest();

        boolean isRequestValid = mergeCaseReservationRequestService.isRequestValid(mergeCaseReservationRequest);

        System.out.println(mergeCaseReservationRequest);
        if(!isRequestValid){
            System.out.println("REQUEST DOES NOT CONTAIN REQUIRED FIELDS");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"success",null),HttpStatus.OK);
        }

        mergeCaseReservationRequest = mergeCaseReservationRequestRepository.findByCustomerPhoneAndRestaurantId(mergeCaseReservationRequest.getCustomerPhone(), mergeCaseReservationRequest.getRestaurantId()).orElse(null);

        if(mergeCaseReservationRequest == null){
            System.out.println("SUCH REQUEST DOES NOT EXIST IN DB");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND,"success",null),HttpStatus.OK);

        }

        mergeCaseReservationRequest = mergeCaseReservationRequestService.rejectMergeCaseRequest(mergeCaseReservationRequest);

        smsService.sendMessageForMergeCaseReservationRequest(mergeCaseReservationRequest.getCustomerPhone(),mergeCaseReservationRequestBody.getMessageContent());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",mergeCaseReservationRequest),HttpStatus.OK);

    }

}
