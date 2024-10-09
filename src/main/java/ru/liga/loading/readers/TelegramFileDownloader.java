package ru.liga.loading.readers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class TelegramFileDownloader {

    @Value("${bot.key}")
    private String TOKEN;

    private static final String HOST = "https://api.telegram.org";
    private static final String GET_FILE_INFO_PATTERN = "/getFile?file_id=";
    private static final String STORAGE_DIRECTORY = "telegram/";

    /**
     * Метод загрузки файла с телеграмм сервера.
     * @param document телеграмм документ.
     * @return локальный путь скачанного файла.
     * @throws Exception если при скачивании произошла ошибка.
     */
    public String download(Document document) throws Exception {
        String fileId = document.getFileId(), fileName = document.getFileName();
        URL url = new URL(HOST + "/bot" + TOKEN + GET_FILE_INFO_PATTERN + fileId);

        String localFilePath;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String getFileResponse = reader.readLine();

            JSONObject jsObject = new JSONObject(getFileResponse);
            JSONObject path = jsObject.getJSONObject("result");
            String filePath = path.getString("file_path");

            localFilePath = STORAGE_DIRECTORY + fileName;

            File localFile = new File(localFilePath);
            try (InputStream inputStream = new URL(HOST + "/file/bot" + TOKEN + "/" + filePath).openStream()) {
                FileUtils.copyInputStreamToFile(inputStream, localFile);
            }
        }
        return localFilePath;
    }
}
