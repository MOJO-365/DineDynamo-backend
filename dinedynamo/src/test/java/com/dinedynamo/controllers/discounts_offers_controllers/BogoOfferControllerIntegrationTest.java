package com.dinedynamo.controllers.discounts_offers_controllers;
import com.dinedynamo.collections.discounts_offers.BogoOffer;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.discount_offers_dtos.EditBogoOfferDTO;
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
class BogoOfferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateBogoOffer() throws Exception {
        BogoOffer bogoOffer = new BogoOffer();
        bogoOffer.setRestaurantId("r123");
        bogoOffer.setBuyQty("1");
        bogoOffer.setGetQty("1");
        bogoOffer.setStartingDate("3/04/2024");
        bogoOffer.setEndingDate("5/4/2024");


        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/create-bogo-offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bogoOffer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurantId").value("r123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteBogoOffer() throws Exception {
        BogoOffer bogoOffer = new BogoOffer();
        bogoOffer.setDiscountOfferId("id123");
        bogoOffer.setRestaurantId("r123");
        bogoOffer.setBuyQty("1");
        bogoOffer.setGetQty("1");
        bogoOffer.setStartingDate("3/04/2024");
        bogoOffer.setEndingDate("5/4/2024");

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/offers/delete-bogo-offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bogoOffer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").value("id123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testEditBogoOffer() throws Exception {
        EditBogoOfferDTO editBogoOfferDTO = new EditBogoOfferDTO();
        BogoOffer bogoOffer = new BogoOffer();
        bogoOffer.setDiscountOfferId("id123");
        bogoOffer.setDiscountOfferId("id123");
        bogoOffer.setRestaurantId("r123");
        bogoOffer.setBuyQty("1");
        bogoOffer.setGetQty("1");
        bogoOffer.setStartingDate("3/04/2024");
        bogoOffer.setEndingDate("5/4/2024");

        editBogoOfferDTO.setBogoOffer(bogoOffer);
        editBogoOfferDTO.setDiscountOfferId("id123");

        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/offers/edit-bogo-offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editBogoOfferDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").value("id123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.buyQty").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.getQty").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.startingDate").value("3/04/2024"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.endingDate").value("5/4/2024"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetBogoOfferById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/get-bogo-offer-by-id")
                        .param("discountOfferId", "id123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").value("id123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetAllBogoOffers() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("r123");
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/get-all-bogo-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant)))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }
}
