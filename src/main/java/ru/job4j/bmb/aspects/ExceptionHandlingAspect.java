package ru.job4j.bmb.aspects;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    // Аспект срабатывает после выбрасывания исключения в методах пакета service
    @AfterThrowing(pointcut = "execution(* ru.job4j.bmb..*.*(..))", throwing = "ex")
    public void handleException(Exception ex) {
        // Логируем сообщение об ошибке
        LOGGER.error("An error occurred: {}", ex.getMessage());
    }
}
