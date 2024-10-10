package ru.liga.loading.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.exceptions.ParcelValidationException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.services.ParcelService;
import ru.liga.loading.validators.ParcelValidator;

import java.util.NoSuchElementException;

@Service("update")
@RequiredArgsConstructor
public class UpdatingParcelsProcessor implements CommandProcessor {

    private static final int PARCEL_NAME_INDEX = 1;
    private static final int PARCEL_SYMBOL_INDEX = 2;
    private static final int PARCEL_NEW_NAME_INDEX = 3;

    private final ParcelValidator parcelValidator;
    private final ParcelService parcelService;

    @Override
    public String process(Message message) {
        if (!message.hasDocument()) {
            return "You must download file with parcel box";
        }

        String[] words = message.getCaption().split("\\s");
        if (words.length < 4) {
            return "You must specify name, new name and symbol of updating parcel";
        }
        String name = words[PARCEL_NAME_INDEX], newName = words[PARCEL_NEW_NAME_INDEX];
        char symbol = words[PARCEL_SYMBOL_INDEX].charAt(0);

        Document document = message.getDocument();
        try {
            Parcel parcel = parcelService.createParcel(newName, symbol, document);
            parcelValidator.validateBox(parcel);
            parcelService.updateParcelHavingBox(name, parcel);
        } catch (ParcelValidationException e) {
            return "All box symbols must be parcel symbol";
        } catch (NoSuchElementException e) {
            return "No such parcel";
        } catch (Exception e) {
            return "Can't update parcel (problems with reading your file)";
        }
        return "Parcel successfully updated";
    }
}
