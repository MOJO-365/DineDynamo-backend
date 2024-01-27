package com.dinedynamo.helper;



import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import com.dinedynamo.collections.Customer;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.SignInRequestBody;
import com.dinedynamo.config.UserDetailsServiceImpl;
import com.dinedynamo.repositories.CustomerRepository;
import com.dinedynamo.repositories.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;

//All the operations related to token will be done here
@Component
public class JwtHelper
{

    @Autowired
    RestaurantRepository restaurantRepository;



    @Autowired
    CustomerRepository customerRepository;

    public static final long TOKEN_VALIDITY = 100*60*100*60; //60minutes 	//specify after how many milisec, token expires
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    //SECRET is for digital signature of token


    //Will get userName from token
    public String getUsernameFromToken(String token)
    {
        return getClaimFromToken(token, Claims::getSubject);
        //setSubject() : used for setting the username in jwt
    }

    //Will get Expiration Date from token
    public Date getExpirationDateFromToken(String token)
    {
        return getClaimFromToken(token, Claims::getExpiration);

    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }


    // To get data from token, first SECRET will be required to specify to decode the token
    private Claims getAllClaimsFromToken(String token)
    {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }


    public Boolean isTokenExpired(String token)
    {
        final Date expirationDate = getExpirationDateFromToken(token);
        System.out.println("EXP DATE--------->"+expirationDate);
        return expirationDate.before(new Date());

    }


    // Generates token for user:
    public String generateToken(UserDetails userDetails, SignInRequestBody signInRequestBody) throws NoSuchAlgorithmException, InvalidKeyException, java.security.InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException
    {
        String token = null;
        Map<String, Object> claims = new HashMap<>();

        if(signInRequestBody.getUserType().toUpperCase().equals("RESTAURANT")){

            Restaurant restaurant = restaurantRepository.findByRestaurantEmail(signInRequestBody.getUserEmail()).orElse(null);

            if(restaurant == null){
                System.out.println("In generateToken(): Restaurant Data does not exist in DB");
                System.out.println("Token generation failed");
                return null;
            }

            claims.put("userEmail", signInRequestBody.getUserEmail());
            claims.put("restaurantOrCustomerId",restaurant.getRestaurantId());
            claims.put("userRole","RESTAURANT");

        }
        else if(signInRequestBody.getUserType().toUpperCase().equals("CUSTOMER")){

            Customer customer = customerRepository.findByCustomerEmail(signInRequestBody.getUserEmail()).orElse(null);

            if(customer == null){
                System.out.println("In generateToken(): Customer Data does not exist in DB");
                System.out.println("Token generation failed");
                return null;
            }


            claims.put("userEmail", signInRequestBody.getUserEmail());
            claims.put("restaurantOrCustomerId",customer.getCustomerId());
            claims.put("userRole","CUSTOMER");

        }

        token =  doGenerateToken(claims, signInRequestBody.getUserEmail());

        return token;

    }


    // This will set all claims of token
    private String doGenerateToken(Map<String, Object> claims, String subject)
    {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

    }


    // Is token valid?
    public boolean validateToken(String token, UserDetails userDetails)
    {

        final String username = getUsernameFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String extractRestaurantId(String token) {
        return extractClaim(token, claims -> claims.get("restaurantId", String.class));
    }


    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("userRole", String.class));
    }






}

