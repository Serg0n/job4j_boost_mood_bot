package ru.job4j.bmb.engine;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class RecommendationEngine {
    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine завершает работу");
    }
}
