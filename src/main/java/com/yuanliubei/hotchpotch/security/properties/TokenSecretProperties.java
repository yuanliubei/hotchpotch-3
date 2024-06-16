package com.yuanliubei.hotchpotch.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuanlb
 * @since 2024/6/10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class TokenSecretProperties {
    // 加密盐
    private String secret;
    // access_token过期时间(ms)
    private long accessTokenExpirationDate;
    // refresh_token过期时间(ms)
    private long refreshTokenExpirationDate;

}
