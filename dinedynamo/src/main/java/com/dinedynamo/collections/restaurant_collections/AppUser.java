package com.dinedynamo.collections.restaurant_collections;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("app_users")
public class AppUser implements UserDetails {

    @Id
    String userId;

    String restaurantId;

    String userType;

    @Indexed(unique = true)
    String userEmail;

    String userPassword;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("in getAuthorities of appuser");
        return List.of(new SimpleGrantedAuthority(this.userType));
    }

    @Override
    public String getPassword() {
        System.out.println("In getPassword of appuser: "+this.userPassword);
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        System.out.println("In getUsername of appuser: "+this.userEmail);
        return this.userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
