package com.dinedynamo.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckExistingInReservationOrWaitingRequest
{

    String customerPhone;

    String restaurantId;
}
