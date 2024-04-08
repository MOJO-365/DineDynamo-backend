package com.dinedynamo.config.jwt_config;

import com.dinedynamo.filters.JwtAuthenticationFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity

public class SecurityConfig
{

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private static final String[] AUTH_WHITELIST = {

//            "/dinedynamo/**",
            "/dinedynamo/customer/**",
            "/dinedynamo/auth/signin",
            "/dinedynamo/signin",
            "/dinedynamo/admin/**",
            "/dinedynamo/signuprestaurant",
            "/dinedynamo/signupcustomer",
            "/dinedynamo/customer/favourites/**",
            "/dinedynamo/refreshtoken",
            "/dinedynamo/restaurant/getall",
            "/dinedynamo/customer/findrestaurantsbycity",
            "/dinedynamo/restaurant/findrestaurantbyid",
            "/dinedynamo/restaurant/subscriptions/take-or-renew-subscription",
            "/dinedynamo/restaurant/table/findbytableid",
            "/dinedynamo/restaurant/table/get-groupby-tables",
            "/dinedynamo/restaurant/menu/get-menu",
            "/dinedynamo/restaurant/menu/get-all-items",
            "dinedynamo/create-payment-intent", //for payment gateway
            "/dinedynamo/customer/filters/**",
            "/dinedynamo/customer/reservations/**",
            "/dinedynamo/restaurant/reservations/get-reservation-settings",
            "/dinedynamo/restaurant/offers/get-bogo-offer-by-id",
            "/dinedynamo/restaurant/offers/get-all-bogo-offers",
            "/dinedynamo/restaurant/offers/get-bogp-offer-by-id",
            "/dinedynamo/restaurant/offers/get-all-bogp-offers",
            "/dinedynamo/restaurant/offers/get-percentage-discount-by-id",
            "/dinedynamo/restaurant/offers/get-all-percentage-discounts",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/public/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**"
    };

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        return new UserDetailsServiceImpl();
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {



        return http
                .authenticationProvider(authenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req->req.requestMatchers(AUTH_WHITELIST).permitAll()
                                //.requestMatchers("/dinedynamo/restaurant/**").hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                ).userDetailsService(userDetailsServiceImpl)
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        e->e.accessDeniedHandler(
                                        (request, response, accessDeniedException)->response.setStatus(403)
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();


//        return http.csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .requestMatchers(AUTH_WHITELIST).permitAll()
//                .anyRequest()
//                .authenticated()
//
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
//                .and()
//                .addFilterBefore((Filter) this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}

