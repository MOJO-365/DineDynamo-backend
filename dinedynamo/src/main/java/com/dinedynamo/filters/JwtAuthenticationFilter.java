package com.dinedynamo.filters;

import com.dinedynamo.config.jwt_config.UserDetailsServiceImpl;
import com.dinedynamo.helper.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


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



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        tokenFromRequest = request.getHeader("Authorization");

        if (tokenFromRequest != null && tokenFromRequest.startsWith("Bearer ")) {

            System.out.println("TOKEN FROM REQ: "+tokenFromRequest);
            tokenFromRequest = tokenFromRequest.substring(7);

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

                request.setAttribute("restId",jwtHelper.extractRestaurantId(tokenFromRequest));
            }


        }
        System.out.println("FILTER PASSED");
        filterChain.doFilter(request, response);

    }
}
