package ru.liga.loading.readers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

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
    private static final String STORAGE_DIRECTORY = "temp/";

    public String download(String fileId, String fileName) throws Exception {
        URL url = new URL(HOST + "/bot" + TOKEN + GET_FILE_INFO_PATTERN + fileId);

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String getFileResponse = reader.readLine();

        JSONObject jsObject = new JSONObject(getFileResponse);
        JSONObject path = jsObject.getJSONObject("result");
        String filePath = path.getString("file_path");

        String localFilePath = STORAGE_DIRECTORY + fileName;

        File localFile = new File(localFilePath);
        InputStream inputStream = new URL(HOST + "/file/bot" + TOKEN + "/" + filePath).openStream();

        FileUtils.copyInputStreamToFile(inputStream, localFile);

        reader.close();
        inputStream.close();

        return localFilePath;
    }
}
