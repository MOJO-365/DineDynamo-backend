package com.dinedynamo.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateTimeUtility {


    /**
     *
     * @param dateString
     * @return LocalDateTime
     * As we saved the date time from frontend in string format (in the database), this method converts JS Datetime string into Java LocalDateTime object
     */
    public LocalDateTime convertJSLocalStringToLocalDateTime(String dateString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a", Locale.US);

        // Parse the string to LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);


        //System.out.println("Converted LocalDateTime: " + localDateTime);

        return localDateTime;

    }

}
