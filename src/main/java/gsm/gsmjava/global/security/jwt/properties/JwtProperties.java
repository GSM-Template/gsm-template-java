package gsm.gsmjava.global.security.jwt.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtProperties {

    TOKEN_TYPE("tokenType"),
    ACCESS("accessToken"),
    REFRESH("refreshToken");

    private String content;
}
