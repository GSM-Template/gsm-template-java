package gsm.gsmjava.global.security.jwt;

import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static gsm.gsmjava.global.security.jwt.properties.JwtProperties.*;

@Component
public class TokenGenerator {

    @Value("${jwt.accessSecret}")
    private String accessSecret;

    @Value("${jwt.refreshSecret}")
    private String refreshSecret;

    @Value("${jwt.accessExp}")
    private int accessExp;

    @Value("${jwt.refreshExp}")
    private int refreshExp;

    public TokenDto generateToken(String email) {
        return TokenDto.builder()
                .accessToken(generateAccessToken(email))
                .refreshToken(generateRefreshToken(email))
                .accessTokenExp(accessExp)
                .refreshTokenExp(refreshExp)
                .build();
    }

    public String getEmailFromRefreshToken(String token) {
        return getRefreshTokenSubject(token);
    }

    private String generateAccessToken(String email) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(accessSecret.getBytes()), SignatureAlgorithm.HS256)
                .setSubject(email)
                .claim(TOKEN_TYPE.getContent(), ACCESS.getContent())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExp * 1000L))
                .compact();
    }

    private String generateRefreshToken(String email) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(refreshSecret.getBytes()), SignatureAlgorithm.HS256)
                .setSubject(email)
                .claim(TOKEN_TYPE.getContent(), REFRESH.getContent())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExp * 1000L))
                .compact();
    }

    private String getRefreshTokenSubject(String refreshToken) {
        return getTokenBody(refreshToken, Keys.hmacShaKeyFor(refreshSecret.getBytes())).getSubject();
    }

    public static Claims getTokenBody(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
