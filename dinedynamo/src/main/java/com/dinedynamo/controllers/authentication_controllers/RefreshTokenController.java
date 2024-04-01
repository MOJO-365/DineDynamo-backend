package com.dinedynamo.controllers.authentication_controllers;

import com.dinedynamo.api.ApiResponse;

import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.RefreshToken;
import com.dinedynamo.dto.authentication_dtos.JwtResponseDTO;
import com.dinedynamo.dto.authentication_dtos.RefreshTokenRequestDTO;
import com.dinedynamo.dto.authentication_dtos.SignInRequestBody;
import com.dinedynamo.config.jwt_config.UserDetailsServiceImpl;
import com.dinedynamo.helper.EncryptionDecryptionUtil;
import com.dinedynamo.helper.JwtHelper;
import com.dinedynamo.repositories.restaurant_repositories.RefreshTokenRepository;
import com.dinedynamo.services.restaurant_services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    EncryptionDecryptionUtil encryptionDecryptionUtil;

    /**
     *

     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     *
     * Use: get a refresh token. This api needs to be hit before the old token expires else user will have to login again using password and username
     */
    @PostMapping("/dinedynamo/refreshtoken")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenRequestDTO.getRefreshToken()).orElse(null);


        if(refreshToken == null){
            throw new RuntimeException("Invalid refresh token-not found in db, signin again");
        }

        else{
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);

            if(refreshToken == null){

                System.out.println("Refresh token expired, redirect to signin");
                return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, "success","TOKEN_EXPIRED"),HttpStatus.OK);

            }
            else{
                AppUser appUser = refreshToken.getAppUser();

                String accessToken = jwtHelper.generateToken(appUser);

                JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
                jwtResponseDTO.setAccessToken(encryptionDecryptionUtil.encrypt(accessToken));
                jwtResponseDTO.setRestaurantId(jwtHelper.extractRestaurantId(accessToken));
                jwtResponseDTO.setRefreshToken(refreshTokenRequestDTO.getRefreshToken());

                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",jwtResponseDTO),HttpStatus.OK);
            }
        }


    }

    @PostMapping("/dinedynamo/restaurant/delete-restaurant-refreshtoken")
    ResponseEntity<ApiResponse> deleteRefreshTokenByRestaurantId(@RequestParam String refreshToken){

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success",true),HttpStatus.OK);


    }

}
