package com.dinedynamo.dto.table_dtos;


import com.dinedynamo.collections.reservation_collections.Reservation;
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
