package ru.liga.loading.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.enums.Command;
import ru.liga.loading.utils.TgCommandProcessor;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TgCommandProcessorService {

    private final Map<Command, TgCommandProcessor> commandProcessorMap = new EnumMap<>(Command.class);
    private final TelegramBotService tgService;

    @PostConstruct
    private void init() {
        commandProcessorMap.put(Command.LOAD, tgService::processLoading);
        commandProcessorMap.put(Command.SPECIFY, tgService::processSpecifying);
        commandProcessorMap.put(Command.PARCELS, tgService::processRetrievingParcels);
        commandProcessorMap.put(Command.CREATE, tgService::processCreatingParcel);
        commandProcessorMap.put(Command.UPDATE, tgService::processUpdatingParcel);
        commandProcessorMap.put(Command.DELETE, tgService::processDeletingParcel);
    }

    public String processCommand(Command command, Message message) {
        return commandProcessorMap.get(command).process(message);
    }
}
