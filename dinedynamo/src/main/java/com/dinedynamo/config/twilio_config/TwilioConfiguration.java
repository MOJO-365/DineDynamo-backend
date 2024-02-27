package com.dinedynamo.config.twilio_config;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfiguration {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioConfiguration(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Bean
    public TwilioRestClient twilioRestClient() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        return Twilio.getRestClient();
    }
}


//@Autowired
//private TwilioConfig twilioConfig;
//
//@PostConstruct
//public void setup() {
//    Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
//}
