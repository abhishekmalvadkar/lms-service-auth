package com.amalvadkar.lms.auth.app.helper;

import com.amalvadkar.lms.auth.ApplicationProperties;
import com.amalvadkar.lms.auth.app.exception.TokenException;
import com.amalvadkar.lms.auth.app.models.dto.CreateTokenDto;
import com.amalvadkar.lms.auth.app.models.resonse.VerifyTokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.amalvadkar.lms.auth.app.constants.AppConstants.TOKEN_VERIFIED_SUCCESSFULLY_MSG;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenHelper {

    private static final String JWT_AUD_KEY = "aud";
    private static final String JWT_DEVICE_KEY = "device";

    private final ApplicationProperties appProps;
    private final Clock clock;

    public String generate(CreateTokenDto createTokenDto) {
        Instant now = clock.instant();
        return Jwts.builder()
                .claims(prepareClaims(createTokenDto))
                .subject(createTokenDto.userId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(appProps.jwtExpiryTimeInSec())))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    private static Map<String, Object> prepareClaims(CreateTokenDto createTokenDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_AUD_KEY, createTokenDto.roleId());
        claims.put(JWT_DEVICE_KEY, createTokenDto.device());
        return claims;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appProps.jwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public VerifyTokenResponse verify(String token) {
        try {
            return processToken(token);
        } catch (MalformedJwtException e) {
            throw new TokenException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new TokenException("Token is expired");
        } catch (UnsupportedJwtException e) {
            throw new TokenException("Unsupported token");
        } catch (IllegalArgumentException e) {
            throw new TokenException("Claims is empty");
        }
    }

    private VerifyTokenResponse processToken(String token) {
        Jws<Claims> claims = validate(token);
        return prepareTokenResponse(claims);
    }

    private static VerifyTokenResponse prepareTokenResponse(Jws<Claims> claims) {
        String userId = claims.getPayload().getSubject();
        String roleId = extractClaimAsString(claims.getPayload().get(JWT_AUD_KEY));
        String device = extractClaimAsString(claims.getPayload().get(JWT_DEVICE_KEY));
        return new VerifyTokenResponse(userId, roleId, device, HttpStatus.OK.value(), TOKEN_VERIFIED_SUCCESSFULLY_MSG);
    }

    private static String extractClaimAsString(Object claim) {
        return switch (claim) {
            case null -> null;
            case String str -> str;
            case Collection<?> collection when !collection.isEmpty() ->
                    collection.iterator().next().toString(); // return first value

            default -> claim.toString();
        };
    }

    private Jws<Claims> validate(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
    }

}