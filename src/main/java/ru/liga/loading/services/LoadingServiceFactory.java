package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.liga.loading.enums.LoadingMode;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadingServiceFactory {

    private final ApplicationContext context;

    /**
     * Фабричный метод для получения определённого сервиса погрузки в зависимости от способа.
     * @param mode способ погрузки
     * @return объект {@code LoadingService} соответсвующий заданному способу погрузки
     * @throws UnsupportedOperationException если нет сервиса погрузки с заданным способом
     */
    public LoadingService createLoadingServiceFromMode(LoadingMode mode) {
        return switch (mode) {
            case SIMPLE -> {
                log.info("Mode - simple loading");
                yield context.getBean(SimpleLoadingService.class);
            }
            case EFFECTIVE -> {
                log.info("Mode - effective loading");
                yield context.getBean("effective", LoadingService.class);
            }
            case UNIFORM -> {
                log.info("Mode - uniform loading");
                yield context.getBean(UniformLoadingService.class);
            }
            default -> throw new UnsupportedOperationException("No such loading service");
        };
    }
}
