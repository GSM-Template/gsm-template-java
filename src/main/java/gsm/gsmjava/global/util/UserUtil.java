package gsm.gsmjava.global.util;

import gsm.gsmjava.domain.user.entity.User;
import gsm.gsmjava.domain.user.repository.UserRepository;
import gsm.gsmjava.global.error.GlobalException;
import gsm.gsmjava.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            String email = ((CustomUserDetails) principal).getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new GlobalException("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        } else {
            throw new GlobalException("현재 인증되어 있는 유저의 principal이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

}
