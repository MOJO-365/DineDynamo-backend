package com.dinedynamo.controllers.inventory_controllers;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.inventory_management.SupplierDetails;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.inventory_dtos.EditSupplierDetailsDTO;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.repositories.inventory_repositories.SupplierDetailsRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.services.inventory_services.SupplierDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SupplierDetailsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SupplierDetailsService supplierDetailsService;

    @MockBean
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    RestaurantRepository restaurantRepository;

    @MockBean
    SupplierDetailsRepository supplierDetailsRepository;

    @Test
    void testAddMultipleItemsSuppliers() throws Exception {
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierPhone("9898989898");
        supplierDetails.setSupplierName("John Doe");
        supplierDetails.setRestaurantId("r123");
        String[] rawMaterialIdList = new String[2];
        rawMaterialIdList[0] = "r123";
        rawMaterialIdList[1] = "r456";
        supplierDetails.setRawMaterialIdList(rawMaterialIdList);

        when(supplierDetailsService.save(supplierDetails)).thenReturn( supplierDetails);
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/add-supplier-for-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.supplierPhone").value("9898989898"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.supplierName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.rawMaterialIdList[0]").value("r123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteSupplier() throws Exception {
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierId("s123");
        supplierDetails.setRestaurantId("r123");

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/inventory/suppliers/delete-supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.supplierId").value("s123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testEditSupplier() throws Exception {
        EditSupplierDetailsDTO editSupplierDetailsDTO = new EditSupplierDetailsDTO();
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierId("s123");
        supplierDetails.setSupplierEmailId("sup@gmail.com");
        supplierDetails.setSupplierPhone("9898989898");
        supplierDetails.setSupplierName("John Doe");
        supplierDetails.setRestaurantId("r123");
        String[] rawMaterialIdList = new String[2];
        rawMaterialIdList[0] = "r123";
        rawMaterialIdList[1] = "r456";
        supplierDetails.setRawMaterialIdList(rawMaterialIdList);
        editSupplierDetailsDTO.setSupplierId("s123");
        editSupplierDetailsDTO.setSupplierDetails(supplierDetails);

        when(supplierDetailsService.save(supplierDetails)).thenReturn(supplierDetails);

        when(supplierDetailsService.updateSupplierDetails(editSupplierDetailsDTO.getSupplierId(),editSupplierDetailsDTO.getSupplierDetails())).thenReturn(supplierDetails);
        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/inventory/suppliers/edit-supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editSupplierDetailsDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.supplierId").value("s123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.supplierName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.rawMaterialIdList[0]").value("r123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetSuppliersForRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("rest123");
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setRestaurantId("rest123");
        supplierDetails.setSupplierId("s123");

        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(supplierDetailsService.save(supplierDetails)).thenReturn(supplierDetails);

        List<SupplierDetails> list = new ArrayList<>();
        list.add(supplierDetails);

        when(supplierDetailsRepository.findByRestaurantId("r123")).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/get-suppliers-for-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": \"r123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].supplierId").value("s123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].restaurantId").value("rest123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetItemsBySupplier() throws Exception {
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierId("s123");
        supplierDetails.setSupplierEmailId("sup@gmail.com");
        supplierDetails.setSupplierPhone("9898989898");
        supplierDetails.setSupplierName("John Doe");
        supplierDetails.setRestaurantId("r123");
        String[] rawMaterialIdList = new String[2];
        rawMaterialIdList[0] = "rw123";
        rawMaterialIdList[1] = "r456";
        supplierDetails.setRawMaterialIdList(rawMaterialIdList);

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRestaurantId("r123");
        rawMaterial.setRawMaterialId("rw123");

        List<RawMaterial> list = new ArrayList<>();
        list.add(rawMaterial);
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        when(supplierDetailsService.save(supplierDetails)).thenReturn(supplierDetails);
        when(supplierDetailsService.getItemsBySupplier(supplierDetails)).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/get-items-by-supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].rawMaterialId").value("rw123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testGetSupplierByRawMaterial() throws Exception {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId("raw123");
        rawMaterial.setRestaurantId("r123");
        SupplierDetails supplierDetails = new SupplierDetails();
        supplierDetails.setSupplierId("sup123");
        supplierDetails.setRestaurantId("r123");
        String[] rawMaterialIdList = new String[2];
        rawMaterialIdList[0] = "r123";
        rawMaterialIdList[1] = "r456";
        supplierDetails.setRawMaterialIdList(rawMaterialIdList);


        List<SupplierDetails> list = new ArrayList<>();
        list.add(supplierDetails);
        when(supplierDetailsService.save(supplierDetails)).thenReturn(supplierDetails);
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        when(supplierDetailsService.getSupplierByRawMaterial(rawMaterial)).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/suppliers/get-supplier-for-raw-material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rawMaterial)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].supplierId").value("sup123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

//    @Test
//    void testDeleteSuppliersForRawMaterial() throws Exception {
//
//
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/inventory/suppliers/delete-all-suppliers-for-restaurant")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"restaurantId\": \"r123\"}"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
//    }


}
