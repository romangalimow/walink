package com.rgt.walink.service;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppLinkService {

    private static final String PLUS_SYMBOL = "+";
    private static final String WHATS_APP_LINK_PREFIX = "https://wa.me/";
    private static final String PHONE_EXTRA_SYMBOLS_REGEX = "[- ()—]";
    private static final String PHONE_SUPPORTED_FORMAT_REGEX = "^\\+?\\d{11,15}$";

    public String createLink(String text) {
        var phoneNumber = preparePhoneNumber(text);
        if (isCorrectPhoneNumber(phoneNumber)) {
            return WHATS_APP_LINK_PREFIX + phoneNumber;
        }
        throw new IllegalArgumentException();
    }

    private String preparePhoneNumber(String phone) {
        var cleaned = removeExtraSymbols(phone);
        return cleaned.startsWith(PLUS_SYMBOL)
                ? cleaned
                : PLUS_SYMBOL + cleaned;
    }

    private String removeExtraSymbols(String text) {
        return text.replaceAll(PHONE_EXTRA_SYMBOLS_REGEX, "");
    }

    private boolean isCorrectPhoneNumber(String text) {
        return text.matches(PHONE_SUPPORTED_FORMAT_REGEX);
    }
}
