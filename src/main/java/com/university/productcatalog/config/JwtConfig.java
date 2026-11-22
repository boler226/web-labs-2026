package com.university.productcatalog.config;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Bean
    JwtEncoder jwtEncoder(@Value("${lab.jwt.secret}") String secret) {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        if (key.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET must contain at least 32 UTF-8 bytes");
        }
        return new NimbusJwtEncoder(new ImmutableSecret<>(new SecretKeySpec(key, "HmacSHA256")));
    }
}
