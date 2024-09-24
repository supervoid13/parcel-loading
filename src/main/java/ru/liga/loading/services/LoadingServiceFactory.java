package ru.liga.loading.services;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.enums.LoadingMode;

@Slf4j
public class LoadingServiceFactory {

    /**
     * Фабричный метод для создания определённого сервиса погрузки в зависимости от способа.
     * @param mode способ погрузки
     * @return объект {@code LoadingService} соответсвующий заданному способу погрузки
     * @throws UnsupportedOperationException если нет сервиса погрузки с заданным способом
     */
    public LoadingService createLoadingServiceFromMode(LoadingMode mode) {
        return switch (mode) {
            case SIMPLE -> {
                log.info("Mode - simple loading");
                yield new SimpleLoadingService();
            }
            case EFFECTIVE -> {
                log.info("Mode - effective loading");
                yield new EffectiveLoadingService();
            }
            case UNIFORM -> {
                log.info("Mode - uniform loading");
                yield new UniformLoadingService();
            }
            default -> throw new UnsupportedOperationException("No such loading service");
        };
    }
}
