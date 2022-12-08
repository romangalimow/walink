package com.rgt.walink.telegram;

import com.rgt.walink.properties.MessageProperties;
import com.rgt.walink.properties.TelegramProperties;
import com.rgt.walink.service.WhatsAppLinkService;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
public class SimpleBot extends SpringWebhookBot {

    private static final String HELP_COMMAND = "/help";
    private static final String START_COMMAND = "/start";

    private final String botPath;
    private final String botToken;
    private final String botUsername;
    private final MessageProperties messageProperties;
    private final WhatsAppLinkService whatsAppLinkService;

    public SimpleBot(SetWebhook setWebhook,
                     TelegramProperties properties,
                     MessageProperties messageProperties,
                     WhatsAppLinkService whatsAppLinkService) {
        super(setWebhook);
        this.botToken = properties.getBotToken();
        this.botPath = properties.getWebhookPath();
        this.botUsername = properties.getBotName();
        this.messageProperties = messageProperties;
        this.whatsAppLinkService = whatsAppLinkService;
    }

    @Override
    public BotApiMethod<Message> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();
            return handleMessage(message);
        }
        throw new UnsupportedOperationException();
    }

    private SendMessage handleMessage(Message message) {
        var request = message.getText();
        var response = getResponse(request);
        var chatId = String.valueOf(message.getChatId());
        return new SendMessage(chatId, response);
    }

    private String getResponse(String request) {
        try {
            if (!StringUtils.hasText(request)) {
                return messageProperties.getNoText();
            }
            return switch (request) {
                case HELP_COMMAND -> messageProperties.getHelp();
                case START_COMMAND -> messageProperties.getStart();
                default -> whatsAppLinkService.createLink(request);
            };
        } catch (Exception ex) {
            return messageProperties.getError();
        }
    }
}
