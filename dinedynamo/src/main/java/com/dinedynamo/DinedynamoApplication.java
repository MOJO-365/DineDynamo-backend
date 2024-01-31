package com.dinedynamo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.LocalTime;


@SpringBootApplication

public class DinedynamoApplication
{
	public static void main(String[] args) {
		SpringApplication.run(DinedynamoApplication.class, args);
		System.out.println("Hello, this application is up and running");

		System.out.println(LocalTime.now());
	}


}
