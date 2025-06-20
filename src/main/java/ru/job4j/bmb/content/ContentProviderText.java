package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;

@Component
public class ContentProviderText implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        Content content = new Content(chatId);
        content.setText("Сегодня отличный день для новых достижений!");
        return content;
    }
}