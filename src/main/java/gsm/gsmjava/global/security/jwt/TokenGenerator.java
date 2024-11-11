package gsm.gsmjava.global.security.jwt;

import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import gsm.gsmjava.global.security.jwt.properties.JwtEnvironment;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static gsm.gsmjava.global.security.jwt.properties.JwtProperties.*;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtEnvironment jwtEnv;

    public TokenDto generateToken(String email) {
        return TokenDto.builder()
                .accessToken(generateAccessToken(email))
                .refreshToken(generateRefreshToken(email))
                .accessTokenExp(jwtEnv.accessExp())
                .refreshTokenExp(jwtEnv.refreshExp())
                .build();
    }

    public String getEmailFromRefreshToken(String token) {
        return getRefreshTokenSubject(token);
    }

    private String generateAccessToken(String email) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtEnv.accessSecret().getBytes()), SignatureAlgorithm.HS256)
                .setSubject(email)
                .claim(TOKEN_TYPE.getContent(), ACCESS.getContent())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtEnv.accessExp() * 1000L))
                .compact();
    }

    private String generateRefreshToken(String email) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtEnv.refreshSecret().getBytes()), SignatureAlgorithm.HS256)
                .setSubject(email)
                .claim(TOKEN_TYPE.getContent(), REFRESH.getContent())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtEnv.refreshExp() * 1000L))
                .compact();
    }

    private String getRefreshTokenSubject(String refreshToken) {
        return getTokenBody(refreshToken, Keys.hmacShaKeyFor(jwtEnv.refreshSecret().getBytes())).getSubject();
    }

    public static Claims getTokenBody(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
