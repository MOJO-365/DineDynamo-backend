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

    @PostMapping("dinedynamo/resend-otp")
    public OtpResponse resend(@RequestBody OtpRequest otpRequest) {
        return smsService.resendOTP(otpRequest);
    }

    @PostMapping("dinedynamo/validate-otp")
    public ResponseEntity<ApiResponse> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        String validateOtpResponse = smsService.validateOtp(otpValidationRequest);

        ApiResponse apiResponse;

        if ("OTP is valid".equals(validateOtpResponse)) {
            apiResponse = new ApiResponse(HttpStatus.OK, "success", "OTP is valid!");
        } else if ("OTP has expired".equals(validateOtpResponse)) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "failure", "OTP has expired");
        } else {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "failure", "Invalid OTP");
        }

        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }


}