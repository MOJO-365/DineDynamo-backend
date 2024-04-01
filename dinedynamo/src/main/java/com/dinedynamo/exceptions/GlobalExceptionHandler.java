package com.dinedynamo.exceptions;

import com.dinedynamo.api.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.ParseException;
import java.util.NoSuchElementException;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException iae)
    {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"success");

        //log the type of exception here
        System.out.println("ILLEGAL ARGUMENT EXCEPTION OCCURRED: "+iae.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ApiResponse> handleNoSuchElementException(NoSuchElementException nsee)
    {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK,"success");

        System.out.println("NO SUCH ELEMENT EXCEPTION: "+nsee.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(value = ParseException.class)
    public ResponseEntity<ApiResponse> handleJsonParseException(ParseException e)
    {

        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"success");

        System.out.println("JSON PARSE OCCURRED: "+e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException e)
    {

        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"success", "BAD_CREDENTIALS");

        System.out.println("BadCredentialsException Occurred: "+e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException e)
    {

        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"success","TOKEN_EXPIRED");

        System.out.println("EXCEPTION: JWT TOKEN EXPIRED: "+e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @ExceptionHandler(value = MalformedJwtException.class)
    public ResponseEntity<ApiResponse> handleMalformedJwtException(MalformedJwtException e)
    {

        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"success", "MALFORMED_TOKEN");

        System.out.println("EXCEPTION: MALFORMED JWT TOKEN: "+e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e)
    {

        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"success");

        System.out.println("RUNTIME EXCEPTION OCCURRED: "+e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleOtherException(Exception e)
    {

        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"success");

        System.out.println("EXCEPTION OCCURRED: "+e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }




}