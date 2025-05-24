package ru.job4j.bmb.core;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class AchievementService {

    @PostConstruct
    public void init() {
        System.out.println("AchievementService инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("AchievementService завершает работу");
    }
}
