package ru.liga.loading.config.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Configuration
public class BotConfig {

    @Value("${bot.key}")
    private static String TOKEN;

    @Bean
    public TelegramClient client() {
        return new OkHttpTelegramClient(TOKEN);
    }

    @Value("${bot.key}")
    public void setToken(String token) {
        TOKEN = token;
    }
}
