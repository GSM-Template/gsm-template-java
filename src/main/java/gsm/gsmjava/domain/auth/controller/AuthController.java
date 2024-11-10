package gsm.gsmjava.domain.auth.controller;

import gsm.gsmjava.domain.auth.service.ReissueTokenService;
import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final ReissueTokenService reissueTokenService;

    @PatchMapping("/refresh")
    public ResponseEntity<Void> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("Refresh_Token");
        TokenDto tokenDto = reissueTokenService.execute(refreshToken);

        response.setHeader("Authorization", tokenDto.getAccessToken());
        response.setHeader("Refresh_Token", tokenDto.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

}
