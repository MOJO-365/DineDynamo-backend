package com.dinedynamo.controllers.restaurant_controllers;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.restaurant_dtos.EditRestaurantDTO;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.restaurant_services.AppUserService;
import com.dinedynamo.services.restaurant_services.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private AppUserService appUserService;

    @Test
    public void testFindRestaurantsByCity() throws Exception {
        // Create a Restaurant object with a city
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantCity("Test_City");

        // Create a list of restaurants
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(restaurant);

        // Mock the behavior of the service to return the list of restaurants
        when(restaurantRepository.findByRestaurantCityRegexIgnoreCase(restaurant.getRestaurantCity())).thenReturn(restaurants);

        // Perform a POST request to the controller endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/customer/findrestaurantsbycity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // Verify the expected response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].restaurantCity").value("Test_City"));
    }

    @Test
    public void testFindRestaurantById() throws Exception {
        // Create a Restaurant object with an ID
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("TestID");


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(restaurant);

        // Mock the behavior of the repository to return the restaurant
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.of(restaurant));

        // Perform a POST request to the controller endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/findrestaurantbyid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // Verify the expected response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurantId").value("TestID"));
    }

    @Test
    public void testEditRestaurant() throws Exception {
        // Create an EditRestaurantDTO object
        EditRestaurantDTO editRestaurantDTO = new EditRestaurantDTO();
        editRestaurantDTO.setRestaurantId("TestID");

        // Create a Restaurant object with an ID
        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setRestaurantId("TestID");
        updatedRestaurant.setRestaurantName("New name");

        editRestaurantDTO.setRestaurant(updatedRestaurant);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(editRestaurantDTO);
        // Mock the behavior of the repository to return the restaurant
        when(restaurantRepository.findById(editRestaurantDTO.getRestaurantId())).thenReturn(Optional.of(updatedRestaurant));

        // Perform a PUT request to the controller endpoint
        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/editrestaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // Verify the expected response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurantName").value("New name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurantId").value("TestID"));
    }

    @Test
    public void testGetAllRestaurants() throws Exception {
        // Create a list of restaurants
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant());
        restaurants.add(new Restaurant());

        // Create a Page object containing the list of restaurants
        Pageable pageable = PageRequest.of(0, 3);
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurants, pageable, restaurants.size());

        // Mock the behavior of the service to return the Page object
        when(restaurantService.getAllRestaurants(0, 3)).thenReturn(restaurantPage);

        // Perform a GET request to the controller endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/dinedynamo/restaurant/getall"))
                // Verify the expected response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2));
    }
}
