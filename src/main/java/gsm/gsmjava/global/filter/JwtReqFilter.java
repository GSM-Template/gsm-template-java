package gsm.gsmjava.global.filter;

import gsm.gsmjava.global.security.jwt.TokenParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static gsm.gsmjava.global.util.HeaderConstants.AUTHORIZATION;
import static gsm.gsmjava.global.util.HeaderConstants.BEARER_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtReqFilter extends OncePerRequestFilter {

    private final TokenParser tokenParser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader(AUTHORIZATION.getName());

        if (accessToken != null) {
            accessToken = accessToken.replaceFirst(BEARER_PREFIX.getName(), "").trim();

            UsernamePasswordAuthenticationToken authentication = tokenParser.authenticate(accessToken);
            SecurityContextHolder.clearContext();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}