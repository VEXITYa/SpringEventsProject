package dev.kuchishkin.security.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

    private final SecretKey secretKey;
    private final long lifetime;


    public JwtTokenManager(
        @Value("${jwt.secret-key}") String stringKey,
        @Value("${jwt.lifetime}") long lifetime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(stringKey.getBytes());
        this.lifetime = lifetime;
    }

    public String generateToken(String login) {
        return Jwts
            .builder()
            .subject(login)
            .signWith(secretKey)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + lifetime))
            .compact();
    }

    public String getLoginFromToken(String token) {
        return Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

}
