package com.n23.expense_sharing_app.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JwtUtils {


    private static final String SECRET_KEY = "nisarg_bhjbcdbffnnkrhdbvvrupdjshikjfdbhka_nf";
//    private final AuthenticationManager authenticationManager;



    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


//    // 1. Generate Token
//    public String generateToken(String email)
//    {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // 2. Extract Username(Email)
//    public String extractUsername(String token){
//        return extractClaim(token, Claims::getSubject);
//    }
//
//
//    // 3. Validate Token
//    public boolean validateToken(String token, UserDetails userDetails){
//        final String username = extractUsername(token);
//
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    // Helpers
//    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//
//        return Jwts.parserBuilder().setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private boolean isTokenExpired(String token){
//        return extractClaim(token,Claims::getExpiration).before(new Date());
//    }


    public String generateToken(String email)
    {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 60))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token)
    {
        return extractClaim(token,t -> t.getSubject());
    }

    private <T>T extractClaim(String token, Function<Claims,T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, t -> t.getExpiration().before(new Date()));
    }


    public boolean validateToken(String token,UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }





}
