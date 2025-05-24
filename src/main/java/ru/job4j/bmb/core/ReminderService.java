package ru.job4j.bmb.core;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class ReminderService {
    @PostConstruct
    public void init() {
        System.out.println("ReminderService инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("ReminderService завершает работу");
    }
}
