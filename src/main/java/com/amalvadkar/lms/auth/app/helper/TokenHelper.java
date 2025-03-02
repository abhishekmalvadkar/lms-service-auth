package com.amalvadkar.lms.auth.app.helper;

import com.amalvadkar.lms.auth.ApplicationProperties;
import com.amalvadkar.lms.auth.app.entities.UserEntity;
import com.amalvadkar.lms.auth.app.exception.AuthException;
import com.amalvadkar.lms.auth.app.models.resonse.JwtTokenVerifyRes;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenHelper {

    private static final String JWT_AUD_KEY = "aud";

    private final ApplicationProperties appProps;
    private final Clock clock;

    public String generateToken(UserEntity user) {
        Instant now = clock.instant();
        return Jwts.builder()
                .claims(prepareClaims(user))
                .subject(user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(appProps.jwtExpiryTimeInSec())))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    private static Map<String, Object> prepareClaims(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_AUD_KEY, user.getRole().getId());
        return claims;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appProps.jwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenVerifyRes validateJwtToken(String authToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(authToken);
               return new JwtTokenVerifyRes(claimsJws.getPayload().getSubject(),claimsJws.getPayload().get(JWT_AUD_KEY));
        } catch ( MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new AuthException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new AuthException(e.getMessage(),HttpStatus.BAD_REQUEST.value());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new AuthException(e.getMessage(),HttpStatus.BAD_REQUEST.value());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new AuthException(e.getMessage(),HttpStatus.BAD_REQUEST.value());
        }

}

}