package com.ebuka.employeemanagementsysytem.security;

import com.ebuka.employeemanagementsysytem.enums.Status;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String jwtToken)  {
        return extractClaims(jwtToken, Claims::getSubject);
    }
    public String extractUserType(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userType", String.class);
    }


    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateTokenForCustomer(UserDetails userDetails, String email, Status status, String userType, String phoneNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", email);
        claims.put("status", status.toString());
        claims.put("userType", userType);
        claims.put("phoneNumber", phoneNumber);
        return generateToken(claims, userDetails);
    }

    public String generateRefreshTokenForCustomer(UserDetails userDetails, String email, Status status) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("status", status.toString());

        return generateRefreshToken(userDetails);
    }

    public String generateTokenForCustomer(UserDetails userDetails) {
        Map<String, Object> claims = Collections.emptyMap();
        return buildToken(claims, userDetails, jwtExpiration);
    }

    private String buildCustomToken(Map<String, Object> claims) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    public String generateToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }


    // builds the token
    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration){
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.putAll(extraClaims);
        claims.put("iat", new Date(System.currentTimeMillis()));
        claims.put("exp", new Date(System.currentTimeMillis() + expiration));
        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String userName) {
        final String username = extractUsername(token);
        return (username.equals(userName)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
