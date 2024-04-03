package com.dinedynamo.controllers.discounts_offers_controllers;

import com.dinedynamo.collections.discounts_offers.PercentageDiscount;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.discount_offers_dtos.EditPercentageDiscountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PercentageDiscountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePercentageOffer() throws Exception {
        PercentageDiscount percentageDiscount = new PercentageDiscount();
        percentageDiscount.setPercentage("10");
        percentageDiscount.setRestaurantId("r123");

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/create-percentage-discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(percentageDiscount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.percentage").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurantId").value("r123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeletePercentageOffer() throws Exception {
        PercentageDiscount percentageDiscount = new PercentageDiscount();
        percentageDiscount.setDiscountOfferId("id123");

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/offers/delete-percentage-discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(percentageDiscount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testEditPercentageOffer() throws Exception {
        EditPercentageDiscountDTO editPercentageDiscountDTO = new EditPercentageDiscountDTO();
        editPercentageDiscountDTO.setDiscountOfferId("id123");
        PercentageDiscount percentageDiscount = new PercentageDiscount();
        percentageDiscount.setPercentage("20");
        editPercentageDiscountDTO.setPercentageDiscount(percentageDiscount);

        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/offers/edit-percentage-discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editPercentageDiscountDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.percentage").value("20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").value("id123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetPercentageOfferById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/get-percentage-discount-by-id")
                        .param("discountOfferId", "id123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").value("id123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetAllPercentageOffers() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("r123");

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/get-all-percentage-discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }
}
