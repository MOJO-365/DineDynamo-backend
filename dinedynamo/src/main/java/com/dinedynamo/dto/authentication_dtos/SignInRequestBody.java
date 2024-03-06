package com.dinedynamo.dto.authentication_dtos;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignInRequestBody
{
    String userEmail;
    String userPassword;
//    String userType;

}
