package ru.liga.loading.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.services.ParcelService;

import java.util.NoSuchElementException;

@Service("delete")
@RequiredArgsConstructor
public class DeletingParcelsProcessor implements CommandProcessor {

    private static final int PARCEL_NAME_INDEX = 1;

    private final ParcelService parcelService;

    @Override
    public String process(Message message) {
        String text = message.getText();
        String[] words = text.split(" ");

        if (words.length == 1) {
            return "You didn't specify which parcel exactly to delete";
        }
        try {
            String name = words[PARCEL_NAME_INDEX];
            parcelService.deleteParcel(name);
        } catch (NoSuchElementException e) {
            return e.getMessage();
        }
        return "Parcel successfully deleted";
    }
}
