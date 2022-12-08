package com.rgt.walink.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("messages")
public class MessageProperties {

    private String help;
    private String start;
    private String error;
    private String noText;
}
