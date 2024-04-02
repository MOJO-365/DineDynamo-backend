package com.dinedynamo.controllers.restaurant_controllers;
import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.restaurant_collections.BugQuery;
import com.dinedynamo.controllers.restaurant_controllers.RaiseBugQueryController;
import com.dinedynamo.services.restaurant_services.RaiseBugQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class RaiseBugQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RaiseBugQueryService raiseBugQueryService;

    @Test
    public void testRaiseBugQuery_Success() throws Exception {
        // Create a BugQuery object
        BugQuery bugQuery = new BugQuery();
        bugQuery.setBugDescription("I have a query");
        bugQuery.setBugQueryTitle("My query title is..");
        bugQuery.setRestaurantId("r123");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(bugQuery);

        // Mock the behavior of the service to return true
        when(raiseBugQueryService.createAndSendQuery(bugQuery)).thenReturn(true);

        // Perform a POST request to the controller endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/query/raise-query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // Verify the expected response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.bugQueryTitle").value("My query title is.."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.bugDescription").value("I have a query"));
    }

    @Test
    public void testRaiseBugQuery_Failure() throws Exception {
        // Create a BugQuery object
        BugQuery bugQuery = new BugQuery();



        // Mock the behavior of the service to return false
        when(raiseBugQueryService.createAndSendQuery(bugQuery)).thenReturn(false);

        // Perform a POST request to the controller endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/query/raise-query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                // Verify the expected response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()); // Verify that data is null
    }
}
