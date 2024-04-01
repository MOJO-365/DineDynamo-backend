package com.dinedynamo.filters;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.config.jwt_config.UserDetailsServiceImpl;
import com.dinedynamo.exceptions.GlobalExceptionHandler;
import com.dinedynamo.helper.EncryptionDecryptionUtil;
import com.dinedynamo.helper.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    JwtHelper jwtHelper;

    String tokenFromRequest=null;

    String userEmail=null;
    String userRole=null;

    @Autowired
    EncryptionDecryptionUtil encryptionDecryptionUtil;

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            tokenFromRequest = request.getHeader("Authorization");

            if (tokenFromRequest != null && tokenFromRequest.startsWith("Bearer ")) {

                System.out.println("TOKEN FROM REQ: "+tokenFromRequest);

                //Decrypting token here
                tokenFromRequest = encryptionDecryptionUtil.decrypt(tokenFromRequest.substring(7));

                userEmail = jwtHelper.getUsernameFromToken(tokenFromRequest);
                userRole = jwtHelper.extractUserRole(tokenFromRequest);

                System.out.println("In filter-UserEmail from token is: "+userEmail);
                System.out.println("In filter-UserRole from token is: "+userRole);
            }

            if (userEmail != null && userRole != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                System.out.println("USER ROLE IS: "+userRole);
                System.out.println("USER EMAIL IS: "+userEmail);

                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userEmail);
                System.out.println("USER DETAILS: "+userDetails);

                if (tokenFromRequest!= null && jwtHelper.validateToken(tokenFromRequest, userDetails)) {

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);


                }


            }
            System.out.println("FILTER PASSED");
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException expiredJwtException){

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponse.setData("TOKEN_EXPIRED");
            apiResponse.setMessage("success");

            // Set response status code and content type

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Write JSON response to output stream
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), apiResponse);
            return;

        }
        catch (MalformedJwtException malformedJwtException){

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponse.setData("TOKEN_MALFORMED");
            apiResponse.setMessage("success");

            // Set response status code and content type

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Write JSON response to output stream
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), apiResponse);
            return;

        }
        catch (Exception exception){

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponse.setData("EXCEPTION");
            apiResponse.setMessage("success");

            // Set response status code and content type

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Write JSON response to output stream
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), apiResponse);

            System.out.println("EXCEPTION OCCURRED: "+ exception.getMessage());
            return;

        }


    }
}
