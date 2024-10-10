package ru.liga.loading.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.readers.TelegramFileDownloader;
import ru.liga.loading.readers.TruckJsonReader;
import ru.liga.loading.services.TruckService;

@Service("specify")
@RequiredArgsConstructor
public class SpecifyingProcessor implements CommandProcessor {

    private final TelegramFileDownloader telegramFileDownloader;
    private final TruckService truckService;
    private final TruckJsonReader truckJsonReader;

    @Override
    public String process(Message message) {
        if (message.hasDocument()) {
            try {
                String filePath = telegramFileDownloader.download(message.getDocument());
                return truckService.getPrettyOutputForTrucks(truckJsonReader.readTrucksFromJson(filePath));
            } catch (Exception e) {
                return "Problem with reading your file";
            }
        }
        return "You must download file with loaded trucks";
    }
}
