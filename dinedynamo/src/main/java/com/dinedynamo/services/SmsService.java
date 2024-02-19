package com.dinedynamo.services;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.dinedynamo.config.TwilioConfig;
import com.dinedynamo.dto.otp_dtos.OtpRequest;
import com.dinedynamo.dto.otp_dtos.OtpResponse;
import com.dinedynamo.dto.otp_dtos.OtpStatus;
import com.dinedynamo.dto.otp_dtos.OtpValidationRequest;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {

    @Autowired
    private TwilioConfig twilioConfig;
    Map<String, Map<String, Long>> otpMap = new HashMap<>();

    public OtpResponse sendSMS(OtpRequest otpRequest) {
        OtpResponse otpResponseDto = null;
        try {
            String phoneNumber = otpRequest.getPhoneNumber();
            PhoneNumber to = new PhoneNumber(phoneNumber);
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
            String otp = generateOTP();
            String otpMessage = "Dear Customer, Your OTP is " + otp +". Thanks for using our service.";

            Message message = Message
                    .creator(to, from, otpMessage)
                    .create();

            Map<String, Long> otpData = new HashMap<>();
            otpData.put(otp, System.currentTimeMillis());
            otpMap.put(phoneNumber, otpData);

            otpResponseDto = new OtpResponse(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception e) {
            e.printStackTrace();
            otpResponseDto = new OtpResponse(OtpStatus.FAILED, e.getMessage());
        }
        return otpResponseDto;
    }

    public OtpResponse resendOTP(OtpRequest otpRequest) {
        OtpResponse otpResponseDto = null;
        String phoneNumber = otpRequest.getPhoneNumber();

        if (otpMap.containsKey(phoneNumber)) {
            Map<String, Long> otpData = otpMap.get(phoneNumber);
            long currentTime = System.currentTimeMillis();

            if (currentTime - otpData.values().iterator().next() <= 30000) {
                String newOtp = generateOTP();
                otpData.clear();
                otpData.put(newOtp, System.currentTimeMillis());

                PhoneNumber to = new PhoneNumber(phoneNumber);
                PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
                String otpMessage = "Dear Customer, Your new OTP is " + newOtp + ". Thank you.";

                Message message = Message
                        .creator(to, from, otpMessage)
                        .create();

                otpResponseDto = new OtpResponse(OtpStatus.DELIVERED, otpMessage);
            } else {
                otpResponseDto = new OtpResponse(OtpStatus.FAILED, "OTP has expired.");
            }
        } else {
            otpResponseDto = new OtpResponse(OtpStatus.FAILED, "Phone number not found for OTP resend.");
        }
        return otpResponseDto;
    }

    public String validateOtp(OtpValidationRequest otpValidationRequest) {
        String phoneNumber = otpValidationRequest.getPhoneNumber();
        String otpNumber = otpValidationRequest.getOtpNumber();

        if (otpMap.containsKey(phoneNumber)) {
            Map<String, Long> otpData = otpMap.get(phoneNumber);
            long currentTime = System.currentTimeMillis();
            Long storedTime = otpData.values().iterator().next();
            String storedOtp = otpData.keySet().iterator().next();

            if (storedOtp.equals(otpNumber)) {
                if (currentTime - storedTime <= 40000) {
                    otpMap.remove(phoneNumber);
                    return "OTP is valid!";
                } else {
                    return "OTP has expired!";
                }
            } else {
                return "Entered OTP is invalid!";
            }
        }
        return "OTP has expired!";
    }







    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }


    public void sendMessageToCustomer(String customerPhone, String messageContent){
        PhoneNumber to = new PhoneNumber(customerPhone);
        System.out.println(to);
        PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());

        System.out.println(from);

        Message message = Message
                .creator(to, from, messageContent)
                .create();

        System.out.println("MESSAGE SENT TO CUSTOMER");
    }
}
