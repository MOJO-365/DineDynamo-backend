package com.dinedynamo.controllers.customer_controllers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddToFavourites() throws Exception {

        String customerPhone = "1234567890";
        List<String> listOfRestaurantIds = new ArrayList<>();
        listOfRestaurantIds.add("r123");
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/customer/favourites/add-to-favs")
                        .param("customerPhone", "1234567890")
                        .param("restaurantId", "r123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.customerPhone").value(customerPhone))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.listOfRestaurantIds[0]").value("r123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetAllFavourites() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/customer/favourites/get-all-favs")
                        .param("customerPhone", "1234567890")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteFromFavourites() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/customer/favourites/delete-from-favs")
                        .param("customerPhone", "1234567890")
                        .param("restaurantId", "r123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }
}
