package com.dinedynamo.dto.otp_dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpRequest {

    private String phoneNumber;
}
