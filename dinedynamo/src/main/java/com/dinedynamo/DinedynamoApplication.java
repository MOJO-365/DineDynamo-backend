package com.dinedynamo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableConfigurationProperties
//@EnableScheduling
public class DinedynamoApplication
{
	public static void main(String[] args) {
		SpringApplication.run(DinedynamoApplication.class, args);
		System.out.println("Hello, this application is up and running");
	}
}
