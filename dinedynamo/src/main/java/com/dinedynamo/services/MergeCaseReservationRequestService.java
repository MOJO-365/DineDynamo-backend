package com.dinedynamo.services;


import com.dinedynamo.collections.MergeCaseReservationRequest;
import com.dinedynamo.collections.Reservation;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.WaitingList;
import com.dinedynamo.dto.ReservationRequestStatus;
import com.dinedynamo.repositories.MergeCaseReservationRequestRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MergeCaseReservationRequestService
{
    @Autowired
    MergeCaseReservationRequestRepository mergeCaseReservationRequestRepository;

    @Autowired
    WaitingListService waitingListService;

    @Autowired
    RestaurantRepository restaurantRepository;



    public boolean isRequestValid(MergeCaseReservationRequest mergeCaseReservationRequest){


        if(mergeCaseReservationRequest.getCustomerName() == null || mergeCaseReservationRequest.getCustomerName() == " " ||
                mergeCaseReservationRequest.getReservationDateAndTime() == null || mergeCaseReservationRequest.getReservationDateAndTime() == " " ||
                mergeCaseReservationRequest.getCustomerPhone() == null || mergeCaseReservationRequest.getCustomerPhone() == " " ||
                mergeCaseReservationRequest.getGuestCount() == 0

        ){
            return false;
        }

        return true;
    }

    public MergeCaseReservationRequest save(MergeCaseReservationRequest mergeCaseReservationRequest){

        Restaurant restaurant = restaurantRepository.findById(mergeCaseReservationRequest.getRestaurantId()).orElse(null);
        if(restaurant == null){
            System.out.println("RESTAURANT-ID NOT IN DB");
            throw new RuntimeException("RESTAURANT-ID NOT FOUND IN DB");
        }


        mergeCaseReservationRequest.setReservationRequestStatus(ReservationRequestStatus.HOLD);

        mergeCaseReservationRequestRepository.save(mergeCaseReservationRequest);
        System.out.println("MERGE CASE REQUEST SAVED IN REQUEST COLLECTION");
        return mergeCaseReservationRequest;

    }


    public WaitingList acceptMergeCaseRequest(MergeCaseReservationRequest mergeCaseReservationRequest){

        Restaurant restaurant = restaurantRepository.findById(mergeCaseReservationRequest.getRestaurantId()).orElse(null);
        if(restaurant == null){
            System.out.println("RESTAURANT-ID NOT IN DB");
            throw new RuntimeException("RESTAURANT-ID NOT FOUND IN DB");
        }


        WaitingList waitingList = new WaitingList();

        waitingList.setCustomerName(mergeCaseReservationRequest.getCustomerName());
        waitingList.setCustomerPhone(mergeCaseReservationRequest.getCustomerPhone());
        waitingList.setGuestCount(mergeCaseReservationRequest.getGuestCount());
        waitingList.setRestaurantId(mergeCaseReservationRequest.getRestaurantId());
        waitingList.setReservationDateAndTime(mergeCaseReservationRequest.getReservationDateAndTime());

        waitingListService.save(waitingList);

        mergeCaseReservationRequestRepository.delete(mergeCaseReservationRequest);

        System.out.println("ADDED TO WAITING & REMOVED FROM REQUEST");

        return waitingList;


    }

    public MergeCaseReservationRequest rejectMergeCaseRequest(MergeCaseReservationRequest mergeCaseReservationRequest){


        Restaurant restaurant = restaurantRepository.findById(mergeCaseReservationRequest.getRestaurantId()).orElse(null);
        if(restaurant == null){
            System.out.println("RESTAURANT-ID NOT IN DB");
            throw new RuntimeException("RESTAURANT-ID NOT FOUND IN DB");
        }


        mergeCaseReservationRequestRepository.delete(mergeCaseReservationRequest);
        System.out.println("REQUEST REJECTED AND DELETED FROM REQUEST COLLECTION");
        return mergeCaseReservationRequest;

    }

}
