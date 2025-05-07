package com.tridev.geoSphere.utils;


import com.tridev.geoSphere.filters.JwtFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    public String getCurrentToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getCredentials() != null) {
            return auth.getCredentials().toString();
        }
        return null;
    }


    private SecretKey  getSigningKey(){
        String SECRET_KEY = "Abfsd*dsssjkmayfjdlkpagRtafj?gsjehav";
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }



    public String extractUsername(String token)  {
        Claims claims = extractAllClaims(token);

        return claims.getSubject();
    }

    public Long getUserIdFromToken() {
        String token = getCurrentToken();
        return extractAllClaims(token).get("userId", Long.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }


    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }


    private Claims extractAllClaims(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (JwtFilter.JwtAuthenticationException ex){
            throw new JwtFilter.JwtAuthenticationException(ex.getStatus(), ex.getMessage());
        }catch (ExpiredJwtException ex){
            throw new JwtFilter.JwtAuthenticationException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, Long userId, String firstName, String lastName) {
        Map<String, Object> claims = new HashMap<>();
//        claims.put("role",role);
        claims.put("userId",userId);
        claims.put("firstName",firstName);
        claims.put("lastName", lastName);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 )) //  1 Day expiry time
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}
