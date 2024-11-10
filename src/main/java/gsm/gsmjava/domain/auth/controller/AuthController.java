package gsm.gsmjava.domain.auth.controller;

import gsm.gsmjava.domain.auth.service.ReissueTokenService;
import gsm.gsmjava.domain.user.entity.User;
import gsm.gsmjava.domain.user.repository.UserRepository;
import gsm.gsmjava.global.security.jwt.TokenGenerator;
import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        response.setHeader(BEARER_PREFIX.getName() + AUTHORIZATION.getName(), tokenDto.getAccessToken());
        response.setHeader(REFRESH_TOKEN.getName(), tokenDto.getRefreshToken());

        return ResponseEntity.noContent().build();
    }
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;


    @PostMapping("/login")
    public void login(@RequestParam String email, HttpServletResponse response) {
        User user = userRepository.findByEmail(email).get();

        TokenDto tokenDto = tokenGenerator.generateToken(user.getEmail());

        response.setHeader(AUTHORIZATION.getName(), tokenDto.getAccessToken());
        response.setHeader(REFRESH_TOKEN.getName(), tokenDto.getRefreshToken());
    }
}
