package ru.liga.loading.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.exceptions.ParcelValidationException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.services.ParcelService;
import ru.liga.loading.validators.ParcelValidator;

@Service("create")
@RequiredArgsConstructor
public class CreatingParcelsProcessor implements CommandProcessor {

    private static final int PARCEL_NAME_INDEX = 1;
    private static final int PARCEL_SYMBOL_INDEX = 2;

    private final ParcelValidator parcelValidator;
    private final ParcelService parcelService;

    @Override
    public String process(Message message) {
        if (!message.hasDocument()) {
            return "You must download file with parcel box";
        }

        String[] words = message.getCaption().split("\\s");
        if (words.length < 3) {
            return "You must specify name and symbol of new parcel";
        }
        String name = words[PARCEL_NAME_INDEX];
        char symbol = words[PARCEL_SYMBOL_INDEX].charAt(0);

        Document document = message.getDocument();
        try {
            Parcel parcel = parcelService.createParcel(name, symbol, document);
            parcelValidator.validateBox(parcel);
            parcelService.saveParcel(parcel);
        } catch (ParcelValidationException e) {
            return "All box symbols must be parcel symbol";
        } catch (Exception e) {
            return "Can't create parcel (problems with reading your file or parcel already created)";
        }
        return "Parcel successfully created";
    }
}
