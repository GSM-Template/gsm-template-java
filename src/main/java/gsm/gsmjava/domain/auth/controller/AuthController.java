package gsm.gsmjava.domain.auth.controller;

import gsm.gsmjava.domain.auth.service.ReissueTokenService;
import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import static gsm.gsmjava.global.util.HeaderConstants.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final ReissueTokenService reissueTokenService;

    @PatchMapping("/refresh")
    public ResponseEntity<Void> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESH_TOKEN.getName());
        TokenDto tokenDto = reissueTokenService.execute(refreshToken);

        response.setHeader(AUTHORIZATION.getName(), BEARER_PREFIX.getName() + tokenDto.getAccessToken());
        response.setHeader(REFRESH_TOKEN.getName(), BEARER_PREFIX.getName() + tokenDto.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

}
