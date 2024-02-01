package com.dinedynamo;


import com.dinedynamo.repositories.TableRepository;
import com.mongodb.internal.connection.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;


@SpringBootApplication

public class DinedynamoApplication
{

	public static void main(String[] args) {
		SpringApplication.run(DinedynamoApplication.class, args);
		System.out.println("Hello, this application is up and running");

//		System.out.println(LocalTime.now());
//
//		System.out.println(new Date());

		LocalDateTime now = LocalDateTime.now();
		String formattedTime = now.format(DateTimeFormatter.ISO_DATE_TIME);
		System.out.println(formattedTime);

		//return formattedTime;
		Date d = new Date();
		System.out.println(LocalDateTime.now().getHour());
		System.out.println(LocalDateTime.now().getMinute());

		System.out.println(LocalDateTime.now());

	}


}
