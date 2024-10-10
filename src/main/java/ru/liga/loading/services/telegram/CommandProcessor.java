package ru.liga.loading.services.telegram;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface CommandProcessor {

    /**
     * Метод исполнения запрошенной команды.
     * @param message сообщение.
     * @return ответ-строку.
     */
    String process(Message message);
}
