package com.rgt.walink.telegram

import com.rgt.walink.properties.MessageProperties
import com.rgt.walink.properties.TelegramProperties
import com.rgt.walink.service.WhatsAppLinkService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import spock.lang.Specification

class SimpleBotTest extends Specification {

    static final HELP_MESSAGE = 'help'
    static final START_MESSAGE = 'start'
    static final ERROR_MESSAGE = 'error'
    static final NO_TEXT_MESSAGE = 'noText'
    static final RESULT = 'result'
    static final CHAT_ID = '1337'

    SimpleBot bot
    WhatsAppLinkService whatsAppService
    MessageProperties messageProperties

    def setup() {
        whatsAppService = Mock(WhatsAppLinkService)
        messageProperties = new MessageProperties(start: START_MESSAGE, help: HELP_MESSAGE, error: ERROR_MESSAGE, noText: NO_TEXT_MESSAGE)
        bot = new SimpleBot(Stub(SetWebhook), Stub(TelegramProperties), messageProperties, whatsAppService)
    }

    def "should return result of whatsApp service"() {
        given:
        def text = '+78005553535'
        def updateEvent = getUpdateEvent(text, CHAT_ID)
        1 * whatsAppService.createLink(text) >> RESULT

        when:
        def response = bot.onWebhookUpdateReceived(updateEvent) as SendMessage

        then:
        response.getText() == RESULT
        response.getChatId() == CHAT_ID
    }

    def "should return prepared answers for special requests"() {
        given:
        def updateEvent = getUpdateEvent(text, CHAT_ID)

        when:
        def response = bot.onWebhookUpdateReceived(updateEvent) as SendMessage

        then:
        0 * whatsAppService.createLink(text)
        response.getText() == expected
        response.getChatId() == CHAT_ID

        where:
        text     | expected
        null     | NO_TEXT_MESSAGE
        '/start' | START_MESSAGE
        '/help'  | HELP_MESSAGE
    }

    def "should return error message when WhatsApp service throw exception"() {
        given:
        def text = 'wrongFormat'
        def updateEvent = getUpdateEvent(text, CHAT_ID)
        1 * whatsAppService.createLink(text) >> { throw new RuntimeException() }

        when:
        def response = bot.onWebhookUpdateReceived(updateEvent) as SendMessage

        then:
        response.getText() == ERROR_MESSAGE
        response.getChatId() == CHAT_ID
    }

    private Update getUpdateEvent(String text, String chatId) {
        return new Update(message: new Message(text: text, chat: new Chat(id: Long.valueOf(chatId))))
    }
}
