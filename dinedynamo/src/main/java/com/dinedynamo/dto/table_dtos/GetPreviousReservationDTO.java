package com.dinedynamo.dto.table_dtos;


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
