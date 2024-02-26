package com.dinedynamo.controllers.authentication_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.dto.otp_dtos.OtpRequest;
import com.dinedynamo.dto.otp_dtos.OtpResponse;
import com.dinedynamo.dto.otp_dtos.OtpValidationRequest;
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
        String validateOtp = smsService.validateOtp(otpValidationRequest);

        ApiResponse apiResponse;

        if ("OTP is valid!".equals(validateOtp)) {
            apiResponse = new ApiResponse(HttpStatus.OK, "success", validateOtp);
        } else {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "failure", validateOtp);
        }

        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());

    }

}