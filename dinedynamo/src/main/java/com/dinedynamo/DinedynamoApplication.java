package com.dinedynamo;


import com.mongodb.internal.connection.Time;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;


@SpringBootApplication

public class DinedynamoApplication
{

	public static void main(String[] args) {
		SpringApplication.run(DinedynamoApplication.class, args);
		System.out.println("Hello, this application is up and running");


		System.out.println(LocalDateTime.now());

		System.out.println(LocalTime.now());

		System.out.println(new Date());


	}




}
