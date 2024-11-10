package gsm.gsmjava.global.security.jwt;

import gsm.gsmjava.global.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static gsm.gsmjava.global.security.jwt.TokenGenerator.getTokenBody;

@Component
@RequiredArgsConstructor
public class TokenParser {

    @Value("${jwt.accessSecret}")
    private String accessSecret;

    private final CustomUserDetailsService customUserDetailsService;

    public UsernamePasswordAuthenticationToken authenticate(String accessToken) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUserEmail(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserEmail(String accessToken) {
        return getAccessTokenSubject(accessToken);
    }

    private String getAccessTokenSubject(String accessToken) {
        return getTokenBody(accessToken, Keys.hmacShaKeyFor(accessSecret.getBytes())).getSubject();
    }

}
