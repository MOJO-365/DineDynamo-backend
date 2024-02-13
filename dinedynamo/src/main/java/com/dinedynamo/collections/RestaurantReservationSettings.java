package com.dinedynamo.collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("reservation_settings")
public class RestaurantReservationSettings
{

    @Id
    String reservationSettingsId;

    String restaurantId;

    String termsAndConditionsForTableReservation;

    String slotDuration;

    String firstSlotStartTime;

    String firstSlotEndTime;

    String secondSlotStartTime;

    String secondSlotEndTime;



}
