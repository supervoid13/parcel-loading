package ru.liga.loading.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.liga.loading.enums.Command;
import ru.liga.loading.services.TgCommandProcessorService;
import ru.liga.loading.utils.TgCommandProcessor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ParcelLoaderBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    @Value("${bot.key}")
    private String TOKEN;

    private final TelegramClient tgClient;
    private final TgCommandProcessorService tgCommandProcessorService;


    @Override
    public void consume(Update update) {
        Message message;
        if (update.hasMessage()) {
            message = update.getMessage();
        } else {
            return;
        }

        boolean hasText = message.hasText(), hasCaption = message.hasCaption();
        String answer;

        if (hasText || hasCaption) {
            String messageText = hasText ? message.getText() : message.getCaption();
            String commandStr = messageText.split(" ")[0];
            try {
                Command command = Command.valueOf(commandStr.toUpperCase());
                Map<Command, TgCommandProcessor> commandProcessorMap = tgCommandProcessorService.getCommandProcessorMap();
                answer = commandProcessorMap.get(command).process(message);
            } catch (IllegalArgumentException e) {
                answer = "No such command";
            }
        } else {
            answer = "Please write something";
        }
        sendMessage(message.getChatId(), answer);
    }

    private void sendMessage(long chatId, String answer) {
        SendMessage message = prepareMessage(chatId, answer);

        try {
            tgClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Can't execute message");
        }
    }

    private SendMessage prepareMessage(long chatId, String answer) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(answer)
                .build();
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}
