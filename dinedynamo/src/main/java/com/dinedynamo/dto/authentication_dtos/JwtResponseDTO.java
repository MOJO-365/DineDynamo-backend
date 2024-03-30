package com.dinedynamo.dto.authentication_dtos;


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
}
