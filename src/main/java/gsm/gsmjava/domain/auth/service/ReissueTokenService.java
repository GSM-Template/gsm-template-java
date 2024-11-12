package gsm.gsmjava.domain.auth.service;

import gsm.gsmjava.domain.auth.entity.RefreshToken;
import gsm.gsmjava.domain.auth.repository.RefreshTokenRepository;
import gsm.gsmjava.domain.user.repository.UserRepository;
import gsm.gsmjava.global.error.GlobalException;
import gsm.gsmjava.global.security.jwt.TokenGenerator;
import gsm.gsmjava.global.security.jwt.dto.TokenDto;
import gsm.gsmjava.global.security.jwt.properties.JwtEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReissueTokenService {

    private final JwtEnvironment jwtEnv;
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final String BEARER_PREFIX = "Bearer ";

    @Transactional
    public TokenDto execute(String token) {
        isNotNullRefreshToken(token);

        String removePrefixToken = token.replaceFirst(BEARER_PREFIX, "").trim();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(removePrefixToken)
                .orElseThrow(() -> new GlobalException("존재하지 않는 refresh token 입니다.", HttpStatus.NOT_FOUND));

        String email = tokenGenerator.getEmailFromRefreshToken(refreshToken.getToken());
        isExistsUser(email);

        TokenDto tokenDto = tokenGenerator.generateToken(email);
        saveNewRefreshToken(tokenDto.getRefreshToken(), refreshToken.getUserId());
        return tokenDto;
    }

    private void isNotNullRefreshToken(String token) {
        if (token == null)
            throw new GlobalException("refresh token을 요청 헤더에 포함시켜 주세요.", HttpStatus.BAD_REQUEST);
    }

    private void isExistsUser(String email) {
        if (!userRepository.existsByEmail(email))
            throw new GlobalException("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    private void saveNewRefreshToken(String token, Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expirationTime(jwtEnv.refreshExp())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

}
