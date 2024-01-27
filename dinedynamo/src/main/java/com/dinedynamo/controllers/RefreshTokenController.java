package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.RefreshJWTRequest;
import com.dinedynamo.collections.SignInRequestBody;
import com.dinedynamo.config.UserDetailsServiceImpl;
import com.dinedynamo.helper.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin("*")
public class RefreshTokenController
{
    @Autowired
    JwtHelper jwtHelper;

    UserDetails userDetails;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/dinedynamo/refreshtoken")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshJWTRequest refreshJWTRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String oldToken = refreshJWTRequest.getToken();

        String userEmailFromToken = jwtHelper.getUsernameFromToken(oldToken);
        String userRole = jwtHelper.extractUserRole(oldToken);
        userDetailsServiceImpl.setUserRole(userRole);
        userDetails = this.userDetailsServiceImpl.loadUserByUsername(userEmailFromToken);
        SignInRequestBody signInRequestBody = new SignInRequestBody();
        signInRequestBody.setUserEmail(userEmailFromToken);
        signInRequestBody.setUserType(userRole.toUpperCase());
        String refreshedToken = jwtHelper.generateToken(userDetails,signInRequestBody);


        System.out.println("REFRESH TOKEN GENERATED : "+refreshedToken);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",refreshedToken),HttpStatus.OK);

    }

}
