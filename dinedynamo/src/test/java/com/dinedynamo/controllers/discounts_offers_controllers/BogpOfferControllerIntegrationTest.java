package com.dinedynamo.controllers.discounts_offers_controllers;
import com.dinedynamo.collections.discounts_offers.BogpOffer;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.discount_offers_dtos.EditBogpOfferDTO;
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
class BogpOfferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateBogpOffer() throws Exception {
        BogpOffer bogpOffer = new BogpOffer();
        bogpOffer.setMinQty("2");
        bogpOffer.setMaxValue("2500");
        bogpOffer.setRestaurantId("r123");
        bogpOffer.setStartingDate("03/04/2024");
        bogpOffer.setEndingDate("04/05/2024");

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/create-bogp-offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bogpOffer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurantId").value("r123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.endingDate").value("04/05/2024"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.startingDate").value("03/04/2024"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteBogpOffer() throws Exception {
        BogpOffer bogpOffer = new BogpOffer();
        bogpOffer.setDiscountOfferId("id123");

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/offers/delete-bogp-offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bogpOffer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testEditBogpOffer() throws Exception {
        EditBogpOfferDTO editBogpOfferDTO = new EditBogpOfferDTO();
        editBogpOfferDTO.setDiscountOfferId("id123");
        BogpOffer bogpOffer = new BogpOffer();
        bogpOffer.setMinQty("5");
        editBogpOfferDTO.setBogpOffer(bogpOffer);

        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/offers/edit-bogp-offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editBogpOfferDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetBogpOfferById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/get-bogp-offer-by-id")
                        .param("discountOfferId", "id123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.discountOfferId").value("id123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetAllBogpOffers() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("r123");

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/offers/get-all-bogp-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }
}
