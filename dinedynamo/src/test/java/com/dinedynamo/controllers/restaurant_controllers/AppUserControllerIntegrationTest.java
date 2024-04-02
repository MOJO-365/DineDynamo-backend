package com.dinedynamo.controllers.restaurant_controllers;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.restaurant_dtos.EditAppUserDTO;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.services.restaurant_services.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class AppUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private AppUserService appUserService;

    @Test
    public void testCreateAppUser() throws Exception {


        AppUser appUser = new AppUser();
        appUser.setUserEmail("a123@gmail.com");
        appUser.setUserPassword("a123");
        appUser.setRestaurantId("r123");
        appUser.setUserType("RESTAURANT");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(appUser);

        when(appUserService.save(appUser)).thenReturn(appUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/users/create-app-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));

    }

    @Test
    public void testDeleteAppUser() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUserId("u123");
        appUser.setUserPassword("a123");
        appUser.setRestaurantId("r123");
        appUser.setUserType("RESTAURANT");
        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", appUser), HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(appUser);

        when(appUserRepository.findById(appUser.getUserId())).thenReturn(java.util.Optional.of(appUser));

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/users/delete-app-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    public void testEditAppUser() throws Exception {
        EditAppUserDTO editAppUserDTO = new EditAppUserDTO();
        AppUser appUser = new AppUser();
        appUser.setUserId("u123");
        appUser.setUserPassword("a123");
        appUser.setRestaurantId("r123");
        appUser.setUserType("RESTAURANT");
        editAppUserDTO.setAppUser(appUser);
        editAppUserDTO.setUserId("u123");


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(appUser);
        when(appUserService.editAppuser(editAppUserDTO.getUserId(), editAppUserDTO.getAppUser())).thenReturn(appUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/users/edit-app-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    public void testGetAllAppUsersForRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("r123");

        AppUser a1 = new AppUser();
        a1.setRestaurantId("r123");
        a1.setUserId("u123");
        a1.setUserPassword("a123");
        a1.setUserType("WAITER");

        AppUser a2 = new AppUser();
        a2.setRestaurantId("r123");
        a2.setUserId("u123");
        a2.setUserPassword("a123");
        a2.setUserType("KITCHEN");
        List<AppUser> appUserList = new ArrayList<>();
        appUserList.add(a1);
        appUserList.add(a2);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(restaurant);
        when(appUserService.findAllUsersByRestaurantId(restaurant)).thenReturn(appUserList);
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/users/get-all-app-users-for-restaurant")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    public void testDeleteAllAppUsersForRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("r123");

        AppUser a1 = new AppUser();
        a1.setRestaurantId("r123");
        a1.setUserId("u123");
        a1.setUserPassword("a123");
        a1.setUserType("WAITER");

        AppUser a2 = new AppUser();
        a2.setRestaurantId("r123");
        a2.setUserId("u123");
        a2.setUserPassword("a123");
        a2.setUserType("KITCHEN");
        List<AppUser> appUserList = new ArrayList<>();
        appUserList.add(a1);
        appUserList.add(a2);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(restaurant);

        when(appUserService.deleteAllUsersByRestaurantId(restaurant)).thenReturn(appUserList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/users/delete-all-app-users-for-restaurant")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }
}
