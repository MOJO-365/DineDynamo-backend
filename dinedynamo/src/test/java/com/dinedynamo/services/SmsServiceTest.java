package com.dinedynamo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.dinedynamo.config.TwilioConfig;
import com.dinedynamo.dto.otp_dtos.OtpRequest;
import com.dinedynamo.dto.otp_dtos.OtpResponse;
import com.dinedynamo.dto.otp_dtos.OtpStatus;
import com.dinedynamo.dto.otp_dtos.OtpValidationRequest;
import com.dinedynamo.services.external_services.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SmsServiceTest {

    @Mock
    private TwilioConfig twilioConfig;

    @InjectMocks
    private SmsService smsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testResendOTP() {

        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setPhoneNumber("+123456789");

        OtpResponse otpResponse = smsService.resendOTP(otpRequest);

        assertEquals(OtpStatus.FAILED, otpResponse.getStatus());
    }

    @Test
    public void testValidateOtp() {
        OtpValidationRequest otpValidationRequest = new OtpValidationRequest("+123456789", "123456");

        String result = smsService.validateOtp(otpValidationRequest);

        assertEquals("OTP has expired!", result);
    }

    @Test
    public void testGenerateOTP() {
        String otp = smsService.generateOTP();

        assertNotNull(otp);
        assertEquals(6, otp.length());
    }


    @Test
    public void testSendSMS() {
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setPhoneNumber("+917046535398");
        when(twilioConfig.getPhoneNumber()).thenReturn("+16592445130");

        OtpResponse otpResponse = smsService.sendSMS(otpRequest);

        assertEquals(OtpStatus.FAILED, otpResponse.getStatus());
        assertNotNull(otpResponse.getMessage());
    }


}
