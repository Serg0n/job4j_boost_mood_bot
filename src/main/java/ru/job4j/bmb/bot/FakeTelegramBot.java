package ru.job4j.bmb.bot;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.bot.condition.OnFakeModeCondition;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;

@Service
@Conditional(OnFakeModeCondition.class)
public class FakeTelegramBot implements SentContent {

    @Override
    public void sent(Content content) {
        System.out.println("[FAKE BOT] Сообщение отправлено:");
        System.out.println("chatId: " + content.getChatId());
        System.out.println("text: " + content.getText());
        if (content.getPhoto() != null) {
            System.out.println("photo: " + content.getPhoto().getMediaName());
        } else if (content.getAudio() != null) {
            System.out.println("audio: " + content.getAudio().getMediaName());
        } else if (content.getVideo() != null) {
            System.out.println("video: " + content.getVideo().getMediaName());
        } else if (content.getMarkup() != null) {
            System.out.println("markup: " + content.getMarkup());
        }
    }
}
