package com.dinedynamo.collections.authentication_collections;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
    private String resetToken;
    private String newPassword;

}
