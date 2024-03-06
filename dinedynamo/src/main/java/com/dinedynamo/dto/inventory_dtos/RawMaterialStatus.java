package com.dinedynamo.dto.inventory_dtos;

public enum RawMaterialStatus {

    NEGATIVE,  // For negative currentLevel of raw material
    POSITIVE, // For negative currentLevel of raw material
    INVALID_EXPIRY,  // For invalid expiry date of raw material
    VALID_AND_SAVED // If raw material is saved to database
}
