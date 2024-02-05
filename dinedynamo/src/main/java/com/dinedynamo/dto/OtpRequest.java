package com.dinedynamo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OtpRequest {

    private String username;
    private String phoneNumber;
}
