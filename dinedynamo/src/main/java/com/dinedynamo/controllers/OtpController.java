package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.dto.OtpRequest;
import com.dinedynamo.dto.OtpResponse;
import com.dinedynamo.dto.OtpValidationRequest;
import com.dinedynamo.services.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin("*")
public class OtpController {

    @Autowired
    private SmsService smsService;

    @GetMapping("dinedynamo/process/otp")
    public String processSMS() {
        return "SMS sent";
    }

    @PostMapping("dinedynamo/send-otp")
    public OtpResponse sendOtp(@RequestBody OtpRequest otpRequest) {
        return smsService.sendSMS(otpRequest);
    }

    @PostMapping("dinedynamo/validate-otp")
    public ResponseEntity<ApiResponse> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        String validateOtp = smsService.validateOtp(otpValidationRequest);

        ApiResponse apiResponse;

        if ("OTP is valid!".equals(validateOtp)) {
            apiResponse = new ApiResponse(HttpStatus.OK, "OTP is valid", null);
        } else {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "OTP is invalid", null);
        }

        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());

    }

}