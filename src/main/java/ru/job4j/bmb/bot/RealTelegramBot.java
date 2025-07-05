package ru.job4j.bmb.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.bot.condition.OnRealModeCondition;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;

@Service
@Conditional(OnRealModeCondition.class)
public class RealTelegramBot extends TelegramLongPollingBot implements SentContent {

    private final String token;
    private final String username;

    public RealTelegramBot(@Value("${telegram.bot.token}") String token,
                           @Value("${telegram.bot.name}") String username) {
        this.token = token;
        this.username = username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Здесь можно обрабатывать входящие сообщения
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getText() != null) {
                SendMessage message = new SendMessage();
                message.setChatId(content.getChatId().toString());
                message.setText(content.getText());
                if (content.getMarkup() != null) {
                    message.setReplyMarkup(content.getMarkup());
                }
                execute(message);
            } else if (content.getPhoto() != null) {
                SendPhoto photo = new SendPhoto();
                photo.setChatId(content.getChatId().toString());
                photo.setPhoto(content.getPhoto());
                if (content.getText() != null) {
                    photo.setCaption(content.getText());
                }
                if (content.getMarkup() != null) {
                    photo.setReplyMarkup(content.getMarkup());
                }
                execute(photo);
            } else if (content.getAudio() != null) {
                SendAudio audio = new SendAudio();
                audio.setChatId(content.getChatId().toString());
                audio.setAudio(content.getAudio());
                execute(audio);
            } else if (content.getVideo() != null) {
                SendVideo video = new SendVideo();
                video.setChatId(content.getChatId().toString());
                video.setVideo(content.getVideo());
                execute(video);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
