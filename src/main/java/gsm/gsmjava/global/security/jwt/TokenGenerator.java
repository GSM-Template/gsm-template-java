package gsm.gsmjava.global.security.jwt;

import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public TokenDto generateToken(Long userId) {
        return TokenDto.builder()
                .accessToken(generateAccessToken(userId))
                .refreshToken(generateRefreshToken(userId))
                .accessTokenExp(accessExp)
                .refreshTokenExp(refreshExp)
                .build();
    }

    private String generateAccessToken(Long userId) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(accessSecret.getBytes()), SignatureAlgorithm.ES256)
                .setSubject(String.valueOf(userId))
                .claim(TOKEN_TYPE.getContent(), ACCESS.getContent())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExp * 1000L))
                .compact();
    }

    private String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(refreshSecret.getBytes()), SignatureAlgorithm.ES256)
                .setSubject(String.valueOf(userId))
                .claim(TOKEN_TYPE.getContent(), REFRESH.getContent())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExp * 1000L))
                .compact();
    }
}
