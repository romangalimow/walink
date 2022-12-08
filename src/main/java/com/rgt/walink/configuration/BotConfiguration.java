package com.rgt.walink.configuration;

import com.rgt.walink.properties.MessageProperties;
import com.rgt.walink.properties.TelegramProperties;
import com.rgt.walink.service.WhatsAppLinkService;
import com.rgt.walink.telegram.SimpleBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {

    private final TelegramProperties telegramProperties;

    @Bean
    public SetWebhook setWebhook() {
        return SetWebhook.builder()
                .url(telegramProperties.getWebhookPath())
                .build();
    }

    @Bean
    public SimpleBot simpleBot(SetWebhook setWebhook,
                               MessageProperties messageProperties,
                               WhatsAppLinkService whatsAppLinkService) {
        return new SimpleBot(setWebhook, telegramProperties, messageProperties, whatsAppLinkService);
    }
}
