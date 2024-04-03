package com.dinedynamo.controllers.inventory_controllers;
import com.dinedynamo.collections.inventory_management.RawMaterial;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.dto.inventory_dtos.AddUsageForRawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.EditRawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialDTO;
import com.dinedynamo.dto.inventory_dtos.RawMaterialStatus;
import com.dinedynamo.repositories.inventory_repositories.RawMaterialRepository;
import com.dinedynamo.services.inventory_services.RawMaterialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RawMaterialControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RawMaterialService rawMaterialService;

    @MockBean
    RawMaterialRepository rawMaterialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddRawMaterial() throws Exception {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId("rw1234");
        rawMaterial.setRestaurantId("r123");
        rawMaterial.setCurrentLevel(15.0);
        rawMaterial.setReorderLevel(2.0);
        rawMaterial.setName("r12");
        rawMaterial.setCategory("c1");
        rawMaterial.setMeasurementUnits("Kg");
        rawMaterial.setCostPerUnit(50.0);
        rawMaterial.setTotalCost(750.0);
        rawMaterial.setPurchaseDate(LocalDate.now());
        rawMaterial.setExpirationDate(LocalDate.now().plusMonths(18));
        rawMaterial.setOperatorName("o1");


        RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
        rawMaterialDTO.setStatus(RawMaterialStatus.VALID_AND_SAVED);
        rawMaterialDTO.setRawMaterial(rawMaterial);

        when(rawMaterialService.save(rawMaterial)).thenReturn(rawMaterialDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/add-raw-material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rawMaterial)))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.rawMaterial.rawMaterialId").isString())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value("VALID_AND_SAVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testFindAllRawMaterialsOfRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/get-all-raw-materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": \"r123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testFindRawMaterialById() throws Exception {

       RawMaterial rawMaterial = new RawMaterial();
       rawMaterial.setRawMaterialId("r123");
       rawMaterialRepository.delete(rawMaterial);
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        mockMvc.perform(MockMvcRequestBuilders.post("/dinedynamo/restaurant/inventory/get-raw-material-by-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("rawMaterialId", "rw123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.rawMaterialId").value("rw123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testEditRawMaterial() throws Exception {
        EditRawMaterialDTO editRawMaterialDTO = new EditRawMaterialDTO();

        editRawMaterialDTO.setRawMaterialId("rw123");
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId("rw123");
        rawMaterial.setRestaurantId("r123");
        rawMaterial.setCurrentLevel(15.0);
        rawMaterial.setReorderLevel(5.0);
        rawMaterial.setName("r12");
        rawMaterial.setCategory("c1");
        rawMaterial.setMeasurementUnits("Kg");
        rawMaterial.setCostPerUnit(50.0);
        rawMaterial.setTotalCost(750.0);
        rawMaterial.setPurchaseDate(LocalDate.now());
        rawMaterial.setExpirationDate(LocalDate.now().plusMonths(18));
        rawMaterial.setOperatorName("o1");

        editRawMaterialDTO.setRawMaterial(rawMaterial);


        RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
        rawMaterialDTO.setStatus(RawMaterialStatus.VALID_AND_SAVED);
        rawMaterialDTO.setRawMaterial(rawMaterial);
        when(rawMaterialService.updateRawMaterial(editRawMaterialDTO.getRawMaterialId(), editRawMaterialDTO.getRawMaterial())).thenReturn(rawMaterialDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/inventory/edit-raw-material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editRawMaterialDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteRawMaterial() throws Exception {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId("r123");


        when(rawMaterialService.deleteRawMaterial(rawMaterial)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/inventory/delete-raw-material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rawMaterial)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testDeleteAllRawMaterials() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("r123");


        when(rawMaterialService.deleteAllRawMaterials(restaurant)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/dinedynamo/restaurant/inventory/delete-all-raw-materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }

    @Test
    void testAddUsage() throws Exception {
        AddUsageForRawMaterialDTO addUsageForRawMaterialDTO = new AddUsageForRawMaterialDTO();
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRawMaterialId("r123");
        rawMaterial.setReorderLevel(5.0);
        rawMaterial.setName("r12");
        rawMaterial.setCategory("c1");
        rawMaterial.setMeasurementUnits("Kg");
        rawMaterial.setCostPerUnit(50.0);
        rawMaterial.setTotalCost(750.0);
        rawMaterial.setPurchaseDate(LocalDate.now());
        rawMaterial.setExpirationDate(LocalDate.now().plusMonths(18));
        rawMaterial.setOperatorName("o1");
        rawMaterial.setStatus(com.dinedynamo.collections.inventory_management.RawMaterialStatus.SUFFICIENT);
        rawMaterial.setCurrentLevel(15);

        addUsageForRawMaterialDTO.setAmountUsed(5);
        addUsageForRawMaterialDTO.setRawMaterialId("r123");

        RawMaterialDTO rawMaterialDTO = new RawMaterialDTO();
        rawMaterialDTO.setStatus(RawMaterialStatus.VALID_AND_SAVED);

        when(rawMaterialService.addUsage(rawMaterial.getRawMaterialId(), addUsageForRawMaterialDTO.getAmountUsed())).thenReturn(rawMaterialDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/dinedynamo/restaurant/inventory/add-usage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addUsageForRawMaterialDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"));
    }



}
