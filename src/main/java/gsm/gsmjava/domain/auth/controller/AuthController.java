package gsm.gsmjava.domain.auth.controller;

import gsm.gsmjava.domain.auth.service.ReissueTokenService;
import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import static gsm.gsmjava.global.security.jwt.properties.JwtProperties.ACCESS;
import static gsm.gsmjava.global.security.jwt.properties.JwtProperties.REFRESH;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final ReissueTokenService reissueTokenService;

    @PatchMapping("/refresh")
    public ResponseEntity<Void> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESH.getContent());
        TokenDto tokenDto = reissueTokenService.execute(refreshToken);

        response.setHeader(ACCESS.getContent(), tokenDto.getAccessToken());
        response.setHeader(REFRESH.getContent(), tokenDto.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

}
