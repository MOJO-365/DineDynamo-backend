package com.dinedynamo.dto;


import com.dinedynamo.collections.Reservation;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationRequestDTO
{

    String messageContent;

    Reservation reservation;
}
