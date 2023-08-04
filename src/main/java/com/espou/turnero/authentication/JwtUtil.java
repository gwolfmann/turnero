package com.espou.turnero.authentication;

import com.espou.turnero.service.MeetService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; // You can store the secret in application.properties

    @Value("${jwt.expiration}")
    private long expirationMs; // Expiration time in milliseconds
    private final Logger logger = LoggerFactory.getLogger(MeetService.class);

    public String generateToken(String userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);
        logger.info("Received login request for user: {}", userId);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUserId(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
