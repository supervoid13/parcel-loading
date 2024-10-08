package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.exceptions.ParcelValidationException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.readers.TelegramFileDownloader;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TelegramBotService {

    private final ParcelService parcelService;
    private final ParcelReader parcelReader;
    private final TelegramFileDownloader telegramFileDownloader;

    private static final int PARCEL_NAME_INDEX = 1;
    private static final int PARCEL_SYMBOL_INDEX = 2;
    private static final int PARCEL_NEW_NAME_INDEX = 3;

    /**
     * Метод погрузки грузовиков.
     * @param message полученное сообщение.
     * @return ответ-строку.
     */
    public String processLoading(Message message) {
        return "LOADING...";
    }

    /**
     * Метод определения количества и вида посылок в грузовике.
     * @param message полученное сообщение.
     * @return ответ-строку.
     */
    public String processSpecifying(Message message) {
        return "SPECIFYING...";
    }

    /**
     * Метод получения существующих посылок (если сообщение содержит имя посылки вернёт посылку по имени -
     * в противном случае все посылки.
     * @param message полученное сообщение.
     * @return ответ-строку (посылки в удобно-читаемой форме).
     */
    public String processRetrievingParcels(Message message) {
        String[] words = message.getText().split(" ");
        try {
            if (words.length == 1) {
                return parcelService.getPrettyOutputForAllParcels();
            }
            String name = words[PARCEL_NAME_INDEX];
            return parcelService.getPrettyOutputForParcelByName(name);
        } catch (NoSuchElementException e) {
            return e.getMessage();
        }
    }

    /**
     * Метод создания новой посылки.
     * @param message полученное сообщение.
     * @return ответ-строку.
     */
    public String processCreatingParcel(Message message) {
        if (!message.hasDocument()) {
            return "You must download file with parcel box";
        }

        String[] words = message.getCaption().split(" ");
        if (words.length < 3) {
            return "You must specify name and symbol of new parcel";
        }
        String name = words[PARCEL_NAME_INDEX];
        char symbol = words[PARCEL_SYMBOL_INDEX].charAt(0);

        Document document = message.getDocument();
        try {
            Parcel parcel = createParcel(name, symbol, document);
            parcelService.saveParcel(parcel);
        } catch (ParcelValidationException e) {
            return "All box symbols must be parcel symbol";
        } catch (Exception e) {
            return "Can't create parcel (problems with reading your file or parcel already created)";
        }
        return "Parcel successfully created";
    }

    private Parcel createParcel(String name, char symbol, Document document) {
        String fileId = document.getFileId(), fileName = document.getFileName();
        Parcel parcel;
        try {
            String filePath = telegramFileDownloader.download(fileId, fileName);
            parcel = parcelReader.readParcelFromFile(name, symbol, filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parcel;
    }

    /**
     * Метод редактирования существующей посылки.
     * @param message полученное сообщение.
     * @return ответ-строку.
     */
    public String processUpdatingParcel(Message message) {
        if (!message.hasDocument()) {
            return "You must download file with parcel box";
        }

        String[] words = message.getCaption().split(" ");
        if (words.length < 3) {
            return "You must specify name and symbol of new parcel";
        }
        String name = words[PARCEL_NAME_INDEX], newName = words[PARCEL_NEW_NAME_INDEX];
        char symbol = words[PARCEL_SYMBOL_INDEX].charAt(0);

        Document document = message.getDocument();
        try {
            Parcel parcel = createParcel(newName, symbol, document);
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


    /**
     * Метод удаления существующей посылки.
     * @param message полученное сообщение.
     * @return ответ-строку.
     */
    public String processDeletingParcel(Message message) {
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
