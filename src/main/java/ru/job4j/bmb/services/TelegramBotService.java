package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;

@Service
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler) {
        super(botToken);
        this.botName = botName;
        this.handler = handler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery()).ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handler.commands(update.getMessage()).ifPresent(this::sent);
        }
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getAudio() != null) {
                SendAudio audio = new SendAudio();
                audio.setChatId(content.getChatId());
                audio.setAudio(content.getAudio());
                audio.setCaption(content.getText());
                execute(audio);
                return;
            }
            if (content.getVideo() != null) {
                SendVideo video = new SendVideo();
                video.setChatId(content.getChatId());
                video.setVideo(content.getVideo());
                video.setCaption(content.getText());
                execute(video);
                return;
            }
            if (content.getPhoto() != null) {
                SendPhoto photo = new SendPhoto();
                photo.setChatId(content.getChatId());
                photo.setPhoto(content.getPhoto());
                photo.setCaption(content.getText());
                execute(photo);
                return;
            }
            if (content.getText() != null) {
                SendMessage message = new SendMessage();
                message.setChatId(content.getChatId());
                message.setText(content.getText());
                if (content.getMarkup() != null) {
                    message.setReplyMarkup(content.getMarkup());
                }
                execute(message);
            }

        } catch (TelegramApiException e) {
            throw new SentContentException("Ошибка отправки сообщения", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
