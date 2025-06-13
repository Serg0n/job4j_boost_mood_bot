package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import org.springframework.beans.factory.BeanNameAware;

@Service
public class BotCommandHandler implements BeanNameAware {

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }

    void receive(Content content) {
        System.out.println(content);
    }

    @PostConstruct
    public void init() {
        System.out.println("BotCommandHandler инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("BotCommandHandler завершает работу");
    }
}
