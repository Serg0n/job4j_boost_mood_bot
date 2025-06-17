package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@Component
public class ContentProviderVideo implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        Content content = new Content(chatId);
        File videoFile = new File("./videos/motivation.mp4");
        content.setVideo(new InputFile(videoFile)); // ⬅️ Для этого нужно добавить поле video в класс Content
        return content;
    }
}
