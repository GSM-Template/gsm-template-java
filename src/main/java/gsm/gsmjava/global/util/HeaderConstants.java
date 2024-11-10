package gsm.gsmjava.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeaderConstants {
    AUTHORIZATION("Authorization"),
    REFRESH_TOKEN("Refresh_Token"),
    BEARER_PREFIX("Bearer ");

    private String name;
}
