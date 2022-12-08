package com.rgt.walink.controller;

import com.rgt.walink.telegram.SimpleBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final SimpleBot bot;

    @PostMapping("/")
    public BotApiMethod<Message> onUpdateReceived(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
