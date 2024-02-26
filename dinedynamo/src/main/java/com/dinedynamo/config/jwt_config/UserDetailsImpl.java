package com.dinedynamo.config.jwt_config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;




public class UserDetailsImpl implements UserDetails
{
    String userId;
    String userEmail;
    String userPassword;
    List<GrantedAuthority> userRole;



//    public UserDetailsImpl(User user) {
//
//        userPassword=user.getUserPassword();
//        userId=user.getUserId();
//        userEmail=user.getUserEmail();
//        userRole = Arrays.stream(user.getUserRole().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return userRole;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return userPassword;
    }

    @Override
    public String getUsername() {
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

}