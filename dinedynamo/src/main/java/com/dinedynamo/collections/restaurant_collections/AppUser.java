package com.dinedynamo.collections.restaurant_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexOptions;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("app_users")
public class AppUser {

    @Id
    String userId;

    String restaurantId;

    String userType;

    @Indexed(unique = true)
    String userEmail;

    String userPassword;

}
