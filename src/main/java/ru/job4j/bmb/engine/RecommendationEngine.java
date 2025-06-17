package ru.job4j.bmb.engine;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.ContentProvider;

import java.util.List;
import java.util.Random;

@Service
public class RecommendationEngine {
    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine инициализирован");
    }

        private final List<ContentProvider> contents;
        private static final Random RND = new Random(System.currentTimeMillis());

        public RecommendationEngine(List<ContentProvider> contents) {
            this.contents = contents;
        }

        public Content recommendFor(Long chatId, Long moodId) {
            var index = RND.nextInt(0, contents.size());
            return contents.get(index).byMood(chatId, moodId);
        }

    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine завершает работу");
    }
}
