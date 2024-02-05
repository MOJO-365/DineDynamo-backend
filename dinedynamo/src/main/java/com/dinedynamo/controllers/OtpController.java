package com.dinedynamo.controllers;

import com.dinedynamo.dto.OtpRequest;
import com.dinedynamo.dto.OtpResponse;
import com.dinedynamo.dto.OtpValidationRequest;
import com.dinedynamo.services.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class OtpController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/process")
    public String processSMS() {
        return "SMS sent";
    }

    @PostMapping("dinedynamo/send-otp")
    public OtpResponse sendOtp(@RequestBody OtpRequest otpRequest) {
        return smsService.sendSMS(otpRequest);
    }
    @PostMapping("/validate-otp")
    public String validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        return smsService.validateOtp(otpValidationRequest);
    }

}