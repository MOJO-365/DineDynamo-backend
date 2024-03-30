package com.dinedynamo.collections.restaurant_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("refresh_tokens")
public class RefreshToken {
    @Id
    private String refreshTokenId;

    //Random uuid
    private String refreshToken;
    private Instant expiryDate;
   private AppUser appUser;
}
