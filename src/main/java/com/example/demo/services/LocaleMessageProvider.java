package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class LocaleMessageProvider {

    private final MessageSource messageSource;

    @Autowired
    public LocaleMessageProvider(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Locale locale) {
        return new String(messageSource.getMessage(code, new Object[0], locale).getBytes(), StandardCharsets.UTF_8);
    }
}
