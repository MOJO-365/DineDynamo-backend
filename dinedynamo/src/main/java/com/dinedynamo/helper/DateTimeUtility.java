package com.dinedynamo.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateTimeUtility {



    public LocalDateTime convertJSLocalStringToLocalDateTime(String dateString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a", Locale.US);

        // Parse the string to LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);


        //System.out.println("Converted LocalDateTime: " + localDateTime);

        return localDateTime;

    }

}
