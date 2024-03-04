package com.dinedynamo.dto.restaurant_dtos;


import com.dinedynamo.collections.restaurant_collections.AppUser;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditAppUserDTO {

    String userId;

    AppUser appUser;
}
