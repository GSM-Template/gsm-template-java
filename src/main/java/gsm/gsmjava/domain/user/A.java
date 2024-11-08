package gsm.gsmjava.domain.user;

import gsm.gsmjava.global.error.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class A {
    @GetMapping("/asd")
    public void a() {
        throw new GlobalException("asd", HttpStatus.BAD_GATEWAY);
    }
}
