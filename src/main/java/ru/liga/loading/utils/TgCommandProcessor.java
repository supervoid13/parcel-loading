package ru.liga.loading.utils;

import org.telegram.telegrambots.meta.api.objects.message.Message;

@FunctionalInterface
public interface TgCommandProcessor {
    String process(Message message);
}
