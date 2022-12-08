package com.rgt.walink.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("telegram")
public class TelegramProperties {

    private String botName;
    private String botToken;
    private String webhookPath;
}
