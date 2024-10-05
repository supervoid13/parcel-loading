package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.enums.LoadingMode;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadingServiceFactory {

    private final Map<String, LoadingService> loadingServiceMap;

    /**
     * Фабричный метод для получения определённого сервиса погрузки в зависимости от способа.
     * @param mode способ погрузки
     * @return объект {@code LoadingService} соответсвующий заданному способу погрузки
     * @throws UnsupportedOperationException если нет сервиса погрузки с заданным способом
     */
    public LoadingService createLoadingServiceFromMode(LoadingMode mode) {
        return loadingServiceMap.get(mode.name().toLowerCase());
    }
}
