package ru.liga.loading.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.loading.enums.Command;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandProcessorFactory {

    private final Map<String, CommandProcessor> commandProcessorMap;

    public CommandProcessor createCommandProcessorFromCommand(Command command) {
        return commandProcessorMap.get(command.name().toLowerCase());
    }
}
