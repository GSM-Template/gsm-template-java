package gsm.gsmjava.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = {
        "gsm.gsmjava.global.security.jwt.properties"})
public class PropertiesScanConfig {
}
