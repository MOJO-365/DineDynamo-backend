package com.dinedynamo.collections;


import com.dinedynamo.dto.ReservationRequestStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("merge_reservation_requests")
@Builder
public class MergeCaseReservationRequest {

    @Id
    String requestId;

    String customerName;

    String customerPhone;

    String reservationDateAndTime;

    String restaurantId;

    int guestCount;

    ReservationRequestStatus reservationRequestStatus;


}
