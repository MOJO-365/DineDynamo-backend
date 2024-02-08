package com.dinedynamo.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetPreviousReservationDTO
{
    String customerPhone;
    String restaurantId;
}
