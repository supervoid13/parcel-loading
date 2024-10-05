package ru.liga.loading.config.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.liga.loading.controllers.ParcelLoaderBot;

@Slf4j
@Configuration
@PropertySource("classpath:application.yaml")
public class BotConfig {

    @Value("${bot.key}")
    private String TOKEN;

    @Bean
    public TelegramBotsLongPollingApplication botsApplication() {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        try {
            botsApplication.registerBot(TOKEN, parcelLoaderBot());
        } catch (TelegramApiException e) {
            log.error("Can't register telegram bot");
        }
        return botsApplication;
    }

    @Bean
    public LongPollingSingleThreadUpdateConsumer parcelLoaderBot() {
        return new ParcelLoaderBot(client());
    }

    @Bean
    public TelegramClient client() {
        return new OkHttpTelegramClient(TOKEN);
    }
}
