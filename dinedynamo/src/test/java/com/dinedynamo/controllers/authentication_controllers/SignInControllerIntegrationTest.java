package com.dinedynamo.controllers.authentication_controllers;
import com.dinedynamo.config.jwt_config.UserDetailsServiceImpl;
import com.dinedynamo.dto.authentication_dtos.SignInRequestBody;
import com.dinedynamo.dto.authentication_dtos.JwtResponseDTO;
import com.dinedynamo.helper.JwtHelper;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.restaurant_services.AppUserService;
import com.dinedynamo.services.restaurant_services.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignInController.class)
@AutoConfigureMockMvc
public class SignInControllerIntegrationTest {



    @MockBean
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    AppUserService appUserService;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    JwtHelper jwtHelper;

    @MockBean
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void whenValidCredentials_thenReturnsAccessTokenAndRefreshToken() throws Exception {
        // Given
        SignInRequestBody requestBody = new SignInRequestBody("dominos@gmail.com", "d123");
        String requestJson = new ObjectMapper().writeValueAsString(requestBody);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/dinedynamo/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.accessToken").isString())
                .andExpect(jsonPath("$.data.refreshToken").isString());
    }

    @Test
    public void whenInvalidCredentials_thenReturnsNotFound() throws Exception {
        // Given
        SignInRequestBody requestBody = new SignInRequestBody("invalid@example.com", "invalidPassword");
        String requestJson = new ObjectMapper().writeValueAsString(requestBody);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/dinedynamo/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        System.out.println("RESULT ACTIONS:"+" "+resultActions);

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
