package com.tiketika.Tiketika.security.jwtUtil;

import com.tiketika.Tiketika.common.auth.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "CW4XAMzsfGInhHYOgCDj1tQToNbfc3SGwHw602yibCSDed63F3pQWcpw6ogFkrTyvkL/hOpiRJuEOPG0mYSSRA==";
    private final Key key = Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET_KEY));

    private final long JWT_EXPIRATION = 1000L * 60 * 60 * 24; // 1 day

    public String generateToken(UserDetails userDetails, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userType);
        return createToken(claims, userDetails.getUsername(), userType);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getUserType().name());
        return createToken(claims, user.getEmail(), user.getUserType().name());
    }

    private String createToken(Map<String, Object> claims, String subject, String userType) {
        long expirationTime = userType.equals("CUSTOMER") ? 1000L * 60 * 60 * 24 * 360 : JWT_EXPIRATION;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
