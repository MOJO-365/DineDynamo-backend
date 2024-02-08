package com.dinedynamo.dto;

import com.dinedynamo.collections.MergeCaseReservationRequest;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MergeCaseReservationRequestBody
{

    MergeCaseReservationRequest mergeCaseReservationRequest;
    String messageContent;
}
