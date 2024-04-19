package com.dinedynamo.services.restaurant_services;


import com.dinedynamo.collections.restaurant_collections.AppUser;
import com.dinedynamo.collections.restaurant_collections.RefreshToken;
import com.dinedynamo.repositories.restaurant_repositories.AppUserRepository;
import com.dinedynamo.repositories.restaurant_repositories.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    AppUserRepository appUserRepository;


    public RefreshToken createRefreshToken(String userEmail){

        AppUser appUser = appUserRepository.findByUserEmail(userEmail).orElse(null);

        if(appUser == null){
            throw new UsernameNotFoundException("Invalid user request, User not found in db");
        }
        RefreshToken refreshToken = RefreshToken.builder()
                .appUser(appUser)
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(86400000)) // expiry of refresh token is 1 day
                .build();
        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){

        if(refreshToken.getExpiryDate().compareTo(Instant.now())<0){
            //if token is expired, it deletes it from the db and return null
            refreshTokenRepository.delete(refreshToken);
            return null;
            //throw new RuntimeException(refreshToken.getRefreshToken() + " Refresh token is expired. Please make a new login..!");
        }
        return refreshToken;
    }
}
