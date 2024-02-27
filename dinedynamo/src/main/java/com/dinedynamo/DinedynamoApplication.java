package com.dinedynamo;


import com.dinedynamo.config.twilio_config.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.LocalDate;


@SpringBootApplication

@EnableConfigurationProperties
public class DinedynamoApplication
{

	public static void main(String[] args) {
		SpringApplication.run(DinedynamoApplication.class, args);
		System.out.println("Hello, this application is up and running");

		LocalDate localDate = LocalDate.now();
		System.out.println("Local Date: " + localDate);

	}
}
