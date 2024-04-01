package com.dinedynamo.dto.authentication_dtos;


import com.dinedynamo.collections.restaurant_collections.AppUser;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponseDTO {

    String accessToken;

    String refreshToken;

    String restaurantId;

    AppUser appUser;
}
