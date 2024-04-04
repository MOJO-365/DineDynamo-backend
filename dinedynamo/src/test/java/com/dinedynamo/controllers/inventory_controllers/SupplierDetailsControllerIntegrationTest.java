package com.dinedynamo.controllers.inventory_controllers;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.dto.inventory_dtos.EditSupplierDetailsDTO;
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
class SupplierDetailsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddMultipleItemsSuppliers() throws Exception {
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierPhone("9898989898");
        supplierDetails.setSupplierName("John Doe");
        String[] rawMaterialIdList = new String[2];
        rawMaterialIdList[0] = "r123";
        rawMaterialIdList[1] = "r456";

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/add-supplier-for-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteSupplier() throws Exception {
        SupplierDetails supplierDetails = new SupplierDetails();
        // Set up supplierDetails properties

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/inventory/suppliers/delete-supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testEditSupplier() throws Exception {
        EditSupplierDetailsDTO editSupplierDetailsDTO = new EditSupplierDetailsDTO();
        // Set up editSupplierDetailsDTO properties

        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/inventory/suppliers/edit-supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editSupplierDetailsDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetSuppliersForRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/get-suppliers-for-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": \"yourRestaurantId\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetItemsBySupplier() throws Exception {
        SupplierDetails supplierDetails = new SupplierDetails();
        // Set up supplierDetails properties

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/get-items-by-supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetSupplierByRawMaterial() throws Exception {
        RawMaterial rawMaterial = new RawMaterial();
        // Set up rawMaterial properties

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/get-supplier-for-raw-material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rawMaterial)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteSuppliersForRawMaterial() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/inventory/suppliers/delete-all-suppliers-for-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": \"yourRestaurantId\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testSearchSupplierByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/search-by-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("restaurantId", "yourRestaurantId")
                        .param("supplierName", "yourSupplierName"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }
}
